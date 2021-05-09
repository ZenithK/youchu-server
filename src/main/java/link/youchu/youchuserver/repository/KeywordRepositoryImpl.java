package link.youchu.youchuserver.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import link.youchu.youchuserver.Dto.ChannelSearchCondition;
import link.youchu.youchuserver.Dto.KeywordDto;
import link.youchu.youchuserver.Dto.KeywordSearchCondition;
import link.youchu.youchuserver.Dto.QKeywordDto;
import link.youchu.youchuserver.domain.ChannelKeyword;
import link.youchu.youchuserver.domain.QChannelKeyword;
import link.youchu.youchuserver.domain.QKeyword;

import javax.persistence.EntityManager;
import java.util.List;

import static link.youchu.youchuserver.domain.QChannel.channel;
import static link.youchu.youchuserver.domain.QChannelKeyword.*;
import static link.youchu.youchuserver.domain.QKeyword.*;

public class KeywordRepositoryImpl implements KeywordRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public KeywordRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<KeywordDto> getKeywordList(ChannelSearchCondition condition) {
        return queryFactory
                .select(new QKeywordDto(keyword.keyword_name))
                .from(keyword)
                .join(keyword.channelKeywords, channelKeyword)
                .where(channelIdEq(condition.getChannel_id()),
                        channelIndexEq(condition.getChannel_index()))
                .fetch();
    }

    private BooleanExpression channelIdEq(String channel_id){
        return channel_id == null ? null : channel.channel_id.eq(channel_id);
    }

    private BooleanExpression channelIndexEq(Long channel_index){
        return channel_index == null ? null : channel.id.eq(channel_index);
    }
}
