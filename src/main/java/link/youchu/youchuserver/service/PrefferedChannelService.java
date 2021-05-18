package link.youchu.youchuserver.service;

import link.youchu.youchuserver.Dto.ChannelDto;
import link.youchu.youchuserver.Dto.PrefferedPostCondition;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import link.youchu.youchuserver.domain.PrefferedChannels;
import link.youchu.youchuserver.repository.PrefferedChannelsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrefferedChannelService {

    private final PrefferedChannelsRepository repository;

    @Transactional
    public Long deletePreffered(PrefferedPostCondition condition){
        return repository.deletePreffered(condition);
    }

    @Transactional
    public List<ChannelDto> getPrefferedList(UserSearchCondition condition){
        return repository.getPrefferedList(condition);
    }

    @Transactional
    public List<PrefferedChannels> findAll(){
        return repository.findAll();
    }

    @Transactional
    public void save(PrefferedChannels prefferedChannels){
        repository.save(prefferedChannels);
    }

    @Transactional
    public Long getCount(UserSearchCondition condition){
        return repository.PrefferedCount(condition);
    }
}
