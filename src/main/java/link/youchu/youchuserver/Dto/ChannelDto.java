package link.youchu.youchuserver.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class ChannelDto {
    private String title;
    private String description;
    private String publishedAt;
    private String thumbnail;
    private Long view_count;
    private Long subscriber_count;
    private String banner_image;
    private Long video_count;
    private String channel_id;

    @QueryProjection
    public ChannelDto(String title, String description, String published_at, String thumbnail, Long view_count, Long subscriber_count, String banner_image, Long video_count, String channel_id) {
        this.title = title;
        this.description = description;
        this.publishedAt = published_at;
        this.thumbnail = thumbnail;
        this.view_count = view_count;
        this.subscriber_count = subscriber_count;
        this.banner_image = banner_image;
        this.video_count = video_count;
        this.channel_id = channel_id;
    }
}