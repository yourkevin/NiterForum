package cn.niter.forum.provider;

import cn.niter.forum.dto.BaiduAccessTokenDTO;
import cn.niter.forum.dto.BaiduUserDTO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BaiduProvider {
    public String getAccessToken(BaiduAccessTokenDTO baiduAccessTokenDTO) {
        MediaType mediaType = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String s = "grant_type="+baiduAccessTokenDTO.getGrant_type()+"&code="+baiduAccessTokenDTO.getCode()+"&client_id="+baiduAccessTokenDTO.getClient_id()+"&client_secret="+baiduAccessTokenDTO.getClient_secret()+"&redirect_uri="+baiduAccessTokenDTO.getRedirect_uri();
        RequestBody body = RequestBody.create(mediaType, s);
        Request request = new Request.Builder()
                .url("https://openapi.baidu.com/oauth/2.0/token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            JSONObject obj = JSONObject.parseObject(string);
            String access_token = obj.getString("access_token");

            // String token = string.split("&")[0].split("=")[1];

            //System.out.println("jsonstring"+JSON.toJSONString(baiduAccessTokenDTO));
          //  System.out.println("string为"+string+"access_token为"+access_token);
            return access_token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public BaiduUserDTO getUser(String accessToken) {


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://openapi.baidu.com/rest/2.0/passport/users/getInfo?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            System.out.println(string);
            BaiduUserDTO baiduUserDTO = JSON.parseObject(string, BaiduUserDTO.class);

            return baiduUserDTO;
        } catch (IOException e) {
        }
        return null;

    }
}
