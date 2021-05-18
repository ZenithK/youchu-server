package link.youchu.youchuserver.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class SimpleChannelDto {
    private String title;
    private String thumbnail;
    private Long subscriber_count;
    private String channel_id;

    @QueryProjection
    public SimpleChannelDto(String title, String thumbnail, Long subscriber_count, String channel_id) {
        this.title = title;
        this.thumbnail = thumbnail;
        this.subscriber_count = subscriber_count;
        this.channel_id = channel_id;
    }
}
