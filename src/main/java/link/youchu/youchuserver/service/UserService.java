package link.youchu.youchuserver.service;

import link.youchu.youchuserver.Dto.SimpleChannelDto;
import link.youchu.youchuserver.Dto.UserDto;
import link.youchu.youchuserver.Dto.UserPostCondition;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import link.youchu.youchuserver.domain.Channel;
import link.youchu.youchuserver.repository.*;
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
    public List<SimpleChannelDto> registerUser(UserPostCondition condition) {
        List<String> channelIds = userRepository.registerUsers(condition);
        List<Channel> channels = channelRepository.findAll();
        List<Integer> data = new ArrayList<>();
        for(Channel c : channels){
            boolean flag = false;
            for(String id : channelIds){
                if(c.getChannel_id() == id){
                    flag= true;
                    break;
                }
            }
            if(flag){
                data.add(1);
            }else{
                data.add(0);
            }
        }
        System.out.println(1);
        List<Long> user_indexs = prefferedChannelsRepository.getRecommendChannel(data);
        System.out.println(2);
        List<Long> channelIndexs = new ArrayList<>();
        for (Long user_index : user_indexs) {
            datasetRepository.getChannelIndexById(user_index);
        }
        System.out.println(3);
        List<SimpleChannelDto> channelDtos = new ArrayList<>();
        for (Long index : channelIndexs) {
            channelDtos.add(channelRepository.getRecommnedChannel(index + 1));
        }
        return channelDtos;
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
