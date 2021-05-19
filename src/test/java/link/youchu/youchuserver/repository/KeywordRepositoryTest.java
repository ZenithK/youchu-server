package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.ChannelSearchCondition;
import link.youchu.youchuserver.service.KeywordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class KeywordRepositoryTest {

    private KeywordService keywordService;

    @Autowired
    private KeywordRepository keywordRepository;

    @Test
    public void simpleTest(){

    }
}