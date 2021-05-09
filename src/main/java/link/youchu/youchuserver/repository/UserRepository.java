package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.UserDto;
import link.youchu.youchuserver.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Long>, UserRepositoryCustom{

}
