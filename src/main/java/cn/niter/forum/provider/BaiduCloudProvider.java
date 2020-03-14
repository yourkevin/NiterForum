package cn.niter.forum.provider;

import cn.niter.forum.dto.ResultDTO;
import com.baidu.aip.contentcensor.AipContentCensor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BaiduCloudProvider {
    @Value("${baiducloud.censor.appid}")
    private  String APP_ID;
    @Value("${baiducloud.censor.apikey}")
    private  String API_KEY;
    @Value("${baiducloud.censor.secretkey}")
    private  String SECRET_KEY ;
    @Value("${baiducloud.censor.enable}")
    private int censorEnable;


    public  ResultDTO getTextCensorReult(String text) {

        if(censorEnable==0) return ResultDTO.resultOf(1,"未开启内容审核，自动通过");
        // 初始化一个AipImageCensor
        AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);
        JSONObject response = client.textCensorUserDefined(text);

        if(response.has("error_code")){ //有错误
            return ResultDTO.resultOf(0,response.getString("error_msg"));
        }
        else {//无错误

            int conclusionType = response.getInt("conclusionType");
            String conclusion = response.getString("conclusion");
            if(conclusionType==1) //合规通过
                return ResultDTO.resultOf(conclusionType,conclusion);
            JSONArray data = response.getJSONArray("data");
            String msg =null;
            for(int i=0; i<data.length(); i++) {
                JSONObject thisData = data.getJSONObject(i);
                if(msg==null) msg = thisData.getString("msg").replace("百度","");
                else msg = msg+"以及"+thisData.getString("msg").replace("百度","");
            }

            //System.out.println(msg);
            return ResultDTO.resultOf(conclusionType,msg);
        }


        //System.out.println(response.toString());


    }

}
