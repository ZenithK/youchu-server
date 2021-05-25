package link.youchu.youchuserver.service;

import link.youchu.youchuserver.Dto.*;
import link.youchu.youchuserver.domain.SecurityUser;
import link.youchu.youchuserver.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final PrefferedChannelsRepository prefferedChannelsRepository;
    private final DatasetUserRepository datasetRepository;
    private final DislikeChannelRepository dislikeChannelRepository;

    @Transactional
    public Long getUserToken(TokenUpdateCondition condition) throws AuthenticationException {
        return userRepository.getUserToken(condition);
    }

    @Transactional
    public String getUserById(Long user_id) {
        return userRepository.getUsersByUser_id(user_id);
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

    @Transactional
    public Long getUserIndex(UserSearchCondition condition) {
        return userRepository.getUserIndex(condition);
    }

    @Transactional
    public Long registerUser(UserPostCondition condition) throws AuthenticationException {
        List<String> channelIds = userRepository.registerUsers(condition);
        if(channelIds == null){
            UserSearchCondition userSearchCondition = new UserSearchCondition();
            userSearchCondition.setGoogle_user_id(condition.getGoogle_user_id());
            Long user_id = userRepository.getUserData(userSearchCondition).getUser_id();
            return user_id;
        }
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setGoogle_user_id(condition.getGoogle_user_id());
        Long user_id = userRepository.getUserData(userSearchCondition).getUser_id();
        for (String c : channelIds) {
            PrefferedPostCondition prefferedPostCondition = new PrefferedPostCondition();
            prefferedPostCondition.setUser_id(user_id);
            ChannelSearchCondition channelSearchCondition = new ChannelSearchCondition();
            channelSearchCondition.setChannel_id(c);
            try{
                prefferedPostCondition.setChannel_index(channelRepository.getChannelIndex(channelSearchCondition));
                if (prefferedPostCondition.getChannel_index() != null) {
                    prefferedChannelsRepository.postPreffered(prefferedPostCondition);
                }
            }catch (Exception e){
                System.out.println(e);
            }
        }

        return user_id;
    }

    @Transactional
    public Long UpdateUser(UserPostCondition condition) throws AuthenticationException {
        List<String> channelIds = userRepository.updateUsers(condition);
        if(channelIds == null){
            Long user_id = condition.getUser_id();
            return user_id;
        }
        Long user_id = condition.getUser_id();
        for (String c : channelIds) {
            PrefferedPostCondition prefferedPostCondition = new PrefferedPostCondition();
            prefferedPostCondition.setUser_id(user_id);
            ChannelSearchCondition channelSearchCondition = new ChannelSearchCondition();
            channelSearchCondition.setChannel_id(c);
            try{
                prefferedPostCondition.setChannel_index(channelRepository.getChannelIndex(channelSearchCondition));
                if (prefferedPostCondition.getChannel_index() != null) {
                    if(prefferedChannelsRepository.getPrefferedChannel(prefferedPostCondition) == null){
                        prefferedChannelsRepository.postPreffered(prefferedPostCondition);
                    }
                }
            }catch (Exception e){
                System.out.println(e);
            }
        }
        return user_id;
    }

    @Transactional
    public Long getPreferCount(UserSearchCondition condition){
        return prefferedChannelsRepository.PrefferedCount(condition);
    }

    @Transactional
    public Long getDislikeCount(UserSearchCondition condition) {
        return dislikeChannelRepository.dislikeCount(condition);
    }


}
