package cn.niter.forum.controller;

import cn.niter.forum.cache.IpLimitCache;
import cn.niter.forum.dto.ResultDTO;
import cn.niter.forum.dto.UserDTO;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.provider.JiGuangProvider;
import cn.niter.forum.service.UserService;
import cn.niter.forum.util.CookieUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wadao
 * @version 2.0
 * @date 2020/5/1 15:17
 * @site niter.cn
 */

@Controller
public class PhoneController {

    @Autowired
    private UserService userService;
    @Autowired
    private IpLimitCache ipLimitCache;
    @Autowired
    private CookieUtils cookieUtils;


    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/phone/getPhoneCode", method = RequestMethod.POST)
    public Object getPhoneCode(@RequestParam("phone") String phone,
                               @RequestParam("ip") String ip,
                               @RequestParam("token") String token){

        if((!token.equals(ipLimitCache.getInterval(ip)))||ipLimitCache.showIpScores(ip)>=100){
            ipLimitCache.addIpScores(ip,20);
            return ResultDTO.errorOf(CustomizeErrorCode.SEND_PHONE_FAILED);
        }
        ipLimitCache.addIpScores(ip,10);
        // TODO 自动生成的方法存根
        try {
            String jsonString = JiGuangProvider.testSendSMSCode(phone);
            if("".equals(jsonString))
                return ResultDTO.errorOf(CustomizeErrorCode.SEND_PHONE_FAILED);
            JSONObject obj = JSONObject.parseObject(jsonString);
            String msg_id = obj.getString("msg_id");
            if(msg_id!=null)
                return ResultDTO.okOf(msg_id);
            String error = obj.getString("error");
            if(error!=null){
                JSONObject subObj = JSONObject.parseObject(error);
                String message = obj.getString("message");
                return ResultDTO.errorOf(message);
            }
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            System.out.println(e.getMessage());

        }
        return ResultDTO.errorOf(CustomizeErrorCode.SEND_PHONE_FAILED);
    }

    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/phone/ValidCode", method = RequestMethod.POST)
    public Object validCode(@RequestParam("msg_id") String msg_id,
                            @RequestParam("code") String code,
                            @RequestParam("phone") String phone,
                            @RequestParam("state") String state,
                            @RequestParam(name = "password", required = false) String password,
                            HttpServletRequest request,
                            HttpServletResponse response){
        //  System.out.println("mail:"+mail);
        // TODO 自动生成的方法存根
        try {
            String jsonString = JiGuangProvider.testSendValidSMSCode(msg_id,code);
            if("".equals(jsonString))
                return ResultDTO.errorOf(CustomizeErrorCode.PHONE_CODE_ERROR);
            JSONObject obj = JSONObject.parseObject(jsonString);
         //   String is_valid = obj.getString("is_valid");
            Boolean is_valid = obj.getBoolean("is_valid");
            if(is_valid){
                if("1".equals(state)){//绑定
                UserDTO user = (UserDTO) request.getAttribute("loginUser");
                Long id = user.getId();
                return userService.updateUserPhoneById(id,phone);
                }
                if("2".equals(state)){//注册、登录
                    ResultDTO resultDTO = (ResultDTO)userService.registerOrLoginWithPhone(phone,password);
                    if(200==resultDTO.getCode()){
                        Cookie cookie = cookieUtils.getCookie("token",resultDTO.getMessage(),86400*3);
                        response.addCookie(cookie);
                    }
                    return resultDTO;
                }

            }
         else{
              String error = obj.getString("error");
                JSONObject subObj = JSONObject.parseObject(error);
                String message = obj.getString("message");
                return ResultDTO.errorOf(message);
            }
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            System.out.println(e.getMessage());

        }
        return ResultDTO.errorOf(CustomizeErrorCode.PHONE_CODE_ERROR);
    }

    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/api/phone/loginTokenVerify", method = RequestMethod.POST)
    public Object loginTokenVerify(@RequestParam("loginToken") String loginToken,
                            HttpServletRequest request,
                            HttpServletResponse response){
        //  System.out.println("mail:"+mail);
        // TODO 自动生成的方法存根
        LoginTokenVerifyDto ltvd = new LoginTokenVerifyDto(loginToken,"0");
        ResultDTO resultDTO = (ResultDTO)JiGuangProvider.loginTokenVerify(ltvd);
        if(200==resultDTO.getCode()){
            resultDTO = (ResultDTO)userService.registerOrLoginWithPhone(resultDTO.getMessage(),null);
        }
        return resultDTO;
        //return ResultDTO.errorOf(CustomizeErrorCode.PHONE_CODE_ERROR);
    }

    @Data
    @AllArgsConstructor//有参构造
    class LoginTokenVerifyDto{
        String loginToken;
        String exID;
    }


}
