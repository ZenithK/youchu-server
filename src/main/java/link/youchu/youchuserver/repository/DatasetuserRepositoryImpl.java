package link.youchu.youchuserver.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import link.youchu.youchuserver.domain.QChannel;
import link.youchu.youchuserver.domain.QDatasetUser;

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
//    public List<Long> getChannelIndexById(Long id) {
//        List<Long> list = jpaQueryFactory.select(channel.id)
//                .from(datasetUser)
//                .where(IdEq(id))
//                .fetch();
//        if (list == null) {
//            System.out.println("Error");
//
//        }
//        return list;
//    }

    private BooleanExpression IdEq(Long user_id){
        return user_id == null ? null : datasetUser.id.eq(user_id);
    }
}
