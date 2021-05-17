package link.youchu.youchuserver.controller;

import link.youchu.youchuserver.Dto.UserDto;
import link.youchu.youchuserver.Dto.UserPostCondition;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import link.youchu.youchuserver.Http.Message;
import link.youchu.youchuserver.Http.StatusEnum;
import link.youchu.youchuserver.repository.DislikeChannelRepository;
import link.youchu.youchuserver.repository.PrefferedChannelsRepository;
import link.youchu.youchuserver.repository.UserRepository;
import link.youchu.youchuserver.service.DislikeChannelService;
import link.youchu.youchuserver.service.PrefferedChannelService;
import link.youchu.youchuserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.nio.charset.Charset;

@RestController
@RequiredArgsConstructor
public class UserApiController {


    private final UserService service;
    private final PrefferedChannelService prefferedChannelService;
    private final DislikeChannelService dislikeChannelService;

    @GetMapping("/user")
    public ResponseEntity<Message> getUserData(UserSearchCondition condition){
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(StatusEnum.OK);
            message.setMessage("Success");
            UserDto userData = service.getUserData(condition);
            Long preferred = prefferedChannelService.getCount(condition);
            userData.setPrefer_count(preferred);
            Long dislike = dislikeChannelService.getCount(condition);
            userData.setDislike_count(dislike);

            message.setData(userData);
            return new ResponseEntity<>(message,headers, HttpStatus.OK);

        }catch (Exception e){
            System.out.println(e.getMessage());
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(StatusEnum.BAD_REQUEST);
            message.setMessage("잘못된 요청입니다.");

            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("/sync")
//    public ResponseEntity<Long> synchronizeRecommend(UserSearchCondition condition){
//
//    }
//
    @PostMapping("/register")
    public ResponseEntity<Message> registerUser(UserPostCondition condition){
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(StatusEnum.OK);
            message.setMessage("Success");

            message.setData(null);

            return new ResponseEntity<>(message,headers, HttpStatus.OK);
        }catch (Exception e){
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(StatusEnum.BAD_REQUEST);
            message.setMessage("잘못된 요청입니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }

    }
//
//    @PostMapping("/prefer")
//    public ResponseEntity<Long>  updatePrefer(){}
//
//    @PostMapping("/preffered")
//    public ResponseEntity<Long>  updateDislike(){}
//
//
    @DeleteMapping("/exit")
    public ResponseEntity<Message>  exitUser(UserSearchCondition condition){
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(StatusEnum.OK);
            message.setMessage("Success");
            message.setData(service.exitUser(condition));

            return new ResponseEntity<>(message,headers, HttpStatus.OK);
        }catch (Exception e){
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(StatusEnum.BAD_REQUEST);
            message.setMessage("잘못된 요청입니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }


    }


}
