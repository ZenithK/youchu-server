package link.youchu.youchuserver.controller;

//import link.youchu.youchuserver.Config.AuthenticationToken;
import link.youchu.youchuserver.Config.JwtAuthenticationTokenProvider;
import link.youchu.youchuserver.Dto.TokenUpdateCondition;
import link.youchu.youchuserver.Dto.UserDto;
import link.youchu.youchuserver.Dto.UserPostCondition;
import link.youchu.youchuserver.Dto.UserSearchCondition;
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
import java.nio.charset.Charset;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService service;
    private final JwtAuthenticationTokenProvider provider;

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
            Long preferred = service.getPreferCount(condition);
            userData.setPrefer_count(preferred);
            Long dislike = service.getDislikeCount(condition);
            userData.setDislike_count(dislike);
            message.setData(userData);
            return new ResponseEntity<>(message,headers, HttpStatus.OK);

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
                return new ResponseEntity<>(message,headers, HttpStatus.OK);
            }else{
                aLong = service.registerUser(condition);
                message.setData(provider.issue(aLong).getToken());
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
    public ResponseEntity<Message>  exitUser(UserSearchCondition condition){
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
