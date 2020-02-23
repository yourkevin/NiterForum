package cn.niter.forum.provider;


import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VaptchaProvider {

    private static String vid ;
    @Value("${vaptcha.vid}")
    public  void setVid(String vid) {
        this.vid= vid;
    }

    private static String key ;
    @Value("${vaptcha.key}")
    public  void setKey(String key) {
        this.key= key;
    }



    public static String getValidateResult(String token,int scene,String ip){
        MediaType mediaType = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
       // System.out.println("vid:"+vid);
        String s = "id="+vid+"&secretkey="+key+"&token="+token+"&scene="+scene+"&ip="+ip;
       // RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(validateDTO));
        RequestBody body = RequestBody.create(mediaType, s);
        //System.out.println("body:"+body.toString());
        Request request = new Request.Builder()
                .url("http://0.vaptcha.com/verify")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            System.out.println(string);
           // String token = string.split("&")[0].split("=")[1];
            return string;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static String getIp(){
        MediaType mediaType = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String s = "ie=utf-8";
        // RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(validateDTO));
        RequestBody body = RequestBody.create(mediaType, s);
        //System.out.println("body:"+body.toString());
        Request request = new Request.Builder()
                .url("http://pv.sohu.com/cityjson")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            // System.out.println(string);
            // String token = string.split("&")[0].split("=")[1];
            return string;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


}
