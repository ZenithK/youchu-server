package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel,Long>, ChannelRepositoryCustom {

}
