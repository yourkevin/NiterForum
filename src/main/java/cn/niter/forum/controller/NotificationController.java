package cn.niter.forum.controller;

import cn.niter.forum.dto.NotificationDTO;
import cn.niter.forum.enums.NotificationTypeEnum;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import cn.niter.forum.model.User;
import cn.niter.forum.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    public String readNotification(HttpServletRequest request,
                          @PathVariable(name = "id") Long id) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        NotificationDTO notificationDTO = notificationService.read(id, user);

        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()
                || NotificationTypeEnum.REPLY_SUB_COMMENT.getType() == notificationDTO.getType()
                || NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()
                || NotificationTypeEnum.LIKE_COMMENT.getType() == notificationDTO.getType()
                || NotificationTypeEnum.LIKE_QUESTION.getType() == notificationDTO.getType()) {
            return "redirect:/p/" + notificationDTO.getOuterid();
        } else {
            return "redirect:/";
        }
    }


    @PostMapping("/notification/removeAll")
    @ResponseBody
    public Map<String,Object> removeAllNotifications(HttpServletRequest request,
                                                     @RequestParam(name = "all",defaultValue = "0") Integer all) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        Map<String,Object> map  = new HashMap<>();
        if(all==null||all!=1) {
            map.put("code",500);
            map.put("msg","妈呀，出问题啦！");
        }
        else if(all==1){
            map.put("code",200);
            int c = notificationService.removeAllByUserId(user.getId());
            if(c==0) map.put("msg","哎呀，您都已经全部删除了呀！");
            else map.put("msg","恭喜您，成功删除"+c+"条消息！");
        }
        return map;

    }


    @PostMapping("/notification/readAll")
    @ResponseBody
    public Map<String,Object> readAllNotifications(HttpServletRequest request,
                                                     @RequestParam(name = "all",defaultValue = "0") Integer all) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        Map<String,Object> map  = new HashMap<>();
        if(all==null||all!=1) {
            map.put("code",500);
            map.put("msg","妈呀，出问题啦！");
        }
        else if(all==1){
            map.put("code",200);
            int c = notificationService.readAllByUserId(user.getId());
            if(c==0) map.put("msg","哎呀，您都已经全部已读了呀！");
            else map.put("msg","恭喜您，成功已读"+c+"条消息！");
        }
        return map;

    }




}

