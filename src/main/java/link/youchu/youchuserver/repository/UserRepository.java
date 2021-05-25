package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.UserDto;
import link.youchu.youchuserver.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Long>, UserRepositoryCustom{
    @Query("select u.user_email from Users u where u.user_email = :user_email")
    String getUsersByUser_email(@Param("user_email") String user_email);

    @Query("select u.user_email from Users u where u.user_id = :user_id")
    String getUsersByUser_id(@Param("user_id") Long user_id);
}
