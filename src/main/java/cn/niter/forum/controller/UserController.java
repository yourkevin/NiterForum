package cn.niter.forum.controller;

import cn.niter.forum.dto.PaginationDTO;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import cn.niter.forum.model.User;
import cn.niter.forum.model.UserAccount;
import cn.niter.forum.model.UserInfo;
import cn.niter.forum.service.QuestionService;
import cn.niter.forum.service.UserAccountService;
import cn.niter.forum.service.UserInfoService;
import cn.niter.forum.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserInfoService userInfoService;
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
            UserInfo userInfo = userInfoService.selectByUserId(user.getId());
            model.addAttribute("user", user);
            model.addAttribute("userAccount", userAccount);
            model.addAttribute("userInfo", userInfo);
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
    @ResponseBody
    public Map<String,Object> set(HttpServletRequest request,
                                  @PathVariable(name = "action") String action
            , @RequestParam(name = "avatar",defaultValue = "https://niter.cn/images/default-avatar.png")String avatar
            , @RequestParam(name = "json",required = false) String json
            , @RequestParam(name = "data",required = false) String data
                      ){
        User user = (User)request.getSession().getAttribute("user");

        if(user==null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        Map<String,Object> map  = new HashMap<>();

        if("avatar".equals(action)){
             //System.out.println(avatar);
            int i = userService.updateAvatarById(user.getId(),avatar);
            if(i==1) {
                map.put("code",200);
                map.put("msg","恭喜您，头像修改成功！！！");
            }
            else{
                map.put("code",500);
                map.put("msg","妈呀，出问题啦！");
            }
        }

        if("info".equals(action)){
            System.out.println("json:"+json);
            UserInfo userInfo = JSON.parseObject(json, UserInfo.class);
            JSONObject obj=JSON.parseObject(json);
            userInfo.setLocation(obj.getString("P1")+"-"+obj.getString("C1")+"-"+obj.getString("A1"));
            String[] birthday = userInfo.getBirthday().split("-");
            if(birthday.length==3) {
                String  constellation = userInfoService.getConstellation(Integer.parseInt(birthday[1]), Integer.parseInt(birthday[2]));
                userInfo.setConstellation(constellation);
            }
            int i = userInfoService.updateByUserId(userInfo,user.getId());
            int j = userService.updateUsernameById(user.getId(),obj.getString("username"));

            if(j!=1){
                map.put("code",500);
                map.put("msg","妈呀，昵称修改失败啦！");
            }
            else if(i!=1) {
                map.put("code",500);
                map.put("msg","妈呀，资料修改失败啦！");
            }
            else{
                map.put("code",200);
                map.put("msg","恭喜您，资料修改成功！！！");
            }
        }

        return map;
    }



}
