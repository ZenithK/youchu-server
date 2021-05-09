package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword,Long>,KeywordRepositoryCustom {
}
