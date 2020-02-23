package cn.niter.forum.controller;


import cn.niter.forum.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SSOController {
    @Value("${vaptcha.vid}")
    private String vaptcha_vid;

    @RequestMapping("/sso/{action}")
    public String aouth(HttpServletRequest request,
                         HttpServletResponse response,
                        @PathVariable(name = "action") String action,
                        Model model) {
       // System.out.println("请求"+request.getAttribute("isOk"));
       // if(isOk==null) return "redirect:/";
        //System.out.println(isOk);
        User user = (User) request.getSession().getAttribute("user");
        if(user != null) {
            return "redirect:/";
        }
        model.addAttribute("vaptcha_vid", vaptcha_vid);
        if("login".equals(action)){
            model.addAttribute("section", "login");
            model.addAttribute("sectionName", "登录");
           // return "/user/login";
        }
        else if("register".equals(action)){
            model.addAttribute("section", "register");
            model.addAttribute("sectionName", "注册");
          //  return "/user/reg";
        }
        else {
            return "redirect:/";
        }
       return "user/sso";
    }
}
