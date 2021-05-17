package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.domain.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic,Long>,TopicRepositoryCustom {
}
