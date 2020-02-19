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
    private Integer permission;

}
