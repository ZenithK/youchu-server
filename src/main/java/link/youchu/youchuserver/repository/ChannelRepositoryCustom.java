package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChannelRepositoryCustom {
    ChannelDto getChannelData(ChannelSearchCondition condition);
    Page<SimpleChannelDto> getChannelByTopic(TopicSearchCondition condition, Pageable pageable);
    ChannelDto getRandomChannel();
    Page<ChannelDto> getChennelByOneKeyword(KeywordSearchCondition condition, Pageable pageable);

    SimpleChannelDto getRecommnedChannel(Long index);
}
