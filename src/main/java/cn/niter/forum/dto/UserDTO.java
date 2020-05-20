package cn.niter.forum.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String avatarUrl;
    private Integer vipRank;
    private Integer groupId;
}
