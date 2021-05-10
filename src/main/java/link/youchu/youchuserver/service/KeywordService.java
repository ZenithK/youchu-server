package link.youchu.youchuserver.service;

import link.youchu.youchuserver.Dto.ChannelSearchCondition;
import link.youchu.youchuserver.Dto.KeywordDto;
import link.youchu.youchuserver.repository.KeywordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeywordService {

    private KeywordRepository keywordRepository;

    public List<KeywordDto> getKeywordData(ChannelSearchCondition condition) {
        return keywordRepository.getKeywordList(condition);
    }
}
