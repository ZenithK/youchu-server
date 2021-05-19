package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.domain.DatasetUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DatasetUserRepository extends JpaRepository<DatasetUser, Long>,DatasetUserRepositoryCustom {
    @Query("select d.channel.id from DatasetUser d where d.id=:id")
    List<Long> getChannelIndexById(@Param("id") Long id);

    @Query("select d.channel.id from DatasetUser d where d.id in :indices")
    List<Long> getChannelIndexByIndices(@Param("indices") List<Long> indices);
}
