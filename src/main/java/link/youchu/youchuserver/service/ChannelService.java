package link.youchu.youchuserver.service;

import link.youchu.youchuserver.Dto.CategorySearchCondition;
import link.youchu.youchuserver.Dto.ChannelDto;
import link.youchu.youchuserver.Dto.ChannelSearchCondition;
import link.youchu.youchuserver.Dto.KeywordSearchCondition;
import link.youchu.youchuserver.repository.ChannelRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ChannelService {


    private final ChannelRepository channelRepository;

    public ChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Transactional
    public ChannelDto getChannelData(ChannelSearchCondition channelSearchCondition){
        return channelRepository.getChannelData(channelSearchCondition);
    }

    @Transactional
    public List<ChannelDto> getRankingChannelByCategory(CategorySearchCondition condition, Pageable pageable){
        return channelRepository.getChannelByCategory(condition, pageable);
    }

    @Transactional
    public List<ChannelDto> getChannelByOneKeyword(KeywordSearchCondition condition, Pageable pageable) {
        return channelRepository.getChennelByOneKeyword(condition, pageable);
    }

}
