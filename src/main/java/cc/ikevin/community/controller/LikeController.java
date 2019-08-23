package cc.ikevin.community.controller;

import cc.ikevin.community.dto.ResultDTO;
import cc.ikevin.community.dto.ThumbCreateDTO;
import cc.ikevin.community.exception.CustomizeErrorCode;
import cc.ikevin.community.model.Thumb;
import cc.ikevin.community.model.User;
import cc.ikevin.community.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public Object like(@RequestBody ThumbCreateDTO thumbCreateDTO,//@RequestBody接受json格式的数据
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (thumbCreateDTO == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.CAN_NOT_LIKE);
        }


        Thumb thumb = new Thumb();
        thumb.setTargetId(thumbCreateDTO.getTargetId());
        thumb.setType(thumbCreateDTO.getType());
        thumb.setGmtModified(System.currentTimeMillis());
        thumb.setGmtCreate(System.currentTimeMillis());
        thumb.setLiker(user.getId());
        int result = likeService.insert(thumb,user);
      //System.out.println(thumb+"r"+result);
        if(result == 0&&thumbCreateDTO.getType()==2)
        return ResultDTO.okOf("点赞成功！");
        if(result == 0&&thumbCreateDTO.getType()==1)
            return ResultDTO.okOf("收藏成功！");
        if(result == 2022||thumbCreateDTO.getType()==2)
        return ResultDTO.errorOf(CustomizeErrorCode.CAN_NOT_LIKE_AGAIN);
        if(result == 2023||thumbCreateDTO.getType()==1)
            return ResultDTO.errorOf(CustomizeErrorCode.CAN_NOT_LIKE_QUESTION_AGAIN);




        return ResultDTO.errorOf(CustomizeErrorCode.CAN_NOT_LIKE);
    }


}
