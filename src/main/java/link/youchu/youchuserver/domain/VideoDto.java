package link.youchu.youchuserver.domain;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class VideoDto {
    private String videoProfile;
    private String title;
    private String publishedAt;

    @QueryProjection
    public VideoDto(String videoProfile, String title, String publishedAt) {
        this.videoProfile = videoProfile;
        this.title = title;
        this.publishedAt = publishedAt;
    }
}
