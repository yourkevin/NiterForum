package cn.niter.forum.controller;

import cn.niter.forum.dto.*;
import cn.niter.forum.enums.CommentTypeEnum;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import cn.niter.forum.model.Comment;
import cn.niter.forum.model.UserAccount;
import cn.niter.forum.provider.BaiduCloudProvider;
import cn.niter.forum.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wadao
 * @version 2.0
 * @date 2020/5/1 15:17
 * @site niter.cn
 */

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private BaiduCloudProvider baiduCloudProvider;

    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,//@RequestBody接受json格式的数据
                       HttpServletRequest request) {

        UserDTO user = (UserDTO) request.getAttribute("loginUser");

        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        ResultDTO resultDTO = baiduCloudProvider.getTextCensorReult(commentCreateDTO.getContent());
        if(resultDTO.getCode()!=1){
            return resultDTO;
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        UserAccount userAccount = new UserAccount();
        userAccount.setVipRank(user.getVipRank());
        userAccount.setGroupId(user.getGroupId());
        userAccount.setUserId(user.getId());
        commentService.insert(comment,user,userAccount);

       return ResultDTO.okOf("回复成功！");

    }


    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id) {
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }

    @ResponseBody
    @RequestMapping(value = "/api/comment/list", method = RequestMethod.POST)
    public ResultDTO<List<CommentDTO>> commentList(
            /*@RequestParam(name = "commentator",required = false) Long commentator
            ,@RequestParam(name = "type",required = false) Integer type
            ,@RequestParam(name = "id",required = false) Long id
            ,@RequestParam(name = "parentId",required = false) Long parentId*/
            @RequestBody CommentQueryDTO commentQueryDTO
            ) {
        PaginationDTO paginationDTO = commentService.listByCommentQueryDTO(commentQueryDTO);


        return ResultDTO.okOf(paginationDTO);
    }

    @PostMapping("/comment/del/id")
    @ResponseBody
    public Map<String,Object> deleteCommentById(HttpServletRequest request,
                                                     @RequestParam(name = "id",defaultValue = "0") Long id
                                                     ,@RequestParam(name = "type",defaultValue = "0") Integer type ) {

        UserDTO user = (UserDTO) request.getAttribute("loginUser");
        UserAccount userAccount = new UserAccount();
        userAccount.setVipRank(user.getVipRank());
        userAccount.setGroupId(user.getGroupId());
        userAccount.setUserId(user.getId());
        if (user == null||userAccount==null) {
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        Map<String,Object> map  = new HashMap<>();
        if(id==null||id==0||type==null||type==0) {
            map.put("code",500);
            map.put("msg","妈呀，出问题啦！");
        }
        else {
            int c = commentService.delCommentByIdAndType(user.getId(),userAccount.getGroupId(),id,type);
            if(c==0) {
                map.put("code",500);
                map.put("msg","哎呀，该评论已删除或您无权删除！");
            }
            else {
                map.put("code",200);
                map.put("msg","恭喜您，成功删除"+c+"条评论及子评论！");
            }
        }
        return map;
    }


}
