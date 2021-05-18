package link.youchu.youchuserver.repository;

import com.google.gson.Gson;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import link.youchu.youchuserver.Dto.*;
import link.youchu.youchuserver.domain.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

import java.util.*;

import static link.youchu.youchuserver.domain.QChannel.*;
import static link.youchu.youchuserver.domain.QChannelKeyword.*;
import static link.youchu.youchuserver.domain.QChannelTopic.*;
import static link.youchu.youchuserver.domain.QKeyword.*;
import static link.youchu.youchuserver.domain.QTopic.*;
import static link.youchu.youchuserver.domain.QUsers.users;

public class ChannelRepositoryImpl implements ChannelRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ChannelRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public ChannelDto getChannelData(ChannelSearchCondition condition) {
        return queryFactory
                .select(new QChannelDto(channel.id,channel.title,channel.description,channel.publishedAt,
                channel.thumbnail,channel.viewCount,channel.subScribeCount,channel.bannerImage,
                        channel.video_count,channel.channel_id))
                .from(channel)
                .where(channelIdEq(condition.getChannel_id()),
                        channelIndexEq(condition.getChannel_index()))
                .fetchOne();
    }

    private BooleanExpression channelIdEq(String channel_id){
        return channel_id == null ? null : channel.channel_id.eq(channel_id);
    }

    private BooleanExpression channelIndexEq(Long channel_index){
        return channel_index == null ? null : channel.id.eq(channel_index);
    }


    @Override
    public Page<SimpleChannelDto> getChannelByTopic(TopicSearchCondition condition, Pageable pageable) {
        QueryResults<SimpleChannelDto> results = queryFactory
                .select(new QSimpleChannelDto(channelTopic.channel.title,channelTopic.channel.thumbnail,channelTopic.channel.subScribeCount,channelTopic.channel.channel_id))
                .from(channelTopic)
                .join(channelTopic.topic, topic)
                .where(topicIdEq(condition.getTopic_index()),
                        topicNameEq(condition.getTopic_name()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(channelTopic.channel.subScribeCount.desc())
                .fetchResults();

        List<SimpleChannelDto> content = results.getResults();
        Long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression topicIdEq(Long topic_id){
        return topic_id == null ? null : channelTopic.topic.id.eq(topic_id);
    }

    private BooleanExpression topicNameEq(String topic_name) {
        return topic_name == null ? null : channelTopic.topic.topic_name.eq(topic_name);
    }
    private BooleanExpression keywordIdEq(Long keyword_id) {
        return keyword_id == null ? null : channelKeyword.keyword.id.eq(keyword_id);
    }

    @Override
    public Page<SimpleChannelDto> getChennelByOneKeyword(KeywordSearchCondition condition, Pageable pageable) {
        QueryResults<SimpleChannelDto> results = queryFactory
                .select(new QSimpleChannelDto(channelTopic.channel.title,channelTopic.channel.thumbnail,channelTopic.channel.subScribeCount,channelTopic.channel.channel_id))
                .from(channel)
                .join(channel.channelKeywords, channelKeyword)
                .where(keywordIdEq(condition.getKeyword_id()),
                        keywordNameEq(condition.getKeyword_name()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<SimpleChannelDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression keywordNameEq(String keyword_name) {
        return keyword_name == null ? null : channelKeyword.keyword.keyword_name.eq(keyword_name);
    }

    @Override
    public ChannelDto getRandomChannel() {
        List<ChannelDto> list = queryFactory
                .select(new QChannelDto(channel.id,channel.title, channel.description, channel.publishedAt,
                        channel.thumbnail, channel.viewCount, channel.subScribeCount, channel.bannerImage,
                        channel.video_count, channel.channel_id))
                .from(channel)
                .fetch();
        Random random = new Random();
        int randValue = random.nextInt(list.size());
        return list.get(randValue);

    }


    @Override
    public SimpleChannelDto getRecommnedChannel(Long index) {
        return queryFactory.select(new QSimpleChannelDto(channel.title, channel.thumbnail, channel.subScribeCount, channel.channel_id))
                .from(channel)
                .where(channelIndexEq(index))
                .fetchOne();


    }

    @Override
    public Page<SimpleChannelDto> getRecommendChannelList(List<Long> channel_indices,Pageable pageable) {
        QueryResults<SimpleChannelDto> results = queryFactory.select(new QSimpleChannelDto(channel.title, channel.thumbnail, channel.subScribeCount, channel.channel_id))
                .from(channel)
                .where(channelIndicesEq(channel_indices))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<SimpleChannelDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression channelIndicesEq(List<Long> channel_indices){
        if(channel_indices == null){
            return null;
        }
        else{
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
    public List<Long> getSimilarUser(List<Integer> data) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String scoring_url = "http://f021bb9d-de3a-4342-8633-8192ac642e03.koreacentral.azurecontainer.io/score";
            String key = "PkEoFWebZeCEefDt4duQcEIOB5EJumrF";
            Map<String,List<Integer>> map = new HashMap<>();
            map.put("data",data);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization","Bearer " + key);
            headers.setContentType(MediaType.APPLICATION_JSON);
            Gson gson = new Gson();
            String json = new Gson().toJson(map);
            HttpEntity entity = new HttpEntity(json,headers);

            Object[] o = restTemplate.postForObject(scoring_url, entity, Object[].class);
            System.out.println((List<Long>)o[0]);
            return (List<Long>) (o[0]);
        }catch (Exception e){
            System.out.println(e);
        }


        return null;
    }
}
