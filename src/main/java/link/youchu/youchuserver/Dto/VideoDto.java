package link.youchu.youchuserver.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class VideoDto {
    private String videoProfile;
    private String title;
    private String publishedAt;
    private Long viewCount;
    private String videoId;

    @QueryProjection
    public VideoDto(String videoProfile, String title, String publishedAt, Long viewCount,String videoId) {
        this.videoProfile = videoProfile;
        this.title = title;
        this.publishedAt = publishedAt;
        this.viewCount = viewCount;
        this.videoId = videoId;
    }
}
