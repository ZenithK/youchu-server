package link.youchu.youchuserver.Dto;

import lombok.Data;

@Data
public class TokenUpdateCondition {
    private String google_user_id;
    private String access_token;
}
