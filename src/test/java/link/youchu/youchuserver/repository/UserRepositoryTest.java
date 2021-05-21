package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.UserDto;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import link.youchu.youchuserver.domain.Users;
import link.youchu.youchuserver.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

//    UserService userService;
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    EntityManager em;
//
//    @Test
//    public void basicTest(){
//        UserSearchCondition condition = new UserSearchCondition();
//        condition.setUser_id(1L);
//
//        System.out.println(userRepository.getUserData(condition));
//    }

}