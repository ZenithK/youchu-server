package link.youchu.youchuserver.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import link.youchu.youchuserver.Dto.ChannelDto;
import link.youchu.youchuserver.Dto.PrefferedPostCondition;
import link.youchu.youchuserver.Dto.QChannelDto;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import link.youchu.youchuserver.domain.DislikeChannels;
import link.youchu.youchuserver.domain.QChannel;
import link.youchu.youchuserver.domain.QDislikeChannels;

import javax.persistence.EntityManager;

import java.util.List;

import static link.youchu.youchuserver.domain.QChannel.*;
import static link.youchu.youchuserver.domain.QDislikeChannels.*;
import static link.youchu.youchuserver.domain.QPrefferedChannels.prefferedChannels;

public class DislikeChannelRepositoryImpl implements DislikeChannelRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public DislikeChannelRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Long deleteDislike(PrefferedPostCondition condition) {
        queryFactory.delete(dislikeChannels)
                    .where(userIdEq(condition.getUser_id()),
                            channelIndexEq(condition.getChannel_index()))
                    .execute();
        return condition.getUser_id();
    }

    private BooleanExpression userIdEq(Long user_id){
        return user_id == null ? null : dislikeChannels.users.user_id.eq(user_id);
    }

    private BooleanExpression channelIndexEq(Long channel_index){
        return channel_index == null ? null : dislikeChannels.channel.id.eq(channel_index);
    }

    @Override
    public List<ChannelDto> getDislike(UserSearchCondition condition) {
        List<ChannelDto> list = queryFactory.select(new QChannelDto(channel.title, channel.description, channel.publishedAt,
                channel.thumbnail, channel.viewCount, channel.subScribeCount, channel.bannerImage,
                channel.video_count, channel.channel_id))
                .from(dislikeChannels)
                .join(dislikeChannels.channel, channel)
                .where(userIdEq(condition.getUser_id()))
                .fetch();

        return list;

    }
}
