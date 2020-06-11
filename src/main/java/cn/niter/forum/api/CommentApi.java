package cn.niter.forum.api;

import cn.niter.forum.annotation.UserLoginToken;
import cn.niter.forum.cache.IpLimitCache;
import cn.niter.forum.dto.CommentCreateDTO;
import cn.niter.forum.dto.ResultDTO;
import cn.niter.forum.dto.UserDTO;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.model.Comment;
import cn.niter.forum.model.UserAccount;
import cn.niter.forum.provider.BaiduCloudProvider;
import cn.niter.forum.service.CommentService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author wadao
 * @version 1.0
 * @date 2020/6/5 17:21
 * @site niter.cn
 */

@Controller
@RequestMapping("/api/comment")
@Api(tags={"评论接口"})
public class CommentApi {


    @Autowired
    private CommentService commentService;
    @Autowired
    private BaiduCloudProvider baiduCloudProvider;
    @Autowired
    private IpLimitCache ipLimitCache;

    @UserLoginToken
    @ApiOperation(value = "新增评论",notes = "只有当用户登录后才能访问此接口，否则会返回401错误")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commentCreateDTO", value = "评论基本信息", dataType = "CommentCreateDTO")
    })
    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Object post(@RequestBody @Valid CommentCreateDTO commentCreateDTO,//@RequestBody接受json格式的数据
                       HttpServletRequest request
                       //     , BindingResult results
    ) {

        UserDTO user = (UserDTO) request.getAttribute("loginUser");
       /* if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }*/
     /*   System.out.println("到这里了"+results.toString());
        if (results.hasErrors()) {
            return ResultDTO.errorOf("参数为空");
        }*/
       /* if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }*/
        if(commentCreateDTO.getType()==1&&(!commentCreateDTO.getToken().equals(ipLimitCache.getInterval(commentCreateDTO.getIp())))){
            return ResultDTO.errorOf(CustomizeErrorCode.TOKEN_ERROR);
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
}
