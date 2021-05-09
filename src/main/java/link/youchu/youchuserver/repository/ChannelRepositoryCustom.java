package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.CategorySearchCondition;
import link.youchu.youchuserver.Dto.ChannelDto;
import link.youchu.youchuserver.Dto.ChannelSearchCondition;
import link.youchu.youchuserver.Dto.KeywordSearchCondition;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChannelRepositoryCustom {
    ChannelDto getChannelData(ChannelSearchCondition condition);
    List<ChannelDto> getChannelByCategory(CategorySearchCondition condition, Pageable pageable);

    List<ChannelDto> getChennelByOneKeyword(KeywordSearchCondition condition, Pageable pageable);
}
