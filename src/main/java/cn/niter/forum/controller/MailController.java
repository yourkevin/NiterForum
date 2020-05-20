package cn.niter.forum.controller;

import cn.niter.forum.cache.IpLimitCache;
import cn.niter.forum.cache.TemporaryCache;
import cn.niter.forum.dto.ResultDTO;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.service.UserService;
import cn.niter.forum.util.CookieUtils;
import cn.niter.forum.util.JavaMailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wadao
 * @version 2.0
 * @date 2020/5/1 16:17
 * @site niter.cn
 */

@Controller
public class MailController {

    @Autowired
    private UserService userService;
    @Autowired
    private IpLimitCache ipLimitCache;
    @Autowired
    private TemporaryCache temporaryCache;

    @Value("${site.main.title}")
    private String siteTitle;
    @Value("${site.main.domain}")
    private String domain;
    @Autowired
    private CookieUtils cookieUtils;

   /* @GetMapping("/profile/regmail")
    public String hello(HttpServletRequest request) {
        UserDTO user = (UserDTO) request.getAttribute("loginUser");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }

        return "user/regmail";
    }*/


    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/mail/getMailCode", method = RequestMethod.POST)
    public Object getMailCode(@RequestParam(name = "username", required = false) String username,
                              @RequestParam("mail") String mail,
                              @RequestParam("ip") String ip,
                              @RequestParam("token") String token) {
        if ((!token.equals(ipLimitCache.getInterval(ip))) || (ipLimitCache.showIpScores(ip) >= 100)) {
            ipLimitCache.addIpScores(ip, 10);
            return ResultDTO.errorOf(CustomizeErrorCode.SEND_MAIL_FAILED);
        }
        /*System.out.println("token"+ipLimitCache.get(ip));
        System.out.println("ExpectedExpiration:"+ipLimitCache.getInterval().getExpectedExpiration(ip));
        System.out.println("Expiration:"+ipLimitCache.getInterval().getExpiration(ip));
        System.out.println("ip:"+ip+"-token:"+token);*/
        // TODO 自动生成的方法存根
        try {
            if (username == null) username = siteTitle;
            JavaMailUtils.setUserName(username);
            JavaMailUtils.setReceiveMailAccount(mail);
            JavaMailUtils.send();
            temporaryCache.putMailCode(mail, JavaMailUtils.code);
            return ResultDTO.okOf("邮箱验证码已发送成功！");
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            System.out.println(e.getMessage());
            return ResultDTO.errorOf(CustomizeErrorCode.SEND_MAIL_FAILED);
        }


    }

    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/mail/submitMail", method = RequestMethod.POST)
    public Object submitMail(@RequestParam("id") String id,
                             @RequestParam("mail") String mail,
                             @RequestParam("code") String code) {
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
        ResultDTO resultDTO = (ResultDTO)userService.registerOrLoginWithMail(mail,password);
        if(200==resultDTO.getCode()){
            Cookie cookie = cookieUtils.getCookie("token",resultDTO.getMessage(),86400*3);
            response.addCookie(cookie);
        }
        // TODO 自动生成的方法存根
        return resultDTO;
    }

    @RequestMapping(value = "/registerorLoginWithMailisOk", method = RequestMethod.GET)
    public String returnToken(Model model,
                              @RequestParam(name = "token") String token,
                              HttpServletResponse response) {
        //  System.out.println("mail:"+mail);
        // TODO 自动生成的方法存根
        model.addAttribute("rsTitle", "成功啦！！！");
        model.addAttribute("rsMessage", "您已成功注册/登陆本站！");
        Cookie cookie = cookieUtils.getCookie("token",token,86400*3);
        response.addCookie(cookie);
        return "result";
    }

}
