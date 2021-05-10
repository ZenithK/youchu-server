package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.CategorySearchCondition;
import link.youchu.youchuserver.Dto.ChannelDto;
import link.youchu.youchuserver.Dto.ChannelSearchCondition;
import link.youchu.youchuserver.Dto.KeywordSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChannelRepositoryCustom {
    ChannelDto getChannelData(ChannelSearchCondition condition);
    Page<ChannelDto> getChannelByCategory(CategorySearchCondition condition, Pageable pageable);

    Page<ChannelDto> getChennelByOneKeyword(KeywordSearchCondition condition, Pageable pageable);
}
