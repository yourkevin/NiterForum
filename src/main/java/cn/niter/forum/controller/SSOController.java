package cn.niter.forum.controller;


import cn.niter.forum.cache.AppUserCache;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import cn.niter.forum.model.User;
import cn.niter.forum.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SSOController {
    @Value("${vaptcha.vid}")
    private String vaptcha_vid;

    @Autowired
    private AppUserCache appUserCache;

    @Autowired
    private UserService userService;

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

    @RequestMapping("/sso/appConfirm")
    public String qrcodeStr(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestParam(name = "qrcodeStr") String qrcodeStr,
                        Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
            model.addAttribute("qrcodeStr", qrcodeStr);
           // System.out.println("qrcodeStr:"+qrcodeStr);
            return "user/appConfirm";
    }

    @PostMapping("/sso/putQrcodeStr")
    @ResponseBody
    public Map<String,Object> putQrcodeStr(HttpServletRequest request,
                                                   @RequestParam(name = "qrcodeStr") String qrcodeStr) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }

        Map<String,Object> map  = new HashMap<>();
        user=userService.selectUserByUserId(user.getId());
        String token = user.getToken();
        //System.out.println("token:"+token);
        appUserCache.put(qrcodeStr,token);
       // System.out.println("cachetoken:"+appUserCache.get(qrcodeStr));
        map.put("success",1);
        return map;

    }

    @GetMapping("/sso/appConfirmResult")
    @ResponseBody
    public Map<String,Object> appConfirmResult(HttpServletRequest request,
                                           @RequestParam(name = "qrcodeStr") String qrcodeStr) {

         //System.out.println("qrcodeStr2:"+qrcodeStr);
        Map<String,Object> map  = new HashMap<>();
        String token = appUserCache.get(qrcodeStr);
        if(token==null||"".equals(token))
                  map.put("success",0);
        else{
            map.put("success",1);
            map.put("token",token);
            System.out.println("token2:"+token);
        }
        return map;

    }





}
