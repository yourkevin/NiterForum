package cn.niter.forum.dto;

import lombok.Data;

/**
 * @author wadao
 * @version 1.0
 * @date 2020/9/23 18:43
 * @site niter.cn
 */

@Data
public class TalkDTO {
    private Long id;
    private Integer type;
    private Integer status;
    private Integer permission;
    private Long gmtCreate;
    private Long gmtModified;
    private Long gmtLatestComment;
    //private String gmtModifiedStr;
    private Integer viewCount = 0;
    private Integer likeCount = 0;
    private Integer commentCount = 0;
    private String description;
    private String title;
    private String images;
    private String video;
    private UserDTO user;
}
