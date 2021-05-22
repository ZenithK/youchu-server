package link.youchu.youchuserver.service;

import link.youchu.youchuserver.Dto.*;
import link.youchu.youchuserver.Http.ComplexMessage;
import link.youchu.youchuserver.Dto.VideoDto;
import link.youchu.youchuserver.repository.*;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    public ComplexMessage getRelatedChannel(UserSearchCondition condition, Pageable pageable){
        Page<SimpleChannelDto> recommendChannel = getRecommendChannel(condition, pageable);
        Random random = new Random();
        List<SimpleChannelDto> content = recommendChannel.getContent();
        int randValue = random.nextInt(content.size());
        SimpleChannelDto simpleChannelDto = content.get(randValue);
        List<Long> channels = channelRepository.getRelatedChannel(simpleChannelDto.getChannel_index());
        channels.forEach(s->s=s+1);
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setUser_id(condition.getUser_id());
        ComplexMessage message = new ComplexMessage();
        Page<SimpleChannelDto> recommendChannelList = channelRepository.getRecommendChannelList(channels, pageable, userSearchCondition);
        message.setData(recommendChannelList);
        message.setStandardValue(simpleChannelDto.getTitle());
        return message;
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
    public Page<SimpleChannelDto> getRecommendChannel(UserSearchCondition condition, Pageable pageable){
        List<Long> preferIndex = prefferedChannelsRepository.getPrefferedChannelIndex(condition);
        List<Long> recommendIndex = new ArrayList<>();
        List<Integer> data = new ArrayList<Integer>(Collections.nCopies(21928,0));
        if(preferIndex != null){
            for(Long index : preferIndex){
                data.set((int) (index - 1), 1);
            }
        }

        System.out.println(channelRepository.getSimilarChannel(data));

        System.out.println(recommendIndex);
        return channelRepository.getRecommendChannelList(recommendIndex, pageable,condition);
    }

}
