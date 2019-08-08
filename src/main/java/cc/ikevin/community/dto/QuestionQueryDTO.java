package cc.ikevin.community.dto;

import lombok.Data;

@Data
public class QuestionQueryDTO {
    private String search;
    private String tag;
    private Integer offset;
    private Integer size;
}
