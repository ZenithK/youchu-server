package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.UserDto;
import link.youchu.youchuserver.Dto.UserPostCondition;
import link.youchu.youchuserver.Dto.UserSearchCondition;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {
    UserDto getUserData(UserSearchCondition condition);
    List<String> registerUsers(UserPostCondition condition);
    Long userPreferChannel();
    Long userDislikeChannel();
    Long exitUser();
}
