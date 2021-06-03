package link.youchu.youchuserver.Dto;

import lombok.Data;

@Data
public class AppleLoginDto {
    private String identity_token;
    private String apple_user_id;
    private String user_email;
}
