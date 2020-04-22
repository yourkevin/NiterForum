package cn.niter.forum.dto;

import lombok.Data;

/**
 * @author wadao
 * @version 1.0
 * @date 2020/4/18 20:17
 * @site niter.cn
 */

@Data
public class CommentQueryDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Integer page;
    private Integer size;
    private Integer offset;
}
