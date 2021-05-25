package link.youchu.youchuserver.Config;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import link.youchu.youchuserver.domain.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationTokenProvider implements AuthenticationTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationTokenProvider.class);

    private static final String AUTHORITIES_KEY = "auth";

    private final String SECRET_KEY;

    private final long EXPIRATIION_MS;


    private Key key;

    public JwtAuthenticationTokenProvider(@Value("${spring.secret.key}") String secret,
                                          @Value("${spring.secret.time}") long tokenValidityInSeconds) {
        this.SECRET_KEY = secret;
        this.EXPIRATIION_MS = tokenValidityInSeconds;
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String token) {
        try {
            if (!token.equals(null)) {
                try {
                    Jwts.parser().setSigningKey(Decoders.BASE64.decode(SECRET_KEY)).parseClaimsJws(token);
                    return true;
                } catch (SignatureException e) {
                    logger.error("Invalid JWT signature", e);
                } catch (MalformedJwtException e) {
                    logger.error("Invalid JWT token", e);
                } catch (ExpiredJwtException e) {
                    logger.error("Expired JWT Token", e);
                } catch (UnsupportedJwtException e) {
                    logger.error("Unsupported JWT Token", e);
                } catch (IllegalArgumentException e) {
                    logger.error("JWT claims string is empty", e);
                }
            }
        }catch (Exception e){
            return false;
        }
        return false;
    }

    @Override
    public String parseTokenString(HttpServletRequest request) {
        String basicToken = request.getHeader("Authorization");
        if(basicToken != null && basicToken.startsWith("Bearer ")){
            return basicToken.substring(7);
        }
        return null;
    }

    @Override
    public AuthenticationToken issue(Long userNo) {
        return JwtAuthenticationToken.builder().token(buildToken(userNo)).build();
    }
    @Override
    public Long getTokenOwnerNo(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(Decoders.BASE64.decode(SECRET_KEY))
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    private String buildToken(Long userNo){
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expireAt = now.plus(EXPIRATIION_MS, ChronoUnit.MILLIS);
        return Jwts.builder()
                .setSubject(String.valueOf(userNo))
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expireAt.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}
