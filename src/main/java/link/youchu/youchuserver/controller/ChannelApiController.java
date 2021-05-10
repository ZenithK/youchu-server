package link.youchu.youchuserver.controller;

import link.youchu.youchuserver.Dto.*;
import link.youchu.youchuserver.Http.Message;
import link.youchu.youchuserver.Http.StatusEnum;
import link.youchu.youchuserver.repository.ChannelRepository;
import link.youchu.youchuserver.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChannelApiController {

    private final ChannelService channelService;

    @GetMapping("/channel")
    public ResponseEntity<Message> getChannelData(ChannelSearchCondition condition){
       try {
           Message message = new Message();
           HttpHeaders headers = new HttpHeaders();
           headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
           message.setStatus(StatusEnum.OK);
           message.setMessage("Success");
           message.setData(channelService.getChannelData(condition));

           return new ResponseEntity<>(message, headers, HttpStatus.OK);
       }catch (Exception e){
           Message message = new Message();
           HttpHeaders headers = new HttpHeaders();
           headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
           message.setStatus(StatusEnum.BAD_REQUEST);
           message.setMessage("잘못된 요청입니다.");

           return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
       }
    }

    @GetMapping("/rank")
    public ResponseEntity<Message> getRankingChannelByCategory(CategorySearchCondition condition, Pageable pageable){
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(StatusEnum.OK);
            message.setMessage("Success");
            message.setData(channelService.getRankingChannelByCategory(condition,pageable));

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

    @GetMapping("/channelByKeyword")
    public ResponseEntity<Message> getChannelByOneKeyword(KeywordSearchCondition condition, Pageable pageable) {
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(StatusEnum.OK);
            message.setMessage("Success");
            message.setData(channelService.getChannelByOneKeyword(condition,pageable));

            return new ResponseEntity<>(message,headers, HttpStatus.OK);
        }catch(Exception e){
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(StatusEnum.BAD_REQUEST);
            message.setMessage("잘못된 요청입니다.");

            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }
    }

}
