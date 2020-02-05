package cn.niter.forum.dto;

import cn.niter.forum.model.User;
import cn.niter.forum.model.UserAccount;
import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private Integer commentCount;
    private String content;
    private User user;
    private UserAccount userAccount;

}
