package link.youchu.youchuserver.controller;

import link.youchu.youchuserver.Http.Message;
import link.youchu.youchuserver.Http.StatusEnum;
import link.youchu.youchuserver.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;

@RestController
@RequiredArgsConstructor
public class BannerApiController {

    private final BannerService service;

    @GetMapping("/banner")
    public ResponseEntity<Message> getBanner(){
        try{
            Message message = new Message();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));
            message.setStatus(StatusEnum.OK);
            message.setMessage("Success");
            message.setData(service.getBanner());

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
}
