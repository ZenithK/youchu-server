package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.*;
import link.youchu.youchuserver.Dto.VideoDto;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.InvalidKeyException;
import java.util.List;

public interface ChannelRepositoryCustom {
    ChannelDto getChannelData(ChannelSearchCondition condition);
    Page<SimpleDtoPlusBanner> getChannelByTopic(TopicSearchCondition condition, Pageable pageable);
    Page<SimpleDtoPlusBanner> getChannelByKeyword(KeywordSearchCondition condition, Pageable pageable);
    ChannelDto getRandomChannel(UserSearchCondition condition);
    Page<SimpleChannelDto> getChannelByOneKeyword(KeywordSearchCondition condition, Pageable pageable);
    Page<SimpleChannelDto> getRecommendChannelList(List<Long> channel_indices,Pageable pageable,UserSearchCondition condition);
    Long getChannelIndex(ChannelSearchCondition condition);
    List<Long> getSimilarChannel(List<Integer> data);
    SimpleChannelDto getRecommnedChannel(Long index);
    List<VideoDto> getChannelVideo(String channel_id) throws ParseException, InvalidKeyException;
    List<Long> getRelatedChannel(Long channel_index);
}
