package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.ChannelDto;
import link.youchu.youchuserver.Dto.ChannelSearchCondition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChannelRepositoryImplTest {

    @Autowired
    ChannelRepository channelRepository;

    @Test
    public void testSimple(){
        ChannelSearchCondition condition = new ChannelSearchCondition();
        condition.setChannel_index(1L);
        ChannelDto data = channelRepository.getChannelData(condition);
        System.out.println(data);
    }

}