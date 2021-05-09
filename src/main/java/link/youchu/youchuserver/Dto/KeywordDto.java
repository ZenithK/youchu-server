package link.youchu.youchuserver.Dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class KeywordDto {
    private String keyword_name;

    @QueryProjection
    public KeywordDto(String keyword_name) {
        this.keyword_name = keyword_name;
    }
}
