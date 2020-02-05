package cn.niter.forum.dto;

import lombok.Data;

@Data
public class QqAccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String grant_type;
}
