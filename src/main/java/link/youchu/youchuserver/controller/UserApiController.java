package link.youchu.youchuserver.controller;

import link.youchu.youchuserver.Dto.UserDto;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import link.youchu.youchuserver.Http.Message;
import link.youchu.youchuserver.Http.StatusEnum;
import link.youchu.youchuserver.repository.UserRepository;
import link.youchu.youchuserver.service.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;

@RestController
@RequiredArgsConstructor
public class UserApiController {


    private final UserRepository userRepository;

    private final UserService userService;


    @GetMapping("/user")
    public ResponseEntity<Message> getUserData(UserSearchCondition condition){
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(StatusEnum.OK);
            message.setMessage("Success");
            message.setData(userRepository.getUserData(condition));

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
//    @PostMapping("/register")
//    public ResponseEntity<Long> registerUser(){
//
//    }
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
            userRepository.deleteById(condition.getUser_id());
            message.setData(condition.getUser_id());

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