package cn.niter.forum.dto;


import lombok.Data;

@Data
public class AuthUserInfoDTO {
    private String openId;//唯一id
    private String nickname;//用户在QQ空间的昵称。
    private String figureurl_qq;//大小为40×40像素的QQ头像URL。
    private String gender;//qq性别。 如果获取不到则默认返回"男"。微博性别，m：男、f：女、n：未知
    private String province;
    private String city;
    private String year;
    private String constellation;
    private String idstr;
    private String screen_name;//用户昵称
    private String name;//友好显示名称
    private String location;//用户所在地
    private String description;//
    private String avatar_hd;//
}
