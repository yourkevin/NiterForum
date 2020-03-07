package cn.niter.forum.controller;

import cn.niter.forum.dto.PaginationDTO;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import cn.niter.forum.model.User;
import cn.niter.forum.service.NotificationService;
import cn.niter.forum.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {


    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @Value("${vaptcha.vid}")
    private String vaptcha_vid;

    @GetMapping("/user/message")
    public String messeage(HttpServletRequest request,
                          Model model,
                          @RequestParam(name = "page",defaultValue = "1")Integer page,
                          @RequestParam(name = "size",defaultValue = "5")Integer size){
        User user = (User)request.getSession().getAttribute("user");

        if(user==null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            model.addAttribute("section", "message");
            model.addAttribute("pagination", paginationDTO);
           // model.addAttribute("sectionName", "最新通知");
            model.addAttribute("navtype", "notifynav");

        return "user/message";
    }

    @GetMapping("/user/p/{action}")
    public String p(HttpServletRequest request,
                    @PathVariable(name = "action") String action,
                    Model model,
                    @RequestParam(name = "page",defaultValue = "1")Integer page,
                    @RequestParam(name = "size",defaultValue = "10")Integer size){
        User user = (User)request.getSession().getAttribute("user");

        if(user==null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        if("myPosts".equals(action)){
            model.addAttribute("section", "myPosts");
            model.addAttribute("sectionName", "我的帖子");
            PaginationDTO pagination = questionService.listByUserId(user.getId(), page, size);
            model.addAttribute("pagination",pagination);
            model.addAttribute("navtype", "communitynav");
        }
        if("likes".equals(action)){
            PaginationDTO paginationDTO = questionService.listByExample(user.getId(), page, size,"likes");
            model.addAttribute("section", "likes");
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "我的收藏");
            model.addAttribute("navtype", "communitynav");
        }

        return "user/p";
    }


    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(name = "page",defaultValue = "1")Integer page,
                          @RequestParam(name = "size",defaultValue = "5")Integer size){
      User user = (User)request.getSession().getAttribute("user");

        if(user==null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }

        if("questions".equals(action)){
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            PaginationDTO pagination = questionService.listByUserId(user.getId(), page, size);
            model.addAttribute("pagination",pagination);
            model.addAttribute("navtype", "communitynav");
        }
        if("notifies".equals(action)){
            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            model.addAttribute("section", "notifies");
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "最新通知");
            model.addAttribute("navtype", "notifynav");
        }
       if("likes".equals(action)){
            PaginationDTO paginationDTO = questionService.listByExample(user.getId(), page, size,"likes");
            model.addAttribute("section", "likes");
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "我的收藏");
            model.addAttribute("navtype", "communitynav");
        }

        return "profile";
    }


    @GetMapping("/user/set/{action}")
    public String getSetPage(HttpServletRequest request,
                                 @PathVariable(name = "action") String action,
                                 Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null) {
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        if("info".equals(action)|| StringUtils.isBlank(action)){
            model.addAttribute("section", "info");
            model.addAttribute("sectionName", "我的资料");
            model.addAttribute("sectionInfo", "绑定邮箱账号后，您可以使用邮箱账号登录本站，也可用邮箱账号找回密码\n");
            model.addAttribute("navtype", "communitynav");
            return "user/set";
        }
        if("account".equals(action)){
            model.addAttribute("section", "account");
            model.addAttribute("sectionName", "绑定/更新邮箱账号");
            model.addAttribute("sectionInfo", "绑定邮箱账号后，您可以使用邮箱账号登录本站，也可用邮箱账号找回密码\n");
            model.addAttribute("navtype", "communitynav");
            model.addAttribute("vaptcha_vid", vaptcha_vid);
            return "user/account";
        }
        return "user/set";
    }



}
