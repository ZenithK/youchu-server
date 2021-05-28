package link.youchu.youchuserver.Config;

import org.json.simple.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setContentType("application/json;charset=utf-8");
        JSONObject json = new JSONObject();
        String message = "토큰이 만료되었습니다";
        json.put("status",302);
        json.put("message",message);

        PrintWriter out = response.getWriter();
        out.println(json);
    }
}
