package link.youchu.youchuserver.Dto;

import lombok.Data;

@Data
public class UserPostCondition {
    private String google_user_id;
    private String user_token;
    private String refresh_token;
}
