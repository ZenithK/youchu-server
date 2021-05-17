package link.youchu.youchuserver.service;

import link.youchu.youchuserver.Dto.*;
import link.youchu.youchuserver.domain.Channel;
import link.youchu.youchuserver.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {


    private final ChannelRepository channelRepository;

    @Transactional
    public ChannelDto getChannelData(ChannelSearchCondition channelSearchCondition){
        return channelRepository.getChannelData(channelSearchCondition);
    }

    @Transactional
    public Page<SimpleChannelDto> getRankingChannelByTopic(TopicSearchCondition condition, Pageable pageable){
        return channelRepository.getChannelByTopic(condition, pageable);
    }

    @Transactional
    public Page<ChannelDto> getChannelByOneKeyword(KeywordSearchCondition condition, Pageable pageable) {
        return channelRepository.getChennelByOneKeyword(condition, pageable);
    }

    @Transactional
    public List<Long> getVerfied(){
        List<Long> longs = new ArrayList<>();
        List<Channel> list = channelRepository.findAll();
        for(int i=0; i<21928;i++){
            try{
                if (list.get(i).getId() != i+1) {
                    longs.add(new Long(i));
                }
            }
            catch (Exception e){
                continue;
            }
        }
        return longs;
    }

}
