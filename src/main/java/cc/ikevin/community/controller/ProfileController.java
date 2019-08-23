package cc.ikevin.community.controller;

import cc.ikevin.community.dto.PaginationDTO;
import cc.ikevin.community.exception.CustomizeErrorCode;
import cc.ikevin.community.exception.CustomizeException;
import cc.ikevin.community.model.User;
import cc.ikevin.community.service.NotificationService;
import cc.ikevin.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          Model model,
                          @RequestParam(name = "page",defaultValue = "1")Integer page,
                          @RequestParam(name = "size",defaultValue = "5")Integer size){
      /*  User user=null;
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length!=0){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){
                    String token=cookie.getValue();
                    user = userMapper.findByToken(token);
                    if(user!=null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }*/
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
}
