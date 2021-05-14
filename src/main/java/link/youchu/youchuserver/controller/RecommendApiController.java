package link.youchu.youchuserver.controller;

import link.youchu.youchuserver.Dto.ChannelDto;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import link.youchu.youchuserver.repository.PrefferedChannelsRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendApiController {

    PrefferedChannelsRepository repository;

    @GetMapping("/recommend")
    public List<ChannelDto> getRecommendChannel(UserSearchCondition condition){
        return repository.getRecommendChannel(condition);
    }
}
