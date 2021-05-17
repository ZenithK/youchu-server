package link.youchu.youchuserver.controller;

import link.youchu.youchuserver.Dto.*;
import link.youchu.youchuserver.Http.Message;
import link.youchu.youchuserver.Http.StatusEnum;
import link.youchu.youchuserver.domain.Channel;
import link.youchu.youchuserver.repository.ChannelRepository;
import link.youchu.youchuserver.repository.PrefferedChannelsRepository;
import link.youchu.youchuserver.service.ChannelService;
import link.youchu.youchuserver.service.PrefferedChannelService;
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
    private final PrefferedChannelService prefferedChannelService;

    @GetMapping("/channel")
    public ResponseEntity<Message> getChannelData(ChannelSearchCondition condition){
       try {
           Message message = new Message();
           HttpHeaders headers = new HttpHeaders();
           headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
           message.setStatus(StatusEnum.OK);
           message.setMessage("Success");
           ChannelDto channelDtos = channelService.getChannelData(condition);
           UserSearchCondition searchCondition = new UserSearchCondition();
           searchCondition.setUser_id(condition.getUser_id());
           List<ChannelDto> preferredDtos = prefferedChannelService.getPrefferedList(searchCondition);
           for(ChannelDto c : preferredDtos){
               if(c.getChannel_id().equals(channelDtos.getChannel_id())){
                   channelDtos.setIsPrefered(true);
               }
           }
           message.setData(channelDtos);

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
    public ResponseEntity<Message> getRankingChannelByTopic(TopicSearchCondition condition, Pageable pageable){
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(StatusEnum.OK);
            message.setMessage("Success");
            message.setData(channelService.getRankingChannelByTopic(condition,pageable));

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

    @GetMapping("/verify")
    public List<Long> verify(){
        return channelService.getVerfied();
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
