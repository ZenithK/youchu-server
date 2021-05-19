package link.youchu.youchuserver.controller;

import link.youchu.youchuserver.Dto.PrefferedPostCondition;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import link.youchu.youchuserver.Http.Message;
import link.youchu.youchuserver.Http.StatusEnum;
import link.youchu.youchuserver.domain.DislikeChannels;
import link.youchu.youchuserver.domain.PrefferedChannels;
import link.youchu.youchuserver.repository.DislikeChannelRepository;
import link.youchu.youchuserver.service.DislikeChannelService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;

@RestController
@RequiredArgsConstructor
public class DislikeChannelApiController {

    private final DislikeChannelService service;

    @GetMapping("/getDislike")
    public ResponseEntity<Message> getPrefferedChannel(UserSearchCondition condition) {
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(200L);
            message.setMessage("Success");
            message.setData(service.getDislikeChannel(condition));
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

    @PostMapping("/dislike")
    public ResponseEntity<Message> postDislike(@RequestBody final PrefferedPostCondition condition){
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(200L);
            message.setMessage("Success");
            service.save(condition);
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

    @DeleteMapping("/deleteDislike")
    public ResponseEntity<Message> deleteDislike(PrefferedPostCondition condition){
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(200L);
            message.setMessage("Success");
            message.setData(service.deleteDislike(condition));
            return new ResponseEntity<>(message,headers, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e);
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(400L);
            message.setMessage("잘못된 요청입니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }
    }
}
