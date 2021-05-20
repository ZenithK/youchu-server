package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChannelRepositoryCustom {
    ChannelDto getChannelData(ChannelSearchCondition condition);
    Page<SimpleDtoPlusBanner> getChannelByTopic(TopicSearchCondition condition, Pageable pageable);

    Page<SimpleDtoPlusBanner> getChannelByKeyword(KeywordSearchCondition condition, Pageable pageable);
    ChannelDto getRandomChannel(UserSearchCondition condition);
    Page<SimpleChannelDto> getChannelByOneKeyword(KeywordSearchCondition condition, Pageable pageable);
    Page<SimpleChannelDto> getRecommendChannelList(List<Long> channel_indices,Pageable pageable,UserSearchCondition condition);
    Long getChannelIndex(ChannelSearchCondition condition);
    List<Long> getSimilarUser(List<Integer> data);
    SimpleChannelDto getRecommnedChannel(Long index);
}
