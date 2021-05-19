package link.youchu.youchuserver.service;

import link.youchu.youchuserver.Dto.*;
import link.youchu.youchuserver.Http.ComplexMessage;
import link.youchu.youchuserver.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public Page<SimpleChannelDto> getRankingChannelByTopic(TopicSearchCondition condition, Pageable pageable){
        return channelRepository.getChannelByTopic(condition, pageable);
    }

    @Transactional
    public ComplexMessage getSimilarTopic(UserSearchCondition condition,Pageable pageable) {
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
        Page<SimpleChannelDto> keyword = channelRepository.getChannelByKeyword(keywordSearchCondition, pageable);

        ComplexMessage message = new ComplexMessage();
        message.setStandardValue(randomChannel.getTitle());
        message.setData(keyword);
        return message;
    }

    @Transactional
    public Page<SimpleChannelDto> getChannelByOneKeyword(KeywordSearchCondition condition, Pageable pageable) {
        return channelRepository.getChannelByOneKeyword(condition, pageable);
    }

    @Transactional
    public Page<SimpleChannelDto> getRecommendChannel(UserSearchCondition condition, Pageable pageable){
        List<Long> preferIndex = prefferedChannelsRepository.getPrefferedChannelIndex(condition);
        List<Integer> data = new ArrayList<Integer>(Collections.nCopies(21974,0));
        for(Long index : preferIndex){
            data.set((int) (index - 1), 1);
        }
        List<Long> similarUsers = channelRepository.getSimilarUser(data);
        similarUsers.forEach(s->s=s+1);
        List<Long> index = datasetUserRepository.getChannelIndexByIndices(similarUsers);
        return channelRepository.getRecommendChannelList(index, pageable);
    }

}
