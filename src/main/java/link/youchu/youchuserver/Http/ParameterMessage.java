package link.youchu.youchuserver.Http;

import lombok.Data;

@Data
public class ParameterMessage {
    private Long status;
    private String message;
    private Boolean exist;
    private String token;
    private Object data;


    public ParameterMessage() {
        this.status = 200L;
        this.data = null;
        this.message = null;
        this.exist = false;
    }
}
