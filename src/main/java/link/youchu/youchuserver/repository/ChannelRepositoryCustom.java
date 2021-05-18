package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChannelRepositoryCustom {
    ChannelDto getChannelData(ChannelSearchCondition condition);
    Page<SimpleChannelDto> getChannelByTopic(TopicSearchCondition condition, Pageable pageable);
    ChannelDto getRandomChannel();
    Page<SimpleChannelDto> getChennelByOneKeyword(KeywordSearchCondition condition, Pageable pageable);
    Page<SimpleChannelDto> getRecommendChannelList(List<Long> channel_indices,Pageable pageable);
    Long getChannelIndex(ChannelSearchCondition condition);
    List<Long> getSimilarUser(List<Integer> data);
    SimpleChannelDto getRecommnedChannel(Long index);
}
