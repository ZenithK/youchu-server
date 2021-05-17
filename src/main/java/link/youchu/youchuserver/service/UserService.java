package link.youchu.youchuserver.service;

import link.youchu.youchuserver.Dto.UserDto;
import link.youchu.youchuserver.Dto.UserPostCondition;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import link.youchu.youchuserver.domain.Channel;
import link.youchu.youchuserver.repository.ChannelRepository;
import link.youchu.youchuserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Transactional
    public UserDto getUserData(UserSearchCondition condition){
        return userRepository.getUserData(condition);
    }

    @Transactional
    public Long exitUser(UserSearchCondition condition){
        userRepository.deleteById(condition.getUser_id());
        return condition.getUser_id();
    }

    @Transactional
    public Long registerUser(UserPostCondition condition) {
        List<Channel> channels = channelRepository.findAll();
        List<Integer> data = new ArrayList<>();

        return userRepository.registerUsers();

    }



}
