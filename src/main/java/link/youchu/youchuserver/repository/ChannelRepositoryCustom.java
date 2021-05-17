package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChannelRepositoryCustom {
    ChannelDto getChannelData(ChannelSearchCondition condition);
    Page<SimpleChannelDto> getChannelByTopic(TopicSearchCondition condition, Pageable pageable);
    ChannelDto getRandomChannel();
    Page<ChannelDto> getChennelByOneKeyword(KeywordSearchCondition condition, Pageable pageable);
}
