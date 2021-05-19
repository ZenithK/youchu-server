package link.youchu.youchuserver.Dto;

import lombok.Data;

@Data
public class UserPostCondition {
    private Long user_id;
    private String google_user_id;
    private String user_token;
}
