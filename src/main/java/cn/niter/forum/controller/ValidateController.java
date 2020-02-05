package cn.niter.forum.controller;

import cn.niter.forum.dto.ValidateDTO;
import cn.niter.forum.provider.VaptchaProvider;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ValidateController {

    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    public Object post(@RequestParam(name = "token", required = false) String token,
                       @RequestParam(name = "scene", required = false) int scene,
                       @RequestParam(name = "ip", required = false) String ip) {
       // System.out.println("3333"+token);
        /*ValidateDTO validateDTO = new ValidateDTO();
        validateDTO.setId("5d807776fc650fd878051c24");
        validateDTO.setSecretkey("0758d7dab2674d5c8e4e003cf16c4558");
        validateDTO.setToken(token);*/
        System.out.println("token:"+token+"scene:"+scene+"ip:"+ip);
        String json = VaptchaProvider.getValidateResult(token,scene,ip);

        JSONObject obj = JSONObject.parseObject(json);
        Integer success = obj.getInteger("success");
        Integer score = obj.getInteger("score");
        String msg = obj.getString("msg");
        ValidateDTO validateDTO = new ValidateDTO();
        validateDTO.setMsg(msg);
        validateDTO.setSocre(score);
        validateDTO.setSuccess(success);
     //   System.out.println("result:"+success+score+msg);
        //return ResultDTO.okOf("验证成功！");
        return validateDTO;
    }

    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/getIp", method = RequestMethod.GET)
    public String getIp() {
        // System.out.println("3333"+token);
        /*ValidateDTO validateDTO = new ValidateDTO();
        validateDTO.setId("5d807776fc650fd878051c24");
        validateDTO.setSecretkey("0758d7dab2674d5c8e4e003cf16c4558");
        validateDTO.setToken(token);*/
        String returnCitySN = VaptchaProvider.getIp();
        String json = returnCitySN.split("=")[1].split(";")[0];
        JSONObject obj = JSONObject.parseObject(json);
        String cip = obj.getString("cip");
        String cid = obj.getString("cid");
        String cname = obj.getString("cname");

       /* ValidateDTO validateDTO = new ValidateDTO();
        validateDTO.setMsg(msg);
        validateDTO.setSocre(score);
        validateDTO.setSuccess(success);*/
        //   System.out.println("result:"+success+score+msg);
        //return ResultDTO.okOf("验证成功！");
        return cip;
    }

}
