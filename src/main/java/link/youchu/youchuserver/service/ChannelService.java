package link.youchu.youchuserver.service;

import link.youchu.youchuserver.Dto.*;
import link.youchu.youchuserver.Http.ComplexMessage;
import link.youchu.youchuserver.Dto.VideoDto;
import link.youchu.youchuserver.repository.*;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.InvalidKeyException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final PrefferedChannelsRepository prefferedChannelsRepository;
    private final DatasetUserRepository datasetUserRepository;
    private final TopicRepository topicRepository;
    private final KeywordRepository keywordRepository;

    @Transactional
    public ChannelDto getChannelData(ChannelSearchCondition channelSearchCondition){
        return channelRepository.getChannelData(channelSearchCondition);
    }

    @Transactional
    public Page<SimpleDtoPlusBanner> getRankingChannelByTopic(TopicSearchCondition condition, Pageable pageable){
        Page<SimpleDtoPlusBanner> channelByTopic = channelRepository.getChannelByTopic(condition, pageable);
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setUser_id(condition.getUser_id());
        List<ChannelDto> prefferedList = prefferedChannelsRepository.getPrefferedList(userSearchCondition);
        for (ChannelDto c : prefferedList) {
            for (SimpleDtoPlusBanner s : channelByTopic) {
                if(s.getChannel_id().equals(c.getChannel_id())){
                    s.setIsPreferred(true);
                }
            }
        }
        return channelByTopic;
    }

    @Transactional
    public ComplexMessage getSimilarTopic(UserSearchCondition condition,Pageable pageable) {
        List<ChannelDto> list = prefferedChannelsRepository.getPreffered(condition);
        Random random = new Random();
        int randValue = random.nextInt(list.size());
        ChannelDto randomChannel = list.get(randValue);
        ChannelSearchCondition channelSearchCondition = new ChannelSearchCondition();
        channelSearchCondition.setChannel_index(randomChannel.getChannel_index());
        List<TopicDto> topicList = topicRepository.getTopicList(channelSearchCondition);
        randValue = random.nextInt(topicList.size());
        TopicDto topicDto = topicList.get(randValue);
        TopicSearchCondition topicSearchCondition = new TopicSearchCondition();
        topicSearchCondition.setTopic_name(topicDto.getTopic_name());
        Page<SimpleDtoPlusBanner> channelByTopic = channelRepository.getChannelByTopic(topicSearchCondition, pageable);
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setUser_id(condition.getUser_id());
        List<ChannelDto> prefferedList = prefferedChannelsRepository.getPrefferedList(userSearchCondition);
        for (ChannelDto c : prefferedList) {
            for (SimpleDtoPlusBanner s : channelByTopic) {
                if(s.getChannel_id().equals(c.getChannel_id())){
                    s.setIsPreferred(true);
                }
            }
        }
        ComplexMessage message = new ComplexMessage();
        message.setStandardValue(randomChannel.getTitle());
        message.setData(channelByTopic);
        return message;
    }

    @Transactional
    public ChannelDto getRandomChannel(UserSearchCondition condition) {
        return channelRepository.getRandomChannel(condition);
    }

    @Transactional
    public ComplexMessage getSimilarKeyword(UserSearchCondition condition, Pageable pageable) {
        List<ChannelDto> list = prefferedChannelsRepository.getPreffered(condition);
        Random random = new Random();
        int randValue = random.nextInt(list.size());
        ChannelDto randomChannel = list.get(randValue);
        ChannelSearchCondition channelSearchCondition = new ChannelSearchCondition();
        channelSearchCondition.setChannel_index(randomChannel.getChannel_index());
        List<KeywordDto> keywordList = keywordRepository.getKeywordList(channelSearchCondition);
        randValue = random.nextInt(keywordList.size());
        KeywordDto keywordDto = keywordList.get(randValue);
        KeywordSearchCondition keywordSearchCondition = new KeywordSearchCondition();
        keywordSearchCondition.setKeyword_name(keywordDto.getKeyword_name());
        Page<SimpleDtoPlusBanner> keyword = channelRepository.getChannelByKeyword(keywordSearchCondition, pageable);
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setUser_id(condition.getUser_id());
        List<ChannelDto> prefferedList = prefferedChannelsRepository.getPrefferedList(userSearchCondition);
        for (ChannelDto c : prefferedList) {
            for (SimpleDtoPlusBanner s : keyword) {
                if(s.getChannel_id().equals(c.getChannel_id())){
                    s.setIsPreferred(true);
                }
            }
        }
        ComplexMessage message = new ComplexMessage();
        message.setStandardValue(randomChannel.getTitle());
        message.setData(keyword);
        return message;
    }

    @Transactional
    public ComplexMessage getRelatedChannel(UserSearchCondition condition){
        List<Long> preferIndex = null;
        try {
            preferIndex = prefferedChannelsRepository.getPrefferedChannelIndex(condition);
        }catch (Exception e){
            e.printStackTrace();
        }
        List<Long> recommendIndex = new ArrayList<>();
        List<Integer> data = new ArrayList<Integer>(Collections.nCopies(21928,0));
        if(preferIndex.size()!=0){
            for(Long index : preferIndex){
                data.set((int) (index - 1), 1);
            }
            recommendIndex = channelRepository.getSimilarChannel(data);
            Random random = new Random();
            Long randValue = recommendIndex.get(random.nextInt(recommendIndex.size()));
            ChannelSearchCondition channelSearchCondition = new ChannelSearchCondition();
            channelSearchCondition.setChannel_index(randValue);
            ChannelDto channelData = getChannelData(channelSearchCondition);
            List<Long> channels = channelRepository.getRelatedChannel(randValue).subList(0,100);
            channels.forEach(s->s=s+1);
            UserSearchCondition userSearchCondition = new UserSearchCondition();
            userSearchCondition.setUser_id(condition.getUser_id());
            ComplexMessage message = new ComplexMessage();
            List<SimpleDtoPlusBanner> recommendChannelList = channelRepository.getRelateChannelList(channels, userSearchCondition);
            List<SimpleDtoPlusBanner> collect = recommendChannelList.stream()
                    .sorted((a,b)->{
                        long a_index = channels.indexOf(a.getChannel_index());
                        long b_index = channels.indexOf(b.getChannel_index());
                        return Long.compare(a_index,b_index);}).collect(Collectors.toList());
            message.setData(collect);
            message.setStandardValue(channelData.getTitle());
            return message;
        }else{
            List<SimpleDtoPlusBanner> channelDtos = channelRepository.getRandomChannelBanner();
            ComplexMessage message = new ComplexMessage();
            message.setData(channelDtos);
            return message;
        }


    }

    @Transactional
    public List<VideoDto> getLatestChannel(ChannelSearchCondition condition) throws ParseException, InvalidKeyException {
        return channelRepository.getChannelVideo(condition.getChannel_id());
    }

    @Transactional
    public Page<SimpleChannelDto> getChannelByOneKeyword(KeywordSearchCondition condition, Pageable pageable) {
        Page<SimpleChannelDto> keyword = channelRepository.getChannelByOneKeyword(condition, pageable);
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setUser_id(condition.getUser_id());
        List<ChannelDto> prefferedList = prefferedChannelsRepository.getPrefferedList(userSearchCondition);
        for (ChannelDto c : prefferedList) {
            for (SimpleChannelDto s : keyword) {
                if(s.getChannel_id().equals(c.getChannel_id())){
                    s.setIsPreferred(true);
                }
            }
        }
        return keyword;
    }

    @Transactional
    public List<SimpleChannelDto> getRecommendChannel(UserSearchCondition condition) {
        List<Long> preferIndex = null;
        try {
            preferIndex = prefferedChannelsRepository.getPrefferedChannelIndex(condition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Integer> data = new ArrayList<Integer>(Collections.nCopies(21928, 0));
        if (preferIndex.size() != 0) {
            for (Long index : preferIndex) {
                data.set((int) (index - 1), 1);
            }
            List<Long> recommendIndex = channelRepository.getSimilarChannel(data).subList(0,100);
            recommendIndex.stream().forEach(s -> s = s + 1);
            List<SimpleChannelDto> recommendChannelList = channelRepository.getRecommendChannelList(recommendIndex, condition);
            List<SimpleChannelDto> collect = recommendChannelList.stream()
                    .sorted((a, b) -> {
                        long a_index = recommendIndex.indexOf(a.getChannel_index());
                        long b_index = recommendIndex.indexOf(b.getChannel_index());
                        return Long.compare(a_index, b_index);
                    }).collect(Collectors.toList());
            return collect;
        } else {
            return channelRepository.getChannelRandom();
        }

    }

}
