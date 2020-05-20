package cn.niter.forum.dto;

import lombok.Data;

@Data
public class BaiduUserDTO {
    private String username;
    private Long userid;
    private String is_realname;
    private String is_bind_mobile;
    private String portrait;//头像id http://tb.himg.baidu.com/sys/portrait/item/-portrait-
    private String userdetail;//自我简介，可能为空。
    private String birthday;//生日，以yyyy-mm-dd格式显示。
    private String marriage;//婚姻状况
    private String sex;//性别性别。"1"表示男，"0"表示女。
    private String blood;//血型
    private String figure;//体型
    private String constellation;//星座
    private String education;//学历
    private String trade;//当前职业
    private String job;//职业
    private String openid;
}
