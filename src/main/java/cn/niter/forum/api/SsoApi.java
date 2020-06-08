package cn.niter.forum.api;

import cn.niter.forum.annotation.UserLoginToken;
import cn.niter.forum.cache.TemporaryCache;
import cn.niter.forum.dto.ResultDTO;
import cn.niter.forum.service.UserService;
import cn.niter.forum.util.CookieUtils;
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
 * @version 1.0
 * @date 2020/5/31 21:06
 * @site niter.cn
 */
@Controller
@RequestMapping("/api/sso")
public class SsoApi {

    @Autowired
    private CookieUtils cookieUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private TemporaryCache temporaryCache;

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


}
