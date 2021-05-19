package link.youchu.youchuserver.Http;

import lombok.Data;

@Data
public class ComplexMessage {
    private Long status;
    private String message;
    private String standardValue;
    private Object data;


    public ComplexMessage() {
        this.status = 200L;
        this.data = null;
        this.message = null;
        this.standardValue = null;
    }

}
