package link.youchu.youchuserver.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import link.youchu.youchuserver.Dto.QSimpleChannelDto;
import link.youchu.youchuserver.Dto.SimpleChannelDto;
import link.youchu.youchuserver.domain.QChannel;
import link.youchu.youchuserver.domain.QDatasetUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static link.youchu.youchuserver.domain.QChannel.*;
import static link.youchu.youchuserver.domain.QDatasetUser.*;

public class DatasetuserRepositoryImpl implements DatasetUserRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    public DatasetuserRepositoryImpl(EntityManager em) {
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

//    @Override
//    public List<Long> getChannel(List<Long> ids) {
//        return jpaQueryFactory.select(datasetUser.channel.id)
//                .from(datasetUser)
//                .where(userIndicesEq(ids))
//                .fetch();
//
//    }

    private BooleanExpression userIndicesEq(List<Long> userIndices){
        return userIndices == null ? null : datasetUser.id.in(userIndices);
    }
}
