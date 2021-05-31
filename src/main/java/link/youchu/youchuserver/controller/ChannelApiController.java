package link.youchu.youchuserver.controller;

import link.youchu.youchuserver.Dto.*;
import link.youchu.youchuserver.Http.ComplexMessage;
import link.youchu.youchuserver.Http.Message;
import link.youchu.youchuserver.Http.StatusEnum;
import link.youchu.youchuserver.domain.Channel;
import link.youchu.youchuserver.domain.DislikeChannels;
import link.youchu.youchuserver.repository.ChannelRepository;
import link.youchu.youchuserver.repository.PrefferedChannelsRepository;
import link.youchu.youchuserver.service.ChannelService;
import link.youchu.youchuserver.service.DislikeChannelService;
import link.youchu.youchuserver.service.PrefferedChannelService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChannelApiController {

    private final ChannelService channelService;
    private final DislikeChannelService dislikeChannelService;
    private final PrefferedChannelService prefferedChannelService;

    @GetMapping("/channel")
    public ResponseEntity<Message> getChannelData(ChannelSearchCondition condition){
       try {
           Message message = new Message();
           HttpHeaders headers = new HttpHeaders();
           headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
           message.setStatus(200L);
           message.setMessage("Success");
           ChannelDto channelDtos = channelService.getChannelData(condition);
           UserSearchCondition searchCondition = new UserSearchCondition();
           searchCondition.setUser_id(condition.getUser_id());
           List<ChannelDto> preferredDtos = prefferedChannelService.getPrefferedList(searchCondition);
           List<ChannelDto> dislikeChannel = dislikeChannelService.getDislikeChannel(searchCondition);
           for(ChannelDto c : preferredDtos){
               if(c.getChannel_id().equals(channelDtos.getChannel_id())){
                   channelDtos.setIsPreferred(1);
               }
           }
           for (ChannelDto c : dislikeChannel) {
               if(c.getChannel_id().equals(channelDtos.getChannel_id())){
                   channelDtos.setIsPreferred(2);
               }
           }
           message.setData(channelDtos);
           return new ResponseEntity<>(message, headers, HttpStatus.OK);
       }catch (Exception e){
           Message message = new Message();
           HttpHeaders headers = new HttpHeaders();
           headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
           message.setStatus(400L);
           message.setMessage("잘못된 요청입니다.");
           return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
       }
    }

    @GetMapping("/relate")
    public ResponseEntity<ComplexMessage> getRelatedChannel(UserSearchCondition condition){
        try{
            ComplexMessage message = channelService.getRelatedChannel(condition);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(200L);
            message.setMessage("Success");
            return new ResponseEntity<>(message,headers, HttpStatus.OK);

        }catch (Exception e){
            ComplexMessage message = new ComplexMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(400L);
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
            message.setStatus(200L);
            message.setMessage("Success");
            if(condition.getTopic_name().equals("전체")){
                condition.setTopic_name(null);
            }
            message.setData(channelService.getRankingChannelByTopic(condition,pageable));
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

    @GetMapping("/latest")
    public ResponseEntity<Message> getLastestChannelList(ChannelSearchCondition condition){
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(200L);
            message.setMessage("Success");

            message.setData(channelService.getLatestChannel(condition));
            return new ResponseEntity<>(message,headers, HttpStatus.OK);

        }catch(InvalidKeyException e){
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(401L);
            message.setMessage("API Key의 할당량이 초과되었습니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        } catch(ParseException e) {
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(204L);
            message.setMessage("채널에 비디오가 없습니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(400L);
            message.setMessage("잘못된 요청입니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/recommend")
    public ResponseEntity<Message> getRecommendChannelList(UserSearchCondition condition) {
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(200L);
            message.setMessage("Success");
            message.setData(channelService.getRecommendChannel(condition));
            return new ResponseEntity<>(message,headers, HttpStatus.OK);
        }catch(Exception e){
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(400L);
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
            message.setStatus(200L);
            message.setMessage("Success");
            message.setData(channelService.getChannelByOneKeyword(condition,pageable));
            return new ResponseEntity<>(message,headers, HttpStatus.OK);
        }catch(Exception e){
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(400L);
            message.setMessage("잘못된 요청입니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/random")
    public ResponseEntity<Message> getRandomChannel(UserSearchCondition condition) {
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(200L);
            message.setMessage("Success");
            message.setData(channelService.getRandomChannel(condition));
            return new ResponseEntity<>(message,headers, HttpStatus.OK);
        }catch(Exception e){
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(400L);
            message.setMessage("잘못된 요청입니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/similar/topic")
    public ResponseEntity<ComplexMessage> getSimilarTopic(UserSearchCondition condition,Pageable pageable) {
        try{
            ComplexMessage message = channelService.getSimilarTopic(condition,pageable);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(200L);
            message.setMessage("Success");

            return new ResponseEntity<>(message,headers, HttpStatus.OK);
        }catch(Exception e){
            ComplexMessage message = new ComplexMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(400L);
            message.setMessage("잘못된 요청입니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/similar/keyword")
    public ResponseEntity<ComplexMessage> getSimilarKeyword(UserSearchCondition condition, Pageable pageable) {
        try{
            ComplexMessage message = channelService.getSimilarKeyword(condition,pageable);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(200L);
            message.setMessage("Success");

            return new ResponseEntity<>(message,headers, HttpStatus.OK);
        }catch(Exception e){
            ComplexMessage message = new ComplexMessage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(400L);
            message.setMessage("잘못된 요청입니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }
    }
}
