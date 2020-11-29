package cn.niter.forum.vo;

import cn.niter.forum.dto.UserDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wadao
 * @version 1.0
 * @date 2020/9/24 21:33
 * @site niter.cn
 */

@Data
@ApiModel(description="说说视图对象")
public class TalkVO {


    @ApiModelProperty(value="说说id")
    private Long id;

    @ApiModelProperty(value="说说状态")
    private Integer status;

    @ApiModelProperty(value="说说创建时间")
    private Long gmtCreate;

    @ApiModelProperty(value="说说修改时间")
    private Long gmtModified;

    @ApiModelProperty(value="说说修改时间字符串")
    private String gmtModifiedStr;

    @ApiModelProperty(value="最后评论时间")
    private Long gmtLatestComment;

    @ApiModelProperty(value="最后评论时间字符串")
    private String gmtLatestCommentStr;

    @ApiModelProperty(value="内容")
    private String description;

    @ApiModelProperty(value="说说类型,默认为1",example="1")
    private Integer type;

    @ApiModelProperty(value="图片地址")
    private String images;

    @ApiModelProperty(value="图片地址数组")
    private String[] imageUrls;

    @ApiModelProperty(value="视频地址")
    private String video;

    @ApiModelProperty(value="浏览数",example="1")
    private Integer viewCount = 0;

    @ApiModelProperty(value="点赞数",example="1")
    private Integer likeCount = 0;

    @ApiModelProperty(value="评论数",example="1")
    private Integer commentCount = 0;

    @ApiModelProperty(value="发表者")
    private UserDTO user;



    @ApiModelProperty(value="标题")
    private String title;

    @ApiModelProperty(value="阅读权限",example="0")
    private Integer permission;

    @ApiModelProperty(value="用户组字符串")
    private String userGroupStr;


    //状态
    @ApiModelProperty(value="是否合法（通过审核）")
    private boolean approved = true;//是否合法（通过审核）
    @ApiModelProperty(value="是否置顶")
    private boolean sticky = false;//是否置顶
    @ApiModelProperty(value="是否加精")
    private boolean essence = false;//是否加精
    @ApiModelProperty(value="是否收藏")
    private boolean favorite = false;//是否收藏
    @ApiModelProperty(value="是否编辑过")
    private boolean edited = false;//是否编辑过
    @ApiModelProperty(value="是否收费")
    private boolean paid = false;//是否收费
    @ApiModelProperty(value="能否回复")
    private boolean canReply = true;//能否回复
    @ApiModelProperty(value="能否查看")
    private boolean canView = true;//能否查看
    @ApiModelProperty(value="能否收藏")
    private boolean canFavorite = true;//能否收藏
    @ApiModelProperty(value="能否置顶，最高权限标志")
    private boolean canSticky = false;//能否置顶，最高权限标志
    @ApiModelProperty(value="能否加精")
    private boolean canEssence = false;//能否加精
    @ApiModelProperty(value="能否删除")
    private boolean canDelete = false;//能否删除
    @ApiModelProperty(value="能否提升")
    private boolean canPromote = false;//能否提升
    @ApiModelProperty(value="能否修改分类")
    private boolean canClassify = false;//能否修改分类
    @ApiModelProperty(value="能否编辑")
    private boolean canEdit = false;//能否编辑
    /*public void convert(){//对部分的参数进行转化
       //待完善
        this.gmtLatestCommentStr = timeUtils.getTime(this.gmtLatestComment,null);
        this.userGroupStr = env.getProperty("user.group.r"+this.user.getGroupId());

    }*/
}
