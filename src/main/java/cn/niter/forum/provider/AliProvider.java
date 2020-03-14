package cn.niter.forum.provider;


import cn.niter.forum.dto.NewsDTO;
import cn.niter.forum.enums.NewsColumnEnum;
import cn.niter.forum.mapper.NewsMapper;
import cn.niter.forum.model.News;
import cn.niter.forum.util.TimeUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class AliProvider {

    @Autowired
    private NewsMapper newsMapper;
    @Autowired
    private QCloudProvider qCloudProvider;

    @Value("${ali.showapi.appid}")
    private String ali_showapi_appid;

    @Value("${ali.showapi.sign}")
    private String ali_showapi_sign;

    @Value("${qcloud.keywords.enable}")
    private int keywordsEnable;

    public int autoGetNews(String channelId) {
       // String channelId="5572a108b3cdc86cf39001cd";
        /*
        * 5572a108b3cdc86cf39001cd 国内焦点
        * 5572a108b3cdc86cf39001d4 体育焦点
        * 5572a108b3cdc86cf39001d5 娱乐焦点
        * 5572a109b3cdc86cf39001e3 互联网最新
        * 5572a10ab3cdc86cf39001f4 科技最新
        * 5572a10bb3cdc86cf39001f5 数码最新
        * 5572a10bb3cdc86cf39001f6 电脑最新
        * 5572a10bb3cdc86cf39001f7 科普最新
        * */
        //List<TwitterDTO> twitterDTOS = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://route.showapi.com/109-35?showapi_appid="+ali_showapi_appid+"&showapi_sign="+ali_showapi_sign+"&channelId="+channelId+"&needHtml=1&needAllList=0&needContent=1&maxResult=15")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            //System.out.println("json"+string);
            JSONObject obj=JSON.parseObject(string);
            Integer showapi_res_code = obj.getInteger("showapi_res_code");
           // System.out.println("ret_code："+showapi_res_code);
            if(showapi_res_code==0){
             JSONObject showapi_res_body = obj.getJSONObject("showapi_res_body");
             JSONObject pagebean = showapi_res_body.getJSONObject("pagebean");
             JSONArray contentlist = pagebean.getJSONArray("contentlist");
             List<NewsDTO> newsDTOs = JSONObject.parseArray(contentlist.toJSONString(), NewsDTO.class);
               // System.out.println("newsDTOs:"+newsDTOs.size());
                News news = new News();
                for (NewsDTO newsDTO : newsDTOs) {
                    BeanUtils.copyProperties(newsDTO, news);
                   if(newsDTO.getHavePic()){
                       //System.out.println("99");

                      List<Image> images = JSONObject.parseArray(newsDTO.getImageurls().toJSONString(), Image.class);
                      // System.out.println(images.size());
                      for (int i=0;i<images.size();i++) {
                          if(i==0) news.setImageurl1(images.get(0).getUrl());
                          if(i==1) news.setImageurl2(images.get(1).getUrl());
                          if(i==2) news.setImageurl3(images.get(2).getUrl());
                       }
                       //System.out.println(twitterDTO.getImageurl2());
                   }
                    String des = newsDTO.getContent();
                    String regex = "\\（.*?）";
                    des = des.replaceAll(regex, "");
                    if(keywordsEnable==1){
                        String keywords = qCloudProvider.getKeywords(des,5,0.4);
                        news.setTag(keywords.substring(1));
                    }
                    news.setDescription(des.length()>140?des.substring(0,140)+"...":des);
                    news.setGmtCreate(System.currentTimeMillis());
                    news.setGmtModified(news.getGmtCreate());
                    news.setGmtLatestComment(TimeUtils.date2TimeStamp(news.getPubDate(),null));
                    news.setColumn2(NewsColumnEnum.strIdToCode(channelId));
                    news.setStatus(1);
                    news.setViewCount(0);
                    news.setLikeCount(0);
                    news.setCommentCount(0);
                    newsMapper.insert(news);
                }

             //System.out.println(twitterDTOS.get(0).getHtml());
             return 1;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
      }

      @Data
    static class  Image{
        Integer height;
        Integer width;
        String url;
    }
}
