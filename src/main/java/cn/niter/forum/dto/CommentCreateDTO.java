package cn.niter.forum.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentCreateDTO {
    @NotNull(message = "评论目标id不允许为空")
    private Long parentId;

    @NotBlank(message = "评论内容不允许为空")
    private String content;

    @NotNull(message = "评论类型不允许为空")
    private Integer type;

    @NotBlank(message = "评论必须携带token")
    private String token;

    @NotBlank(message = "评论必须携带ip")
    private String ip;
}
