package link.youchu.youchuserver.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class SimpleDtoPlusBanner implements Comparable<SimpleDtoPlusBanner>{
    private Long channel_index;
    private String title;
    private String thumbnail;
    private Long subscriber_count;
    private String channel_id;
    private int isPreferred;
    private String banner_image;

    @QueryProjection
    public SimpleDtoPlusBanner(Long channel_index,String title, String thumbnail, Long subscriber_count, String banner_image, String channel_id) {
        this.channel_index = channel_index;
        this.title = title;
        this.thumbnail = thumbnail;
        this.subscriber_count = subscriber_count;
        this.channel_id = channel_id;
        this.banner_image = banner_image;
        this.isPreferred = 0;
    }

    @QueryProjection
    public SimpleDtoPlusBanner(Long channel_index,String title, String thumbnail, Long subscriber_count,String banner_image, String channel_id, int isPreferred) {
        this.channel_index = channel_index;
        this.title = title;
        this.thumbnail = thumbnail;
        this.subscriber_count = subscriber_count;
        this.banner_image = banner_image;
        this.channel_id = channel_id;
        this.isPreferred = isPreferred;
    }

    @Override
    public int compareTo(SimpleDtoPlusBanner simpleDtoPlusBanner) {
        return 0;
    }
}
