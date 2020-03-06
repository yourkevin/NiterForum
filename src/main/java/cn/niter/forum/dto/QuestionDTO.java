package cn.niter.forum.dto;

import cn.niter.forum.model.UserAccount;
import lombok.Data;

@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private String userGroupName;
    //private User user;
    private UserDTO user;
    private UserAccount userAccount;
    private Long gmtLatestComment;
    private String gmtLatestCommentStr;
    private String shortDescription;
    private Integer isVisible;
    private Integer status;
    private Integer column2;
    private Integer permission;//阅读权限

    //状态
    private boolean approved = true;//是否合法（通过审核）
    private boolean sticky = false;//是否置顶
    private boolean essence = false;//是否加精
    private boolean favorite = false;//是否收藏
    private boolean edited = false;//是否编辑过
    private boolean paid = false;//是否收费
    private boolean canReply = true;//能否回复
    private boolean canView = true;//能否查看
    private boolean canFavorite = true;//能否收藏
    private boolean canSticky = false;//能否置顶，最高权限标志
    private boolean canEssence = false;//能否加精
    private boolean canDelete = false;//能否删除
    private boolean canPromote = false;//能否提升
    private boolean canClassify = false;//能否修改分类
    private boolean canEdit = false;//能否编辑
}
