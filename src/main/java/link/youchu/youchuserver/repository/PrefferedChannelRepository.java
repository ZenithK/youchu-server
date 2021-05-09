package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.domain.PrefferedChannels;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrefferedChannelRepository extends JpaRepository<PrefferedChannels,Long> {
}
