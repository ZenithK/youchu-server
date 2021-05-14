package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.domain.PrefferedChannels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PrefferedChannelsRepository extends JpaRepository<PrefferedChannels,Long>, PrefferedChannelsRepositoryCustom {

}
