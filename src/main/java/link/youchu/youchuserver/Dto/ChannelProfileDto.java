package link.youchu.youchuserver.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ChannelProfileDto {
    private String title;
    private String channel_id;
    private String thumbnail;

    @QueryProjection
    public ChannelProfileDto(String title, String channel_id, String thumbnail) {
        this.title = title;
        this.channel_id = channel_id;
        this.thumbnail = thumbnail;
    }
}
