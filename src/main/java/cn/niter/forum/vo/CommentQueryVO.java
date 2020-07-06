package cn.niter.forum.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wadao
 * @version 1.0
 * @date 2020/7/5 0:46
 * @site niter.cn
 */

@Data
@ApiModel(description="评论查询视图对象")
public class CommentQueryVO {
    @ApiModelProperty(value="评论的id",example="1")
    private Long id;

    @ApiModelProperty(value="评论目标的id",example="1")
    private Long parentId;

    @ApiModelProperty(value="评论类型,1为回复主题，2为回复评论",example="1")
    private Integer type;

    @ApiModelProperty(value="评论者id",example="1")
    private Long commentator;

    @ApiModelProperty(value="评论的页数",example="1")
    private Integer page;

    @ApiModelProperty(value="尺寸",example="1")
    private Integer size;

    @ApiModelProperty(value="offset",example="1")
    private Integer offset;
}
