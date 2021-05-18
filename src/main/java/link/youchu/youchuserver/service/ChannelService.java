package link.youchu.youchuserver.service;

import link.youchu.youchuserver.Dto.*;
import link.youchu.youchuserver.domain.Channel;
import link.youchu.youchuserver.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
    public Page<SimpleChannelDto> getRankingChannelByTopic(TopicSearchCondition condition, Pageable pageable){
        return channelRepository.getChannelByTopic(condition, pageable);
    }

    @Transactional
    public Page<SimpleChannelDto> getSimilarTopic(UserSearchCondition condition,Pageable pageable) {
        List<ChannelDto> list = prefferedChannelsRepository.getPrefferedList(condition);
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
        Page<SimpleChannelDto> channelByTopic = channelRepository.getChannelByTopic(topicSearchCondition, pageable);

        return channelByTopic;
    }

    @Transactional
    public ChannelDto getRandomChannel() {
        return channelRepository.getRandomChannel();
    }

    @Transactional
    public Page<SimpleChannelDto> getSimilarKeyword(UserSearchCondition condition, Pageable pageable) {
        List<ChannelDto> list = prefferedChannelsRepository.getPrefferedList(condition);
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
        return channelRepository.getChennelByOneKeyword(keywordSearchCondition,pageable);
    }

    @Transactional
    public Page<SimpleChannelDto> getChannelByOneKeyword(KeywordSearchCondition condition, Pageable pageable) {
        return channelRepository.getChennelByOneKeyword(condition, pageable);
    }

    @Transactional
    public Page<SimpleChannelDto> getRecommendChannel(UserSearchCondition condition, Pageable pageable){
        List<ChannelDto> prefferedList = prefferedChannelsRepository.getPrefferedList(condition);
        List<Channel> channels = channelRepository.findAll();
        List<Integer> data = new ArrayList<>();
        for(Channel c : channels){
            boolean flag = false;
            for(ChannelDto cd : prefferedList){
                if(c.getChannel_id().equals(cd.getChannel_id())){
                    flag= true;
                    break;
                }
            }
            if(flag){
                data.add(1);
            }else{
                data.add(0);
            }
        }
        for(int i=0; i<46;i++){
            data.add(0);
        }
        List<Long> similarUsers = channelRepository.getSimilarUser(data);
        List<Long> channelIndexs = new ArrayList<>();
        for(Long index : similarUsers){
            List<Long> channelIndexById = datasetUserRepository.getChannelIndexById(index + 1);
            for(Long channel_index : channelIndexById){
                channelIndexs.add(channel_index);
            }
        }
        channelIndexs = channelIndexs.stream().distinct().collect(Collectors.toList());
        return channelRepository.getRecommendChannelList(channelIndexs,pageable);
    }

}
