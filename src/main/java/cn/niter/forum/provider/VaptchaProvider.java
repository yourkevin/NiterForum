package cn.niter.forum.provider;


import okhttp3.*;

public class VaptchaProvider {
    public static String getValidateResult(String token,int scene,String ip){
        MediaType mediaType = MediaType.get("application/x-www-form-urlencoded; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        String s = "id=5ddff34a3f11e776a4947f39&secretkey=5fcab456ff884a1f84bcb96fac0c6beb&token="+token+"&scene="+scene+"&ip="+ip;
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
