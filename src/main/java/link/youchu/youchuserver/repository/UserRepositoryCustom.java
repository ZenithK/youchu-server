package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.UserDto;
import link.youchu.youchuserver.Dto.UserSearchCondition;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {
    UserDto findUsers(UserSearchCondition condition);
    Long registerUsers();
    Long userPreferChannel();
    Long userDislikeChannel();
    Long exitUser();
}
