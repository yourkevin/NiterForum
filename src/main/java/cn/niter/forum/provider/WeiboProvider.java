package cn.niter.forum.provider;

import cn.niter.forum.dto.WeiboAccessTokenDTO;
import cn.niter.forum.dto.WeiboUserDTO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WeiboProvider {

    public String getAccessToken(WeiboAccessTokenDTO weiboAccessTokenDTO) {
        MediaType mediaType = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String s = "grant_type="+weiboAccessTokenDTO.getGrant_type()+"&code="+weiboAccessTokenDTO.getCode()+"&client_id="+weiboAccessTokenDTO.getClient_id()+"&client_secret="+weiboAccessTokenDTO.getClient_secret()+"&redirect_uri="+weiboAccessTokenDTO.getRedirect_uri();
        RequestBody body = RequestBody.create(mediaType, s);
        Request request = new Request.Builder()
                .url("https://api.weibo.com/oauth2/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();

            return string;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getTokenInfo(String accessToken) {
        MediaType mediaType = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String s = "access_token="+accessToken;
        RequestBody body = RequestBody.create(mediaType, s);
        Request request = new Request.Builder()
                .url("https://api.weibo.com/oauth2/get_token_info")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            JSONObject info = JSON.parseObject(string);
           // System.out.println(info.getString("uid"));
            return info.getString("uid");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public WeiboUserDTO getUser(String accessToken,String uid) {


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.weibo.com/2/users/show.json?access_token=" + accessToken+"&uid="+uid)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            System.out.println(string);
            WeiboUserDTO weiboUserDTO = JSON.parseObject(string, WeiboUserDTO.class);

            return weiboUserDTO;
        } catch (IOException e) {
        }
        return null;

    }

}
