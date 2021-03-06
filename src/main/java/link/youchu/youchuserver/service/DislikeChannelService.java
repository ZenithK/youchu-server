package link.youchu.youchuserver.service;

import link.youchu.youchuserver.Dto.ChannelDto;
import link.youchu.youchuserver.Dto.PrefferedPostCondition;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import link.youchu.youchuserver.domain.DislikeChannels;
import link.youchu.youchuserver.repository.DislikeChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DislikeChannelService {

    private final DislikeChannelRepository repository;

    @Transactional
    public void save(PrefferedPostCondition condition){
        repository.postDislike(condition);
    }

    @Transactional
    public Long deleteDislike(PrefferedPostCondition condition){
        return repository.deleteDislike(condition);
    }

    @Transactional
    public List<ChannelDto> getDislikeChannel(UserSearchCondition condition){
        List<ChannelDto> dislike = repository.getDislike(condition);
        for (ChannelDto c : dislike) {
            c.setIsPreferred(2);
        }
        return dislike;
    }

    @Transactional
    public Long getCount(UserSearchCondition condition) {
        return repository.dislikeCount(condition);
    }
}
