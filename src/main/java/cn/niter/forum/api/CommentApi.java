package cn.niter.forum.api;

import cn.niter.forum.constant.PageConstant;
import cn.niter.forum.annotation.UserLoginToken;
import cn.niter.forum.cache.IpLimitCache;
import cn.niter.forum.dto.CommentDTO;
import cn.niter.forum.dto.CommentQueryDTO;
import cn.niter.forum.dto.ResultDTO;
import cn.niter.forum.dto.UserDTO;
import cn.niter.forum.enums.CommentTypeEnum;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.provider.BaiduCloudProvider;
import cn.niter.forum.service.CommentService;
import cn.niter.forum.vo.CommentInsertVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

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
            @ApiImplicitParam(name = "commentInsertVO", value = "评论基本信息", dataType = "CommentInsertVO")
    })
    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Object insert(@RequestBody @Valid CommentInsertVO commentInsertVO,//@RequestBody接受json格式的数据
                         HttpServletRequest request
                         //     , BindingResult results
    ) {
        UserDTO userDTO = (UserDTO) request.getAttribute("loginUser");
        if(commentInsertVO.getType()==1&&(!commentInsertVO.getToken().equals(ipLimitCache.getInterval(commentInsertVO.getIp())))){
            return ResultDTO.errorOf(CustomizeErrorCode.TOKEN_ERROR);
        }
        ResultDTO resultDTO = baiduCloudProvider.getTextCensorReult(commentInsertVO.getContent());
        if(resultDTO.getCode()!=1){
            return resultDTO;
        }
        CommentDTO commentDTO = new CommentDTO();
        BeanUtils.copyProperties(commentInsertVO,commentDTO);
        commentDTO.setGmtCreate(System.currentTimeMillis());
        commentDTO.setGmtModified(commentDTO.getGmtCreate());
        commentDTO.setCommentator(userDTO.getId());
        commentService.insert(commentDTO,userDTO);
        return ResultDTO.okOf("回复成功！");
    }

    @UserLoginToken
    @ApiOperation(value = "删除评论",notes = "只有当作者和管理登录后才能访问此接口")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object deleteByIdAndType(HttpServletRequest request,
                                                @RequestParam(name = "id",defaultValue = "0") Long id
            , @RequestParam(name = "type",defaultValue = "0") Integer type ) {

        UserDTO userDTO = (UserDTO) request.getAttribute("loginUser");
        if(id==null||id==0||type==null||type==0) {
                 return ResultDTO.errorOf("访问异常");
        }
        else {
            int c = commentService.deleteByIdAndType(userDTO,id,type);
            if(c==0) {
                return ResultDTO.errorOf("哎呀，该评论已删除或您无权删除！");
            }
            else {
                return ResultDTO.okOf("恭喜您，成功删除"+c+"条评论及子评论！");
            }
        }
        //return map;
    }

    @ApiOperation(value = "查询子评论",notes = "根据目标id一次性获取全部子评论。此为历史遗留接口，正式环境请使用以下接口")
    @ResponseBody
    @GetMapping(value = "/list/{id}")
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id) {
        //List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentService.listByTargetId(id, CommentTypeEnum.COMMENT));
    }


    @ApiOperation(value = "查询评论",notes = "根据查询条件来获取评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评论id",dataType = "Long")
            ,@ApiImplicitParam(name = "parent_id", value = "评论目标id",dataType = "Long")
            ,@ApiImplicitParam(name = "type", value = "评论类型",dataType = "Integer")
            ,@ApiImplicitParam(name = "commentator", value = "评论者",dataType = "Long")
            ,@ApiImplicitParam(name = PageConstant.PAGE_NUM, value = "分页页码", defaultValue = PageConstant.PAGE_NUM_DEFAULT, dataType = "Integer")
            ,@ApiImplicitParam(name = PageConstant.PAGE_SIZE, value = "分页大小，不得超过20", defaultValue = PageConstant.PAGE_SIZE_DEFAULT, dataType = "Integer")
            //,@ApiImplicitParam(name = PageConstant.PAGE_OFFSET, value = "偏移量", defaultValue = PageConstant.PAGE_OFFSET_DEFAULT, dataType = "Integer")
            ,@ApiImplicitParam(name = PageConstant.SORT, value = "排序字段", defaultValue = PageConstant.PAGE_SORT_DEFAULT, dataType = "String")
            ,@ApiImplicitParam(name = PageConstant.ORDER, value = "排序方向", defaultValue = PageConstant.PAGE_ORDER_DEFAULT, dataType = "String")
    })
    @ResponseBody
    @GetMapping(value = "/list")
    public Object commentList(
            @RequestParam(value = "id", required = false) Long id
            ,@RequestParam(value = "parent_id", required = false) Long parentId
            ,@RequestParam(value = "type", required = false) Integer type
            ,@RequestParam(value = "commentator", required = false) Long commentator
            ,@RequestParam(value = PageConstant.PAGE_NUM, required = false, defaultValue = PageConstant.PAGE_NUM_DEFAULT) Integer page
            ,@RequestParam(value = PageConstant.PAGE_SIZE, required = false, defaultValue = PageConstant.PAGE_SIZE_DEFAULT) Integer size
            //,@RequestParam(value = PageConstant.PAGE_OFFSET, required = false, defaultValue = PageConstant.PAGE_OFFSET_DEFAULT) Integer offset
            ,@RequestParam(value = PageConstant.SORT, required = false, defaultValue = PageConstant.PAGE_SORT_DEFAULT) String sort
            ,@RequestParam(value = PageConstant.ORDER, required = false, defaultValue = PageConstant.PAGE_ORDER_DEFAULT) String order
    ) {

        CommentQueryDTO commentQueryDTO = new CommentQueryDTO();
        commentQueryDTO.setPage(page);
        //commentQueryDTO.setOffset(offset);
        commentQueryDTO.setCommentator(commentator);
        commentQueryDTO.setId(id);
        commentQueryDTO.setParentId(parentId);
        commentQueryDTO.setSize(size);
        commentQueryDTO.setType(type);
        commentQueryDTO.setSort(sort);
        commentQueryDTO.setOrder(order);
        //BeanUtils.copyProperties(commentQueryVO,commentQueryDTO);
        commentQueryDTO.convert();
        //PaginationDTO paginationDTO = commentService.listByCommentQueryDTO(commentQueryDTO);
        return ResultDTO.okOf(commentService.list(commentQueryDTO));
    }

}
