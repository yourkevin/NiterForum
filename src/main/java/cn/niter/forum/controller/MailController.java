package cn.niter.forum.controller;

import cn.niter.forum.cache.IpLimitCache;
import cn.niter.forum.dto.ResultDTO;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import cn.niter.forum.model.User;
import cn.niter.forum.service.UserService;
import cn.niter.forum.util.JavaMailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class MailController {

    @Autowired
    private UserService userService;
    @Autowired
    private IpLimitCache ipLimitCache;

    @Value("${site.main.title}")
    private String siteTitle;

    @GetMapping("/profile/regmail")
    public String hello(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }

        return "user/regmail";
    }


    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/mail/getMailCode", method = RequestMethod.POST)
    public Object getMailCode(@RequestParam(name="username",required = false) String username,
                              @RequestParam("mail") String mail,
                              @RequestParam("ip") String ip,
                              @RequestParam("token") String token){
        if((!token.equals(ipLimitCache.getInterval(ip)))||(ipLimitCache.showIpScores(ip)>=100)){
            ipLimitCache.addIpScores(ip,10);
            return ResultDTO.errorOf(CustomizeErrorCode.SEND_MAIL_FAILED);
        }
        /*System.out.println("token"+ipLimitCache.get(ip));
        System.out.println("ExpectedExpiration:"+ipLimitCache.getInterval().getExpectedExpiration(ip));
        System.out.println("Expiration:"+ipLimitCache.getInterval().getExpiration(ip));
        System.out.println("ip:"+ip+"-token:"+token);*/
        // TODO 自动生成的方法存根
       try {
            if(username==null) username=siteTitle;
            JavaMailUtils.setUserName(username);
            JavaMailUtils.setReceiveMailAccount(mail);
            JavaMailUtils.send();
            return ResultDTO.okOf(JavaMailUtils.code);
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            System.out.println(e.getMessage());
            return ResultDTO.errorOf(CustomizeErrorCode.SEND_MAIL_FAILED);
        }


    }

    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/mail/submitMail", method = RequestMethod.GET)
    public Object submitMail(@RequestParam("id") String id,
                              @RequestParam("mail") String mail){
        //  System.out.println("mail:"+mail);
        // TODO 自动生成的方法存根
           return userService.updateUserMailById(id,mail);
    }

    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/mail/registerOrLoginWithMail", method = RequestMethod.GET)
    public Object registerOrLoginWithMail(
                             @RequestParam("mail") String mail,
                                          HttpServletResponse response){
        //  System.out.println("mail:"+mail);
        String token = UUID.randomUUID().toString();
      //  Cookie cookie = new Cookie("token", token);
       // cookie.setMaxAge(60 * 60 * 24 * 30 * 6);
      //  response.addCookie(cookie);
        // TODO 自动生成的方法存根
        return userService.registerOrLoginWithMail(mail,token);
    }

    @RequestMapping(value = "/registerorLoginWithMailisOk", method = RequestMethod.GET)
    public String returnToken(Model model,
                              @RequestParam(name = "token")String token,
                              HttpServletResponse response){
        //  System.out.println("mail:"+mail);
        // TODO 自动生成的方法存根
        model.addAttribute("rsTitle", "成功啦！！！");
        model.addAttribute("rsMessage", "您已成功注册/登陆本站！");
        Cookie cookie = new Cookie("token", token);
        cookie.setSecure(true);   //服务只能通过https来进行cookie的传递，使用http服务无法提供服务。
        cookie.setHttpOnly(true);//通过js脚本是无法获取到cookie的信息的。防止XSS攻击。
        cookie.setMaxAge(60 * 60 * 24 * 3 * 1);//三天
        response.addCookie(cookie);
        return "result";
    }



}
