package cn.niter.forum.dto;

import lombok.Data;

@Data
public class AuthResultDTO {
    private int ret;
    private String openid;
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String scope;
    private String pay_token;
    private String pf;
    private String pfkey;
    private String msg;
    private int login_cost;
    //private String query_authority_cost;
    private int authority_cost;
}
