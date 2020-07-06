package cn.niter.forum.dto;

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
    private String gmtModifiedStr;
    private Long likeCount = 0L;
    private Integer commentCount = 0;
    private String content;
    private UserDTO user;
    private UserAccount userAccount;

}
