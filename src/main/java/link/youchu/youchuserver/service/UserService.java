package link.youchu.youchuserver.service;

import link.youchu.youchuserver.Dto.*;
import link.youchu.youchuserver.domain.Channel;
import link.youchu.youchuserver.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final PrefferedChannelsRepository prefferedChannelsRepository;
    private final DatasetUserRepository datasetRepository;
    private final DislikeChannelRepository dislikeChannelRepository;

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
        List<String> channelIds = userRepository.registerUsers(condition);
        if(channelIds == null){
            return null;
        }
        UserSearchCondition userSearchCondition = new UserSearchCondition();
        userSearchCondition.setGoogle_user_id(condition.getGoogle_user_id());
        Long user_id = userRepository.getUserData(userSearchCondition).getUser_id();
        System.out.println(user_id);
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
    public Long getPreferCount(UserSearchCondition condition){
        return prefferedChannelsRepository.PrefferedCount(condition);
    }

    @Transactional
    public Long getDislikeCount(UserSearchCondition condition) {
        return dislikeChannelRepository.dislikeCount(condition);
    }



}
