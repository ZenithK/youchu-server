package link.youchu.youchuserver.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class UserDto {
    private Long user_id;
    private String user_email;
    private Long prefer_count;
    private Long dislike_count;
    private String domain = null;

    @QueryProjection
    public UserDto(Long user_id, String user_email) {
        this.user_id = user_id;
        this.user_email = user_email;
        this.prefer_count = 0L;
        this.dislike_count = 0L;
    }
}
