package link.youchu.youchuserver.Config;

import lombok.Builder;
import org.springframework.stereotype.Service;

public interface AuthenticationToken {
    String getToken();
}
