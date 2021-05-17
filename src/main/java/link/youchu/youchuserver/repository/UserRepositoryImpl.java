package link.youchu.youchuserver.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import link.youchu.youchuserver.Dto.QUserDto;
import link.youchu.youchuserver.Dto.UserDto;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import link.youchu.youchuserver.domain.QUsers;

import javax.persistence.EntityManager;
import java.util.Optional;

import static link.youchu.youchuserver.domain.QUsers.*;
import static org.springframework.util.ObjectUtils.isEmpty;

public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public UserDto getUserData(UserSearchCondition condition) {
        return queryFactory
                .select(new QUserDto(users.user_id, users.user_email))
                .from(users)
                .where(useridEq(condition.getUser_id()),
                        useremailEq(condition.getUser_email()),
                        googleIdEq(condition.getGoogle_user_id()))
                .fetchOne();
    }

    private BooleanExpression useridEq(Long id){
        return id == null ? null : users.user_id.eq(id);
    }

    private BooleanExpression useremailEq(String email){
        return email == null ? null : users.user_email.eq(email);
    }

    private BooleanExpression googleIdEq(String googleId){
        return googleId == null ? null : users.google_user_id.eq(googleId);
    }

    @Override
    public Long registerUsers() {

        return null;
    }

    @Override
    public Long userPreferChannel() {
        return null;
    }

    @Override
    public Long userDislikeChannel() {
        return null;
    }

    @Override
    public Long exitUser() {
        return null;
    }
}
