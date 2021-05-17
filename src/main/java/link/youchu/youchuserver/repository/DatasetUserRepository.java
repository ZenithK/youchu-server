package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.domain.DatasetUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DatasetUserRepository extends JpaRepository<DatasetUser, Long>,DatasetUserRepositoryCustom {
}
