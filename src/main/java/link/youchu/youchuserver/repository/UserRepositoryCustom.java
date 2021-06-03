package link.youchu.youchuserver.repository;

import io.jsonwebtoken.ExpiredJwtException;
import link.youchu.youchuserver.Dto.*;
import org.json.simple.parser.ParseException;
import org.springframework.web.client.HttpClientErrorException;

import javax.naming.AuthenticationException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

public interface UserRepositoryCustom {
    Long getUserIndex(UserSearchCondition condition);
    UserDto getUserData(UserSearchCondition condition);
    List<String> registerUsers(UserPostCondition condition) throws AuthenticationException, HttpClientErrorException;
    Long appleUsers(AppleLoginDto appleLoginDto) throws Exception;
    Long getUserToken(TokenUpdateCondition condition) throws AuthenticationException;
    List<String> updateUsers(UserPostCondition condition) throws HttpClientErrorException.Unauthorized, AuthenticationException, ParseException;
    Long userPreferChannel();
    Long userDislikeChannel();
    Long exitUser();
}
