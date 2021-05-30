package link.youchu.youchuserver.repository;

import link.youchu.youchuserver.Dto.TokenUpdateCondition;
import link.youchu.youchuserver.Dto.UserDto;
import link.youchu.youchuserver.Dto.UserPostCondition;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import org.json.simple.parser.ParseException;
import org.springframework.web.client.HttpClientErrorException;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {
    Long getUserIndex(UserSearchCondition condition);
    UserDto getUserData(UserSearchCondition condition);
    List<String> registerUsers(UserPostCondition condition) throws AuthenticationException, HttpClientErrorException;

    Long getUserToken(TokenUpdateCondition condition) throws AuthenticationException;
    List<String> updateUsers(UserPostCondition condition) throws HttpClientErrorException.Unauthorized, AuthenticationException, ParseException;
    Long userPreferChannel();
    Long userDislikeChannel();
    Long exitUser();
}
