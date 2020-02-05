package cn.niter.forum.controller;

import cn.niter.forum.dto.PaginationDTO;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import cn.niter.forum.model.User;
import cn.niter.forum.model.UserAccount;
import cn.niter.forum.service.QuestionService;
import cn.niter.forum.service.UserAccountService;
import cn.niter.forum.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAccountService userAccountService;


    @GetMapping("/user/{userId}")
    public String getUserHome(@PathVariable(name="userId",required = false) String userId,
                              @RequestParam(value = "page", defaultValue = "1") Integer page,
                              @RequestParam(value = "size", defaultValue = "12") Integer size,
                              Model model,
                              HttpServletRequest request) {
        //System.out.println("用户id为："+userId);
        if (userId == null|| StringUtils.isBlank(userId)) {
            System.out.println("用户为空!");
            return "redirect:/";
        }
        User user = userService.selectUserByUserId(userId);
        if (user != null) {
            UserAccount userAccount = userAccountService.selectUserAccountByUserId(user.getId());
            model.addAttribute("user", user);
            model.addAttribute("userAccount", userAccount);
            PaginationDTO paginationDTO = questionService.listByUserId(user.getId(), page, size);
            model.addAttribute("paginationDto", paginationDTO);
            return "user/home";
        }
        else {
            model.addAttribute("errorCode", CustomizeErrorCode.USER_IS_EMPTY.getCode());
            model.addAttribute("message",CustomizeErrorCode.USER_IS_EMPTY.getMessage());
            return "error";
        }
        //return "user/home";
    }



    @PostMapping("/user/set/{action}")
    public String set(HttpServletRequest request,
                          @PathVariable(name = "action") String action,
                          //Model model,
                          @RequestParam(name = "avatar",defaultValue = "https://niter.cn/images/default-avatar.png")String avatar
                          ){
        User user = (User)request.getSession().getAttribute("user");

        if(user==null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }

        if("avatar".equals(action)){
             //System.out.println(avatar);
            userService.updateAvatarById(user.getId(),avatar);
        }

        return "user/set";
    }



}
