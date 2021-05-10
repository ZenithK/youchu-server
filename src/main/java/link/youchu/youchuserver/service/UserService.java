package link.youchu.youchuserver.service;

import link.youchu.youchuserver.Dto.UserDto;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import link.youchu.youchuserver.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserDto getUserData(UserSearchCondition condition){
        return userRepository.getUserData(condition);
    }

    @Transactional
    public Long exitUser(UserSearchCondition condition){
        userRepository.deleteById(condition.getUser_id());
        return condition.getUser_id();
    }

}
