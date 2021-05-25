package link.youchu.youchuserver.Http;

import lombok.Data;

@Data
public class UserInfoMessage {
    private Long status;
    private String message;
    private Object token;
    private Object data;

    public UserInfoMessage() {
        this.status = 200L;
        this.message = null;
        this.token = null;
        this.data = null;
    }
}
