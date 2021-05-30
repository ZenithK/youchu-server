package link.youchu.youchuserver.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    private static final String[] PUBLIC_URI = {
            "/register", "/channel", "/recommend", "/keyword", "/similar/**","/rank","/channelByKeyword",
            "/banner","/getPrefer","/getDislike","/random","/latest","/relate","/hello","/userIndex","/getTopic",
            "/index.html", "/Personal_Information.html","/license_information.html", "/Service.html", "/",
            "/img.png","/page2.png", "/page3.png","/page4.png", "/page5.png", "/style.css"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().disable()
                .and()
                .authorizeRequests()
                .antMatchers(PUBLIC_URI)
                .permitAll()
                .anyRequest()
                .hasRole("AUTH")
                .and()
                .httpBasic().authenticationEntryPoint(authenticationEntryPoint);
        http.addFilterBefore(tokenAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(){
        return new TokenAuthenticationFilter();
    }
}
