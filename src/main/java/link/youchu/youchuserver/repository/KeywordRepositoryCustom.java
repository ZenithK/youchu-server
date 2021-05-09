package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.ChannelSearchCondition;
import link.youchu.youchuserver.Dto.KeywordDto;
import link.youchu.youchuserver.Dto.KeywordSearchCondition;

import java.util.List;

public interface KeywordRepositoryCustom {
    List<KeywordDto> getKeywordList(ChannelSearchCondition condition);
}
