package link.youchu.youchuserver.controller;

//import link.youchu.youchuserver.Config.AuthenticationToken;
import io.jsonwebtoken.ExpiredJwtException;
import link.youchu.youchuserver.Config.JwtAuthenticationTokenProvider;
import link.youchu.youchuserver.Dto.*;
import link.youchu.youchuserver.Http.*;
import link.youchu.youchuserver.domain.Users;
import link.youchu.youchuserver.repository.DislikeChannelRepository;
import link.youchu.youchuserver.repository.PrefferedChannelsRepository;
import link.youchu.youchuserver.repository.UserRepository;
import link.youchu.youchuserver.service.DislikeChannelService;
import link.youchu.youchuserver.service.PrefferedChannelService;
import link.youchu.youchuserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.transaction.Transactional;
import java.lang.reflect.Parameter;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService service;
    private final JwtAuthenticationTokenProvider provider;

    @PostMapping("/Apple/Register")
    public ResponseEntity<ParameterMessage> appleLogin(@RequestBody final AppleLoginDto appleLoginDto){
        try{
            ParameterMessage message = new ParameterMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(200L);
            message.setMessage("Success");
            Long aLong = null;
            Long user_id = null;
            UserSearchCondition userSearchCondition = new UserSearchCondition();
            userSearchCondition.setGoogle_user_id(appleLoginDto.getApple_user_id());
            UserDto userData = service.getUserData(userSearchCondition);
            if(userData != null){
                message.setExist(true);
                user_id= userData.getUser_id();
                message.setData(user_id);
                message.setToken(provider.issue(user_id).getToken());
                return new ResponseEntity<>(message,headers, HttpStatus.OK);
            }else{
                aLong = service.appleLogin(appleLoginDto);
                message.setData(aLong);
                message.setToken(provider.issue(aLong).getToken());
                return new ResponseEntity<>(message,headers, HttpStatus.OK);
            }

        } catch(ExpiredJwtException e){
            ParameterMessage message = new ParameterMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(401L);
            message.setMessage("애플 로그인 토큰이 만료되었습니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            ParameterMessage message = new ParameterMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(400L);
            message.setMessage("잘못된 로그인 토큰 입니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/userToken")
    public ResponseEntity<Message> getUserToken(TokenUpdateCondition condition) {
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(200L);
            message.setMessage("Success");
            message.setData(provider.issue(service.getUserToken(condition)).getToken());
            return new ResponseEntity<>(message,headers, HttpStatus.OK);

        }catch(AuthenticationException e){
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(401L);
            message.setMessage("재인증 실패!");
            return new ResponseEntity<>(message,headers, HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(400L);
            message.setMessage("잘못된 요청입니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/userIndex")
    public ResponseEntity<UserInfoMessage> getUserIndex(UserSearchCondition condition) {
        try{
            UserInfoMessage message = new UserInfoMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(200L);
            message.setMessage("Success");
            Long index = service.getUserIndex(condition);
            message.setData(index);
            message.setToken(provider.issue(index).getToken());
            return new ResponseEntity<>(message,headers, HttpStatus.OK);

        }catch (Exception e){
            System.out.println(e.getMessage());
            UserInfoMessage message = new UserInfoMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(400L);
            message.setMessage("잘못된 요청입니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<Message> getUserData(UserSearchCondition condition){
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(200L);
            message.setMessage("Success");
            UserDto userData = service.getUserData(condition);
            if(userData.getUser_email().contains("gmail.com")){
                userData.setDomain("Google");
            }else{
                userData.setDomain("Apple");
            }
            Long preferred = service.getPreferCount(condition);
            userData.setPrefer_count(preferred);
            Long dislike = service.getDislikeCount(condition);
            userData.setDislike_count(dislike);

            message.setData(userData);
            return new ResponseEntity<>(message,headers, HttpStatus.OK);

        }catch(ExpiredJwtException e){
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(403L);
            message.setMessage("토큰이 만료되었습니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            System.out.println(e.getMessage());
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(400L);
            message.setMessage("잘못된 요청입니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ParameterMessage> registerUser(@RequestBody final UserPostCondition condition){
        try{
            ParameterMessage message = new ParameterMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(200L);
            message.setMessage("Success");
            Long aLong = null;
            Long user_id = null;
            UserSearchCondition userSearchCondition = new UserSearchCondition();
            userSearchCondition.setGoogle_user_id(condition.getGoogle_user_id());
            UserDto userData = service.getUserData(userSearchCondition);
            if(userData != null){
                user_id = userData.getUser_id();
                message.setExist(true);
                condition.setUser_id(user_id);
                aLong = service.UpdateUser(condition);
                message.setData(user_id);
                message.setToken(provider.issue(aLong).getToken());
                return new ResponseEntity<>(message,headers, HttpStatus.OK);
            }else{
                aLong = service.registerUser(condition);
                message.setData(aLong);
                message.setToken(provider.issue(aLong).getToken());
                return new ResponseEntity<>(message,headers, HttpStatus.OK);
            }
        }catch(AuthenticationException e) {
            ParameterMessage message = new ParameterMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(401L);
            message.setMessage("로그인 실패!");
            return new ResponseEntity<>(message,headers, HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            ParameterMessage message = new ParameterMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(400L);
            message.setMessage("잘못된 요청입니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/exit")
    public ResponseEntity<Message> exitUser(UserSearchCondition condition){
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(200L);
            message.setMessage("Success");
            message.setData(service.exitUser(condition));
            return new ResponseEntity<>(message,headers, HttpStatus.OK);
        }catch (Exception e){
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(400L);
            message.setMessage("잘못된 요청입니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }


    }


}
