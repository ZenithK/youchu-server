package link.youchu.youchuserver;

import link.youchu.youchuserver.Config.JwtAuthenticationTokenProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class YouchuServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(YouchuServerApplication.class, args);
    }

}
