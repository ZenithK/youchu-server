package link.youchu.youchuserver.Dto;

import lombok.Data;

@Data
public class KeywordSearchCondition {
    private Long keyword_id;
    private String keyword_name;
    private Long user_id;
}
