//package link.youchu.youchuserver.Config;
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.web.filter.CharacterEncodingFilter;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterRegistration;
//import java.nio.charset.Charset;
//
//@Configuration
//public class SpringConfig {
//    @Bean
//    public HttpMessageConverter<String> requestConverter() {
//        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
//    }
//
//    @Bean
//    public Filter characterEncodingFilter(){
//        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
//        characterEncodingFilter.setEncoding("UTF-8");
//        characterEncodingFilter.setForceEncoding(true);
//        return characterEncodingFilter;
//    }
//}
