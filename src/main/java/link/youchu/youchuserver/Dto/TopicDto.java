package link.youchu.youchuserver.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class TopicDto {
    private String topic_name;

    @QueryProjection
    public TopicDto(String topic_name) {
        this.topic_name = topic_name;
    }
}
