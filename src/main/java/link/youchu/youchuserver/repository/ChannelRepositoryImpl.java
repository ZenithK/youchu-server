package link.youchu.youchuserver.repository;

import com.google.gson.Gson;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import link.youchu.youchuserver.Dto.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;

import java.security.InvalidKeyException;
import java.util.*;

import static link.youchu.youchuserver.domain.QChannel.*;
import static link.youchu.youchuserver.domain.QChannelKeyword.*;
import static link.youchu.youchuserver.domain.QChannelTopic.*;
import static link.youchu.youchuserver.domain.QDislikeChannels.*;
import static link.youchu.youchuserver.domain.QPrefferedChannels.*;
import static link.youchu.youchuserver.domain.QTopic.*;

public class ChannelRepositoryImpl implements ChannelRepositoryCustom {

    @Value("${api.address}")
    private String scoring_url;
    @Value("${api.key}")
    private String api_key;

    private final JPAQueryFactory queryFactory;

    public ChannelRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public ChannelDto getChannelData(ChannelSearchCondition condition) {
        return queryFactory
                .select(new QChannelDto(channel.id, channel.title, channel.description, channel.publishedAt,
                        channel.thumbnail, channel.viewCount, channel.subScribeCount, channel.bannerImage,
                        channel.video_count, channel.channel_id))
                .from(channel)
                .where(channelIdEq(condition.getChannel_id()),
                        channelIndexEq(condition.getChannel_index()))
                .fetchOne();
    }

