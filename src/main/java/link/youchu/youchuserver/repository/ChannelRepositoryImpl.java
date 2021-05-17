package link.youchu.youchuserver.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import link.youchu.youchuserver.Dto.*;
import link.youchu.youchuserver.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Random;

import static link.youchu.youchuserver.domain.QChannel.*;
import static link.youchu.youchuserver.domain.QChannelKeyword.*;
import static link.youchu.youchuserver.domain.QChannelTopic.*;
import static link.youchu.youchuserver.domain.QKeyword.*;
import static link.youchu.youchuserver.domain.QTopic.*;

public class ChannelRepositoryImpl implements ChannelRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ChannelRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public ChannelDto getChannelData(ChannelSearchCondition condition) {
        return queryFactory
                .select(new QChannelDto(channel.title,channel.description,channel.publishedAt,
                channel.thumbnail,channel.viewCount,channel.subScribeCount,channel.bannerImage,
                        channel.video_count,channel.channel_id))
                .from(channel)
                .where(channelIdEq(condition.getChannel_id()),
                        channelIndexEq(condition.getChannel_index()))
                .fetchOne();
    }

    private BooleanExpression channelIdEq(String channel_id){
        return channel_id == null ? null : channel.channel_id.eq(channel_id);
    }

    private BooleanExpression channelIndexEq(Long channel_index){
        return channel_index == null ? null : channel.id.eq(channel_index);
    }


    @Override
    public Page<SimpleChannelDto> getChannelByTopic(TopicSearchCondition condition, Pageable pageable) {
        QueryResults<SimpleChannelDto> results = queryFactory
                .select(new QSimpleChannelDto(channelTopic.channel.title,channelTopic.channel.thumbnail,channelTopic.channel.subScribeCount,channelTopic.channel.channel_id))
                .from(channelTopic)
                .join(channelTopic.topic, topic)
                .where(topicIdEq(condition.getTopic_index()),
                        topicNameEq(condition.getTopic_name()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(channelTopic.channel.subScribeCount.desc())
                .fetchResults();

        List<SimpleChannelDto> content = results.getResults();
        Long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression topicIdEq(Long topic_id){
        return topic_id == null ? null : channelTopic.topic.id.eq(topic_id);
    }

    private BooleanExpression topicNameEq(String topic_name) {
        return topic_name == null ? null : channelTopic.topic.topic_name.eq(topic_name);
    }
    private BooleanExpression keywordIdEq(Long keyword_id) {
        return keyword_id == null ? null : channelKeyword.keyword.id.eq(keyword_id);
    }

    @Override
    public Page<ChannelDto> getChennelByOneKeyword(KeywordSearchCondition condition, Pageable pageable) {
        QueryResults<ChannelDto> results = queryFactory
                .select(new QChannelDto(channel.title, channel.description, channel.publishedAt,
                        channel.thumbnail, channel.viewCount, channel.subScribeCount, channel.bannerImage,
                        channel.video_count, channel.channel_id))
                .from(channel)
                .join(channel.channelKeywords, channelKeyword)
                .where(keywordIdEq(condition.getKeyword_id()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<ChannelDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public ChannelDto getRandomChannel() {
        List<ChannelDto> list = queryFactory
                .select(new QChannelDto(channel.title, channel.description, channel.publishedAt,
                        channel.thumbnail, channel.viewCount, channel.subScribeCount, channel.bannerImage,
                        channel.video_count, channel.channel_id))
                .from(channel)
                .fetch();
        Random random = new Random();
        int randValue = random.nextInt(list.size());
        return list.get(randValue);

    }

    @Override
    public SimpleChannelDto getRecommnedChannel(Long index) {
        return queryFactory.select(new QSimpleChannelDto(channel.title, channel.thumbnail, channel.subScribeCount, channel.channel_id))
                .from(channel)
                .where(channelIndexEq(index))
                .fetchOne();


    }
}
