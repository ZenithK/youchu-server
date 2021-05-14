package link.youchu.youchuserver.controller;

import link.youchu.youchuserver.Dto.PrefferedChannelDto;
import link.youchu.youchuserver.Dto.PrefferedPostCondition;
import link.youchu.youchuserver.Dto.UserSearchCondition;
import link.youchu.youchuserver.Http.Message;
import link.youchu.youchuserver.Http.StatusEnum;
import link.youchu.youchuserver.domain.PrefferedChannels;
import link.youchu.youchuserver.repository.PrefferedChannelsRepository;
import link.youchu.youchuserver.service.PrefferedChannelService;
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
public class PrefferedChannelApiController {

    private final PrefferedChannelService service;
    private final PrefferedChannelsRepository repository;

    @GetMapping("/getPrefer")
    public ResponseEntity<Message> getPrefferedChannel(UserSearchCondition condition) {
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(StatusEnum.OK);
            message.setMessage("Success");
            message.setData(service.getPrefferedList(condition));

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



    @RequestMapping(value = "/prefer",method = {RequestMethod.POST})
    public ResponseEntity<Message> postPrefferedChannel(PrefferedPostCondition condition){
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(StatusEnum.OK);
            message.setMessage("Success");
            PrefferedChannels prefferedChannels = new PrefferedChannels(condition.getChannel_index(), condition.getUser_id());
            prefferedChannels.setId(prefferedChannels.getId());
            service.save(prefferedChannels);
            repository.flush();
            message.setData(1);

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

    @DeleteMapping("/deletePreffered")
    public ResponseEntity<Message> deletePreffered(PrefferedPostCondition condition){
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(StatusEnum.OK);
            message.setMessage("Success");
            message.setData(service.deletePreffered(condition));

            return new ResponseEntity<>(message,headers, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e);
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(StatusEnum.BAD_REQUEST);
            message.setMessage("잘못된 요청입니다.");
            return new ResponseEntity<>(message,headers, HttpStatus.BAD_REQUEST);
        }
    }
}