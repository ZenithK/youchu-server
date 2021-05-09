package link.youchu.youchuserver.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import link.youchu.youchuserver.Dto.*;
import link.youchu.youchuserver.domain.*;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;

import static link.youchu.youchuserver.domain.QCategory.*;
import static link.youchu.youchuserver.domain.QChannel.*;
import static link.youchu.youchuserver.domain.QChannelCategory.*;
import static link.youchu.youchuserver.domain.QChannelKeyword.*;
import static link.youchu.youchuserver.domain.QKeyword.*;

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
    public List<ChannelDto> getChannelByCategory(CategorySearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(new QChannelDto(channel.title,channel.description,channel.publishedAt,
                        channel.thumbnail,channel.viewCount,channel.subScribeCount,channel.bannerImage,
                        channel.video_count,channel.channel_id))
                .from(channel)
                .join(channel.channelCategories, channelCategory)
                .where(categoryIdEq(condition.getCategory_id()),
                        categoryNameEq(condition.getCategory_name()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(channel.subScribeCount.desc())
                .fetch();
    }

    private BooleanExpression categoryIdEq(Long category_id){
        return category_id == null ? null : channelCategory.id.eq(category_id);
    }

    private BooleanExpression categoryNameEq(String category_name){
        return category_name == null ? null : channelCategory.category.category_name.eq(category_name);
    }

    private BooleanExpression keywordIdEq(Long keyword_id) {
        return keyword_id == null ? null : keyword.id.eq(keyword_id);
    }

    @Override
    public List<ChannelDto> getChennelByOneKeyword(KeywordSearchCondition condition, Pageable pageable) {
        return queryFactory
                .select(new QChannelDto(channel.title,channel.description,channel.publishedAt,
                        channel.thumbnail,channel.viewCount,channel.subScribeCount,channel.bannerImage,
                        channel.video_count,channel.channel_id))
                .from(channel)
                .join(channel.channelKeywords, channelKeyword)
                .where(keywordIdEq(condition.getKeyword_id()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
