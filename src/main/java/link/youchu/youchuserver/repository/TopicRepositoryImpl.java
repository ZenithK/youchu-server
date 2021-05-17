package link.youchu.youchuserver.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import link.youchu.youchuserver.Dto.ChannelSearchCondition;
import link.youchu.youchuserver.Dto.QTopicDto;
import link.youchu.youchuserver.Dto.TopicDto;
import link.youchu.youchuserver.Dto.TopicSearchCondition;
import link.youchu.youchuserver.domain.QChannel;
import link.youchu.youchuserver.domain.QChannelTopic;
import link.youchu.youchuserver.domain.QTopic;

import javax.persistence.EntityManager;
import java.util.List;

import static link.youchu.youchuserver.domain.QChannel.*;
import static link.youchu.youchuserver.domain.QChannelTopic.*;
import static link.youchu.youchuserver.domain.QTopic.*;

public class TopicRepositoryImpl implements TopicRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public TopicRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<TopicDto> getTopicList(ChannelSearchCondition condition) {
        return queryFactory.select(new QTopicDto(channelTopic.topic.topic_name))
                .from(channelTopic)
                .join(channelTopic.topic, topic)
                .where(channelIdEq(condition.getChannel_id()),
                        channelIndexEq(condition.getChannel_index()))
                .fetch();
    }

    private BooleanExpression channelIdEq(String channel_id) {
        return channel_id == null ? null : channelTopic.channel.channel_id.eq(channel_id);
    }

    private BooleanExpression channelIndexEq(Long channel_index) {
        return channel_index == null ? null : channelTopic.channel.id.eq(channel_index);
    }
}
