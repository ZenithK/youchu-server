package link.youchu.youchuserver.service;

import link.youchu.youchuserver.Dto.ChannelSearchCondition;
import link.youchu.youchuserver.Dto.TopicDto;
import link.youchu.youchuserver.Dto.TopicSearchCondition;
import link.youchu.youchuserver.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TopicRepository repository;

    @Transactional
    public List<TopicDto> getTopicList(ChannelSearchCondition condition) {
        return repository.getTopicList(condition);
    }
}
