package cn.niter.forum.dto;

import cn.niter.forum.model.User;
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
    private User user;
    private UserAccount userAccount;
    private Long gmtLatestComment;
    private Integer status;
    private Integer column2;
    private Integer permission;

}
