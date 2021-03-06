package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.ChannelDto;
import link.youchu.youchuserver.Dto.PrefferedPostCondition;
import link.youchu.youchuserver.Dto.UserSearchCondition;

import java.util.List;

public interface DislikeChannelRepositoryCustom {
    Long deleteDislike(PrefferedPostCondition condition);

    List<Long> getDislikeIndex(UserSearchCondition condition);
    void postDislike(PrefferedPostCondition condition);
    Long dislikeCount(UserSearchCondition condition);
    List<ChannelDto> getDislike(UserSearchCondition condition);
}