    @Override
    public List<VideoDto> getChannelVideo(String channel_id) throws ParseException, InvalidKeyException {
        RestTemplate restTemplate = new RestTemplate();
        List<VideoDto> channelVideo = new ArrayList<>();
        try {
            int count = 0;
            // Channel playlist
            String requestUrl = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/youtube/v3/channels")
                    .queryParam("part", "contentDetails")
                    .queryParam("id", channel_id)
                    .queryParam("key", api_key).encode().toUriString();

            JSONParser parser = new JSONParser();

            // playlist result
            String resultJson = restTemplate.getForObject(requestUrl, String.class);

            Object parse = parser.parse(resultJson);
            JSONObject jsonObject = (JSONObject) parse;
            JSONArray items = (JSONArray) jsonObject.get("items");
            String uploads = ((JSONObject)((JSONObject)((JSONObject) items.get(0)).get("contentDetails")).get("relatedPlaylists")).get("uploads").toString();

            String itemRequest = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/youtube/v3/playlistItems")
                    .queryParam("part", "contentDetails")
                    .queryParam("playlistId", uploads)
                    .queryParam("maxResults", 10)
                    .queryParam("key", api_key).encode().toUriString();

            String resultVideo = restTemplate.getForObject(itemRequest, String.class);

            JSONObject json = (JSONObject) parser.parse(resultVideo);
            JSONArray videoItems = (JSONArray) json.get("items");

            for (int i = 0; i < videoItems.size(); i++) {
                JSONObject jsonObject1 = (JSONObject) videoItems.get(i);
                JSONObject content = (JSONObject) jsonObject1.get("contentDetails");
                String videoId = content.get("videoId").toString();
                String videoPublishedAt = content.get("videoPublishedAt").toString().substring(0, 10);
                String videoRequest = UriComponentsBuilder.fromHttpUrl("https://www.googleapis.com/youtube/v3/videos")
                            .queryParam("part", "snippet,statistics")
                            .queryParam("id", videoId)
                            .queryParam("key", api_key)
                            .encode().toUriString();
                String videos = restTemplate.getForObject(videoRequest, String.class);
                JSONObject parse1 = (JSONObject) parser.parse(videos);
                JSONArray jsonArray = (JSONArray) parse1.get("items");
                Long viewCount = Long.parseLong(((JSONObject) ((JSONObject) jsonArray.get(0)).get("statistics")).get("viewCount").toString());
                String title = ((JSONObject) ((JSONObject) jsonArray.get(0)).get("snippet")).get("title").toString();
                String url = ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) jsonArray.get(0)).get("snippet")).get("thumbnails")).get("medium")).get("url").toString();
                VideoDto videoDto = new VideoDto(url, title, videoPublishedAt, viewCount);
                channelVideo.add(videoDto);
                }
        } catch (ParseException | HttpClientErrorException.NotFound e) {
            throw new ParseException(0, "채널에 업로드된 영상이 없습니다.");
        } catch (HttpClientErrorException e) {
            throw new InvalidKeyException();
        }
        return channelVideo;
    }

    private BooleanExpression channelIdEq(String channel_id) {
        return channel_id == null ? null : channel.channel_id.eq(channel_id);
    }

    private BooleanExpression channelIndexEq(Long channel_index) {
        return channel_index == null ? null : channel.id.eq(channel_index);
    }


    @Override
    public Page<SimpleDtoPlusBanner> getChannelByTopic(TopicSearchCondition condition, Pageable pageable) {
        QueryResults<SimpleDtoPlusBanner> results = queryFactory
                .select(new QSimpleDtoPlusBanner(channelTopic.channel.id, channelTopic.channel.title, channelTopic.channel.thumbnail, channelTopic.channel.subScribeCount, channelTopic.channel.bannerImage, channelTopic.channel.channel_id))
                .distinct()
                .from(channelTopic)
                .join(channelTopic.topic, topic)
                .where(topicIdEq(condition.getTopic_index()),
                        topicNameEq(condition.getTopic_name()),
                        country())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(channelTopic.channel.subScribeCount.desc())
                .fetchResults();

        List<SimpleDtoPlusBanner> content = results.getResults();
        Long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression country() {
        return channelTopic.channel.country.eq("KR");
    }

    private BooleanExpression topicIdEq(Long topic_id) {
        return topic_id == null ? null : channelTopic.topic.id.eq(topic_id);
    }

    private BooleanExpression topicNameEq(String topic_name) {
        return topic_name == null ? null : channelTopic.topic.topic_name.eq(topic_name);
    }

    private BooleanExpression keywordIdEq(Long keyword_id) {
        return keyword_id == null ? null : channelKeyword.keyword.id.eq(keyword_id);
    }

    @Override
    public Page<SimpleDtoPlusBanner> getChannelByKeyword(KeywordSearchCondition condition, Pageable pageable) {
        QueryResults<SimpleDtoPlusBanner> results = queryFactory
                .select(new QSimpleDtoPlusBanner(channelKeyword.channel.id, channelKeyword.channel.title, channelKeyword.channel.thumbnail, channelKeyword.channel.subScribeCount, channelKeyword.channel.bannerImage, channelKeyword.channel.channel_id))
                .from(channelKeyword)
                .join(channelKeyword.channel, channel)
                .where(keywordIdEq(condition.getKeyword_id()),
                        keywordNameEq(condition.getKeyword_name()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(channel.subScribeCount.desc())
                .fetchResults();

        List<SimpleDtoPlusBanner> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<SimpleChannelDto> getChannelByOneKeyword(KeywordSearchCondition condition, Pageable pageable) {
        QueryResults<SimpleChannelDto> results = queryFactory
                .select(new QSimpleChannelDto(channelKeyword.channel.id, channelKeyword.channel.title, channelKeyword.channel.thumbnail, channelKeyword.channel.subScribeCount, channelKeyword.channel.channel_id))
                .distinct()
                .from(channelKeyword)
                .join(channelKeyword.channel, channel)
                .where(keywordIdEq(condition.getKeyword_id()),
                        keywordNameEq(condition.getKeyword_name()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(channel.subScribeCount.desc())
                .fetchResults();

        List<SimpleChannelDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression keywordNameEq(String keyword_name) {
        return keyword_name == null ? null : channelKeyword.keyword.keyword_name.eq(keyword_name);
    }

    @Override
    public ChannelDto getRandomChannel(UserSearchCondition condition) {
        List<Long> result = queryFactory.select(dislikeChannels.channel.id)
                .from(dislikeChannels)
                .where(dislikeUserIdEq(condition.getUser_id()),
                        dislikeGoogleIdEq(condition.getGoogle_user_id()))
                .fetch();

        List<Long> resultPrefer = queryFactory.select(prefferedChannels.channel.id)
                .from(prefferedChannels)
                .where(preferUserIdEq(condition.getUser_id()),
                        preferGoogleIdEq(condition.getGoogle_user_id()))
                .fetch();
        result.addAll(resultPrefer);

        List<ChannelDto> list = queryFactory
                .select(new QChannelDto(channel.id, channel.title, channel.description, channel.publishedAt,
                        channel.thumbnail, channel.viewCount, channel.subScribeCount, channel.bannerImage,
                        channel.video_count, channel.channel_id))
                .from(channel)
                .where(channelIndexNEq(result))
                .fetch();
        Random random = new Random();
        int randValue = random.nextInt(list.size());
        return list.get(randValue);

    }

    private BooleanExpression preferUserIdEq(Long user_id) {
        return user_id == null ? null : prefferedChannels.users.user_id.eq(user_id);
    }

    private BooleanExpression preferGoogleIdEq(String google_user_id) {
        return google_user_id == null ? null : prefferedChannels.users.google_user_id.eq(google_user_id);
    }

    private BooleanExpression dislikeUserIdEq(Long user_id) {
        return user_id == null ? null : dislikeChannels.users.user_id.eq(user_id);
    }

    private BooleanExpression dislikeGoogleIdEq(String google_user_id) {
        return google_user_id == null ? null : dislikeChannels.users.google_user_id.eq(google_user_id);
    }

    private BooleanExpression channelIndexNEq(List<Long> channelIndices) {
        return channelIndices == null ? null : channel.id.notIn(channelIndices);
    }

    @Override
    public SimpleChannelDto getRecommnedChannel(Long index) {
        return queryFactory.select(new QSimpleChannelDto(channel.id, channel.title, channel.thumbnail, channel.subScribeCount, channel.channel_id))
                .from(channel)
                .where(channelIndexEq(index))
                .fetchOne();
    }

    @Override
    public Page<SimpleChannelDto> getRecommendChannelList(List<Long> channel_indices, Pageable pageable, UserSearchCondition condition) {
        List<Long> result = queryFactory.select(dislikeChannels.channel.id)
                .from(dislikeChannels)
                .where(dislikeUserIdEq(condition.getUser_id()),
                        dislikeGoogleIdEq(condition.getGoogle_user_id()))
                .fetch();

        List<Long> resultPrefer = queryFactory.select(prefferedChannels.channel.id)
                .from(prefferedChannels)
                .where(preferUserIdEq(condition.getUser_id()),
                        preferGoogleIdEq(condition.getGoogle_user_id()))
                .fetch();
        result.addAll(resultPrefer);

        QueryResults<SimpleChannelDto> results = queryFactory.select(new QSimpleChannelDto(channel.id, channel.title, channel.thumbnail, channel.subScribeCount, channel.channel_id))
                .from(channel)
                .where(channelIndicesEq(channel_indices),
                        channelIndexNEq(result))
                .orderBy(channel.subScribeCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<SimpleChannelDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<SimpleChannelDto> getChannelRandom(Pageable pageable) {
        QueryResults<SimpleChannelDto> results = queryFactory.select(new QSimpleChannelDto(channel.id, channel.title, channel.thumbnail, channel.subScribeCount, channel.channel_id))
                .from(channel)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(channel.subScribeCount.desc())
                .fetchResults();

        List<SimpleChannelDto> list = results.getResults();

        long total = results.getTotal();

        return new PageImpl<>(list, pageable, total);
    }

    private BooleanExpression channelIndicesEq(List<Long> channel_indices) {
        if (channel_indices == null) {
            return null;
        } else {
            return channel.id.in(channel_indices);
        }
    }

    @Override
    public Long getChannelIndex(ChannelSearchCondition condition) {
        return queryFactory.select(channel.id)
                .from(channel)
                .where(channelIndexEq(condition.getChannel_index()),
                        channelIdEq(condition.getChannel_id()))
                .fetchOne();
    }

    @Override
    public List<Long> getSimilarChannel(List<Integer> data) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, List<Integer>> map = new HashMap<>();
            map.put("data", data);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Gson gson = new Gson();
            String json = new Gson().toJson(map);
            HttpEntity entity = new HttpEntity(json, headers);
            JSONParser parser = new JSONParser();
            String resultJson = restTemplate.postForObject(scoring_url + "/recommand", entity, String.class);
            System.out.println(resultJson);
            Object obj = parser.parse(resultJson);
            JSONArray jsonArray = (JSONArray) obj;
            List<Long> channels = new ArrayList<>();
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    channels.add(Long.parseLong(jsonArray.get(i).toString()));
                }
            }
            return channels;
        } catch (Exception e) {
            System.out.println(e);
        }


        return null;
    }

    @Override
    public Page<SimpleDtoPlusBanner> getRelateChannelList(List<Long> channel_indices, Pageable pageable, UserSearchCondition condition) {
        List<Long> result = queryFactory.select(dislikeChannels.channel.id)
                .from(dislikeChannels)
                .where(dislikeUserIdEq(condition.getUser_id()),
                        dislikeGoogleIdEq(condition.getGoogle_user_id()))
                .fetch();

        List<Long> resultPrefer = queryFactory.select(prefferedChannels.channel.id)
                .from(prefferedChannels)
                .where(preferUserIdEq(condition.getUser_id()),
                        preferGoogleIdEq(condition.getGoogle_user_id()))
                .fetch();
        result.addAll(resultPrefer);

        QueryResults<SimpleDtoPlusBanner> results = queryFactory.select(new QSimpleDtoPlusBanner(channel.id, channel.title, channel.thumbnail, channel.subScribeCount, channel.bannerImage, channel.channel_id))
                .from(channel)
                .where(channelIndicesEq(channel_indices),
                        channelIndexNEq(result))
                .orderBy(channel.subScribeCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();


        List<SimpleDtoPlusBanner> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<SimpleDtoPlusBanner> getRandomChannelBanner(Pageable pageable) {
        QueryResults<SimpleDtoPlusBanner> results = queryFactory.select(new QSimpleDtoPlusBanner(channel.id, channel.title, channel.thumbnail, channel.subScribeCount, channel.bannerImage, channel.channel_id))
                .from(channel)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(channel.subScribeCount.desc())
                .fetchResults();

        List<SimpleDtoPlusBanner> list = results.getResults();

        long total = results.getTotal();

        return new PageImpl<>(list, pageable, total);
    }

    @Override
    public List<Long> getRelatedChannel(Long channel_index) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Long> map = new HashMap<>();
            map.put("channel", channel_index);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            Gson gson = new Gson();
            String json = new Gson().toJson(map);
            HttpEntity entity = new HttpEntity(json, headers);

            String resultJson = restTemplate.postForObject(scoring_url + "/channel", entity, String.class);
            JSONParser parser = new JSONParser();
            Object parse = parser.parse(resultJson);
            JSONArray jsonArray = (JSONArray) parse;
            List<Long> channels = new ArrayList<>();
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    channels.add(Long.parseLong(jsonArray.get(i).toString()));
                }
            }
                return channels;
            }catch(ParseException e){
                e.printStackTrace();
            }
            return null;
        }
    }