package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.ChannelDto;
import link.youchu.youchuserver.Dto.PrefferedPostCondition;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrefferedChannelsRepositoryCustom {
    List<ChannelDto> getRecommendChannel(UserSearchCondition condition);
    Long postPreffered(PrefferedPostCondition condition);
    Long deletePreffered(PrefferedPostCondition condition);

    List<ChannelDto> getPrefferedList(UserSearchCondition condition);
}
