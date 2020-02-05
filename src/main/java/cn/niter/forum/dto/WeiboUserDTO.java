package cn.niter.forum.dto;

import lombok.Data;

@Data
public class WeiboUserDTO {
    private String idstr;
    private String screen_name;//用户昵称
    private String name;//友好显示名称
    private String location;//用户所在地
    private String description;//
    private String avatar_hd;//
    private String gender;//性别，m：男、f：女、n：未知
}
