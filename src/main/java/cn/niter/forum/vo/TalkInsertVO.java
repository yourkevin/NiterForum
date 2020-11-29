package cn.niter.forum.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author wadao
 * @version 1.0
 * @date 2020/9/23 18:34
 * @site niter.cn
 */

@Data
@ApiModel(description="说说创建视图对象")
public class TalkInsertVO {

    @ApiModelProperty(value="内容",required=true)
    @NotBlank(message = "内容不允许为空")
    private String description;

    @ApiModelProperty(value="说说类型,默认为1",required=true,example="1")
    @NotNull(message = "说说类型不允许为空")
    private Integer type;

    @ApiModelProperty(value="图片地址")
    private String images;

    @ApiModelProperty(value="视频地址")
    private String video;

   @ApiModelProperty(value="此token在完成验证后自动生成",required=true)
    @NotBlank(message = "评论必须携带token")
    private String token;

    @ApiModelProperty(value="用户的ip地址",required=true)
    @NotBlank(message = "无法正常获取用户ip")
    private String ip;
}
