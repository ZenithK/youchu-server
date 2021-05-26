package link.youchu.youchuserver.Config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import link.youchu.youchuserver.domain.SecurityUser;
import link.youchu.youchuserver.repository.UserRepository;
import link.youchu.youchuserver.service.UserService;
import link.youchu.youchuserver.service.UsersDetailService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;
import java.util.Arrays;

@NoArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private JwtAuthenticationTokenProvider tokenProvider;

    public TokenAuthenticationFilter(JwtAuthenticationTokenProvider tokenProvider){
        this.tokenProvider = tokenProvider;
    }

    @Autowired
    private AuthenticationTokenProvider authenticationTokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private UsersDetailService usersDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = authenticationTokenProvider.parseTokenString(request);
        if (authenticationTokenProvider.validateToken(token)) {
            Long userNo = authenticationTokenProvider.getTokenOwnerNo(token);
            try{
                SecurityUser user = new SecurityUser(userService.getUserById(userNo));
                UserDetails authentication = usersDetailService.loadUserByUsername(user.getUsername());
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(authentication.getUsername(),null,authentication.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (MalformedJwtException e) {
                logger.error("Invalid JWT token", e);
            } catch (ExpiredJwtException e) {
                logger.error("Expired JWT token", e);
            } catch (UnsupportedJwtException e) {
                logger.error("Unsupported JWT token", e);
            } catch (IllegalArgumentException e) {
                logger.error("JWT claims string is empty.", e);
            }

        }
        filterChain.doFilter(request,response);
    }
}
