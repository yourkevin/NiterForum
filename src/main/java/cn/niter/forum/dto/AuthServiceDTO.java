package cn.niter.forum.dto;

import lombok.Data;

@Data
public class AuthServiceDTO {
    private String id;
    private String description;
    //private AppleInfo appleInfo;
    private AuthResultDTO authResult;
    private AuthUserInfoDTO userInfo;
}
