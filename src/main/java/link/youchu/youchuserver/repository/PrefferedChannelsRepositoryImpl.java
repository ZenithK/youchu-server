package link.youchu.youchuserver.repository;

import com.google.gson.Gson;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import link.youchu.youchuserver.Dto.*;
import link.youchu.youchuserver.domain.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static link.youchu.youchuserver.domain.QChannel.channel;
import static link.youchu.youchuserver.domain.QChannelKeyword.*;
import static link.youchu.youchuserver.domain.QPrefferedChannels.*;

@Repository
public class PrefferedChannelsRepositoryImpl implements PrefferedChannelsRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public PrefferedChannelsRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Transactional
    @Override
    public Long deletePreffered(PrefferedPostCondition condition) {
        queryFactory.delete(prefferedChannels)
                .where(userIdEq(condition.getUser_id()),
                        channelIndexEq(condition.getChannel_index()))
                .execute();
        return condition.getUser_id();
    }

    private BooleanExpression userIdEq(Long user_id){
        return user_id == null ? null : prefferedChannels.users.user_id.eq(user_id);
    }

    private BooleanExpression channelIndexEq(Long channel_index){
        return channel_index == null ? null : prefferedChannels.channel.id.eq(channel_index);
    }


    @Override
    public List<ChannelDto> getPrefferedList(UserSearchCondition condition) {
        return queryFactory.select(new QChannelDto(channel.id,channel.title, channel.description, channel.publishedAt,
                channel.thumbnail, channel.viewCount, channel.subScribeCount, channel.bannerImage,
                channel.video_count, channel.channel_id))
                .from(prefferedChannels)
                .join(prefferedChannels.channel, channel)
                .where(userIdEq(condition.getUser_id()))
                .fetch();
    }

    @Override
    public List<ChannelDto> getPreffered(UserSearchCondition condition) {
        List<Long> list = queryFactory.select(channelKeyword.channel.id)
                .from(channelKeyword)
                .join(channelKeyword.channel, channel)
                .where(channelKeyword.keyword.isNull())
                .fetch();
        return queryFactory.select(new QChannelDto(channel.id,channel.title, channel.description, channel.publishedAt,
                channel.thumbnail, channel.viewCount, channel.subScribeCount, channel.bannerImage,
                channel.video_count, channel.channel_id))
                .from(prefferedChannels)
                .join(prefferedChannels.channel, channel)
                .where(userIdEq(condition.getUser_id()),
                        channelIndexNotIn(list))
                .fetch();
    }

    private BooleanExpression channelIndexNotIn(List<Long> list){
        return list == null ? null : prefferedChannels.channel.id.notIn(list);
    }

    @Override
    public Long PrefferedCount(UserSearchCondition condition) {
        QueryResults<PrefferedChannels> results = queryFactory.selectFrom(prefferedChannels)
                .where(userIdEq(condition.getUser_id()),
                        userEmailEq(condition.getUser_email()),
                        googleIdEq(condition.getGoogle_user_id()))
                .fetchResults();


        return results.getTotal();
    }

    @Override
    public List<Long> getPrefferedChannelIndex(UserSearchCondition condition) {
        List<Long> result = queryFactory.select(prefferedChannels.channel.id)
                .from(prefferedChannels)
                .join(prefferedChannels.channel, channel)
                .where(userIdEq(condition.getUser_id()),
                        userGoogleEq(condition.getGoogle_user_id()))
                .fetch();
        return result;
    }

    private BooleanExpression userGoogleEq(String google_user_id){
        return google_user_id == null ? null : prefferedChannels.users.google_user_id.eq(google_user_id);
    }

    private BooleanExpression userEmailEq(String user_email) {
        return user_email == null ? null : prefferedChannels.users.user_email.eq(user_email);
    }

    private BooleanExpression googleIdEq(String google_id){
        return google_id == null ? null : prefferedChannels.users.google_user_id.eq(google_id);
    }

    @Transactional
    @Override
    public void postPreffered(PrefferedPostCondition condition) {
        PrefferedChannels prefferedChannels = new PrefferedChannels(condition.getChannel_index(), condition.getUser_id());
        em.persist(prefferedChannels);
    }

    @Override
    public PrefferedChannels getPrefferedChannel(PrefferedPostCondition condition) {
        return queryFactory.selectFrom(prefferedChannels)
                .where(userIdEq(condition.getUser_id()),
                        channelIndexEq(condition.getChannel_index()))
                .fetchOne();
    }


}
