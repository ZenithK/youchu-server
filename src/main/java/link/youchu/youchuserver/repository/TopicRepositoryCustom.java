package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.ChannelSearchCondition;
import link.youchu.youchuserver.Dto.TopicDto;
import link.youchu.youchuserver.Dto.TopicSearchCondition;

import java.util.List;

public interface TopicRepositoryCustom {
    List<TopicDto> getTopicList(ChannelSearchCondition condition);
}
