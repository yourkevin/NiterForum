package cn.niter.forum.provider;

import cn.niter.forum.dto.QqAccessTokenDTO;
import cn.niter.forum.dto.QqUserDTO;
import cn.niter.forum.mapper.UserMapper;
import cn.niter.forum.model.User;
import cn.niter.forum.model.UserExample;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class QqProvider {
    @Autowired
    private UserMapper userMapper;


    public String getAccessToken(QqAccessTokenDTO qqAccessTokenDTO) {
        MediaType mediaType = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String s = "grant_type="+qqAccessTokenDTO.getGrant_type()+"&code="+qqAccessTokenDTO.getCode()+"&client_id="+qqAccessTokenDTO.getClient_id()+"&client_secret="+qqAccessTokenDTO.getClient_secret()+"&redirect_uri="+qqAccessTokenDTO.getRedirect_uri();
        RequestBody body = RequestBody.create(mediaType, s);
        Request request = new Request.Builder()
                .url("https://graph.qq.com/oauth2.0/token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            System.out.println(string);
            return string;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getOpenID(String accessToken) {


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graph.qq.com/oauth2.0/me?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            String jsonString = string.split(" ")[1].split(" ")[0];
            //System.out.println(jsonString);
            JSONObject obj = JSONObject.parseObject(jsonString);
            String openid = obj.getString("openid");
            //System.out.println(openid);
            return openid;
        } catch (IOException e) {
        }
        return null;

    }


    public String getUnionId(String accessToken) {


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graph.qq.com/oauth2.0/me?access_token=" + accessToken+"&unionid=1")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            System.out.println("rs:"+string);
            String jsonString = string.split(" ")[1].split(" ")[0];
            System.out.println(jsonString);
            JSONObject obj = JSONObject.parseObject(jsonString);
            //String openid = obj.getString("openid");
            //String client_id = obj.getString("client_id");
            String unionid = obj.getString("unionid");
            System.out.println(unionid);
            return unionid;
        } catch (IOException e) {
        }
        return null;

    }

    public void allOpenIdtoUnionId(String client_id) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andQqAccountIdIsNotNull().andIdGreaterThan(100L);
        List<User> users = userMapper.selectByExample(userExample);
        //System.out.println("size:"+users.size());
        //System.out.println("openid:"+users.get(0).getQqAccountId());
       // String unionid = openIdtoUnionId(users.get(0).getQqAccountId(),client_id);
       // System.out.println(unionid);
        for (User user : users) {
            String unionid = openIdtoUnionId(user.getQqAccountId(),client_id);
            if(unionid==null) System.out.println("uid为null的openid："+user.getQqAccountId());
            else{
                user.setQqAccountId(unionid);
                userMapper.updateByPrimaryKey(user);
            }
            System.out.println(unionid);
        }
    }

    public String openIdtoUnionId(String openId,String client_id) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graph.qq.com/oauth2.0/get_unionid?openid="+openId+"&client_id="+client_id)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            //System.out.println("rs:"+string);
            String jsonString = string.split(" ")[1].split(" ")[0];
            //System.out.println(jsonString);
            JSONObject obj = JSONObject.parseObject(jsonString);
            //String openid = obj.getString("openid");
            //String client_id = obj.getString("client_id");
           // String unionid = obj.getString("unionid");
            //System.out.println(obj.getString("unionid"));
            return obj.getString("unionid");
        } catch (IOException e) {
            return null;
        }

    }


    public QqUserDTO getUser(String accessToken, String oauth_consumer_key,String openid) {


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://graph.qq.com/user/get_user_info?access_token=" + accessToken+"&oauth_consumer_key="+oauth_consumer_key +"&openid="+openid)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            System.out.println(string);
            QqUserDTO qqUserDTO = JSON.parseObject(string, QqUserDTO.class);
            qqUserDTO.setOpenId(openid);
            return qqUserDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
