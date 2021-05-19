package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.ChannelDto;
import link.youchu.youchuserver.Dto.SimpleChannelDto;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import link.youchu.youchuserver.domain.PrefferedChannels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrefferedChannelsRepository extends JpaRepository<PrefferedChannels,Long>, PrefferedChannelsRepositoryCustom {

}
