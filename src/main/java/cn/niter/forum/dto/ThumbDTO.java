package cn.niter.forum.dto;

import lombok.Data;

/**
 * @author wadao
 * @version 1.0
 * @date 2020/7/13 23:24
 * @site niter.cn
 */
@Data
public class ThumbDTO {
    private Long id;
    private Long targetId;
    private Integer type;
    private UserDTO user;
    private Long gmtCreate;
    private Long gmtModified;

}
