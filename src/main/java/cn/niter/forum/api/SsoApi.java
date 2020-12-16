package cn.niter.forum.api;

import cn.niter.forum.annotation.UserLoginToken;
import cn.niter.forum.cache.IpLimitCache;
import cn.niter.forum.cache.TemporaryCache;
import cn.niter.forum.dto.ResultDTO;
import cn.niter.forum.dto.UserDTO;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.provider.JiGuangProvider;
import cn.niter.forum.provider.QCloudProvider;
import cn.niter.forum.service.UserService;
import cn.niter.forum.util.CookieUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wadao
 * @version 1.0
 * @date 2020/5/31 21:06
 * @site niter.cn
 */
@Controller
@RequestMapping("/api/sso")
@ApiIgnore
public class SsoApi {
    @Autowired
    private IpLimitCache ipLimitCache;
    @Autowired
    private CookieUtils cookieUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private TemporaryCache temporaryCache;
    @Autowired
    private QCloudProvider qCloudProvider;


    @Value("${sms.enable}")
    private Integer smsEnable;
    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object login(HttpServletRequest request,
                        @RequestParam("name") String name,
                        @RequestParam("password") String password,
                        @RequestParam("type") Integer type,
                        HttpServletResponse response) {
        //1为手机号，2为邮箱号
        ResultDTO resultDTO = (ResultDTO)userService.login(type,name,password);
        if(200==resultDTO.getCode()){
            Cookie cookie = cookieUtils.getCookie("token",""+resultDTO.getData(),86400*3);
            response.addCookie(cookie);
        }
        return resultDTO;
    }

    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Object register(HttpServletRequest request,
                        @RequestParam("name") String name,
                        @RequestParam("password") String password,
                        @RequestParam("type") Integer type,
                        HttpServletResponse response) {
        //1为手机号，2为邮箱号
        ResultDTO resultDTO = (ResultDTO)userService.register(type,name,password);
        if(200==resultDTO.getCode()){
            Cookie cookie = cookieUtils.getCookie("token",""+resultDTO.getData(),86400*3);
            response.addCookie(cookie);
        }
        return resultDTO;
    }

    @UserLoginToken
    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/mail/submitMail", method = RequestMethod.POST)
    public Object submitMail(@RequestParam("id") String id,
                             @RequestParam("mail") String mail,
                             @RequestParam("code") String code) {
        System.out.println("mail code"+mail+code);
        if (!code.equals(temporaryCache.getMailCode(mail)))
            return ResultDTO.errorOf("验证码不匹配，可能已经超过5分钟，请重试");        // TODO 自动生成的方法存根
        return userService.updateUserMailById(id, mail);
    }

    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/mail/registerOrLoginWithMail", method = RequestMethod.POST)
    public Object registerOrLoginWithMail(
            @RequestParam("mail") String mail,
            @RequestParam("code") String code,
            @RequestParam(name = "password", required = false) String password,
            HttpServletResponse response) {
        if (!code.equals(temporaryCache.getMailCode(mail)))
            return ResultDTO.errorOf("验证码不匹配，可能已经超过5分钟，请重试");
        if("null".equals(password)) password=null;
        ResultDTO resultDTO = (ResultDTO)userService.registerOrLoginWithMail(mail,password);
        if(200==resultDTO.getCode()){
            Cookie cookie = cookieUtils.getCookie("token",resultDTO.getMessage(),86400*3);
            response.addCookie(cookie);
        }
        // TODO 自动生成的方法存根
        return resultDTO;
    }

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
            String jsonString = null ;
            if(smsEnable==1){//极光短信返回结果
                jsonString = JiGuangProvider.testSendSMSCode(phone);
            }else if(smsEnable==2){//腾讯短信返回结果
                jsonString = qCloudProvider.sendSms("用户",phone);
            }
            if(StringUtils.isBlank(jsonString))
                return ResultDTO.errorOf(CustomizeErrorCode.SEND_PHONE_FAILED);
            JSONObject obj = JSONObject.parseObject(jsonString);
            String msg_id = obj.getString(smsEnable==1?"msg_id":"SerialNo");
            if(StringUtils.isNotBlank(msg_id))
                return ResultDTO.okOf(msg_id);
            String error = obj.getString("error");
            String Code = obj.getString("Code");
            if(StringUtils.isNotBlank(error)||!"Ok".equals(Code)){
                return ResultDTO.errorOf(obj.getString(smsEnable==1?"message":"Message"));
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
            Boolean is_valid=false;
            JSONObject jiGuangObj;
            if(smsEnable==1){//开启极光短信
                String jsonString = JiGuangProvider.testSendValidSMSCode(msg_id,code);
                if(StringUtils.isBlank(jsonString))
                    return ResultDTO.errorOf(CustomizeErrorCode.PHONE_CODE_ERROR);
                jiGuangObj = JSONObject.parseObject(jsonString);
                is_valid = jiGuangObj.getBoolean("is_valid");
            }else if(smsEnable==2){//开启腾讯短信
                is_valid = ("+86"+phone+code).equals(temporaryCache.getPhoneCode(msg_id));
            }
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
                return ResultDTO.errorOf("验证码不匹配");
            }
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            System.out.println(e.getMessage());

        }
        return ResultDTO.errorOf(CustomizeErrorCode.PHONE_CODE_ERROR);
    }


}
