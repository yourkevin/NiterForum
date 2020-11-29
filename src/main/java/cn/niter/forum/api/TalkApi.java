package cn.niter.forum.api;

import cn.niter.forum.annotation.UserLoginToken;
import cn.niter.forum.cache.IpLimitCache;
import cn.niter.forum.constant.PageConstant;
import cn.niter.forum.dto.*;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.model.Talk;
import cn.niter.forum.provider.BaiduCloudProvider;
import cn.niter.forum.service.TalkService;
import cn.niter.forum.vo.TalkInsertVO;
import cn.niter.forum.vo.TalkVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author wadao
 * @version 1.0
 * @date 2020/9/23 18:31
 * @site niter.cn
 */
@Controller
@RequestMapping("/api/talk")
@Api(tags={"说说接口"})
public class TalkApi {
    @Autowired
    private BaiduCloudProvider baiduCloudProvider;
    @Autowired
    private TalkService talkService;
    @Autowired
    private IpLimitCache ipLimitCache;

    @UserLoginToken
    @ApiOperation(value = "新增说说",notes = "只有当用户登录后才能访问此接口，否则会返回401错误")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "talkInsertVO", value = "说说基本信息", dataType = "TalkInsertVO")
    })
    @ResponseBody//@ResponseBody返回json格式的数据
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Object insert(@RequestBody @Valid TalkInsertVO talkInsertVO,//@RequestBody接受json格式的数据
                         HttpServletRequest request
                         //     , BindingResult results
    ) {
        UserDTO userDTO = (UserDTO) request.getAttribute("loginUser");
        if(!talkInsertVO.getToken().equals(ipLimitCache.getInterval(talkInsertVO.getIp()))){
            return ResultDTO.errorOf(CustomizeErrorCode.TOKEN_ERROR);
        }
        ResultDTO resultDTO = baiduCloudProvider.getTextCensorReult(talkInsertVO.getDescription());
        if(resultDTO.getCode()!=1){
            return resultDTO;
        }
        TalkDTO talkDTO = new TalkDTO();
        BeanUtils.copyProperties(talkInsertVO,talkDTO);
        talkDTO.setGmtCreate(System.currentTimeMillis());
        talkDTO.setGmtModified(talkDTO.getGmtCreate());
        talkDTO.setGmtLatestComment(talkDTO.getGmtCreate());
        //talkDTO.setCreator(userDTO.getId());
        talkDTO.setUser(userDTO);
        int i = talkService.insert(talkDTO);
        if(i>0) return ResultDTO.okOf(talkInsertVO);
      /*  CommentDTO commentDTO = new CommentDTO();
        BeanUtils.copyProperties(commentInsertVO,commentDTO);
        commentDTO.setGmtCreate(System.currentTimeMillis());
        commentDTO.setGmtModified(commentDTO.getGmtCreate());
        commentDTO.setCommentator(userDTO.getId());
        commentService.insert(commentDTO,userDTO);*/
        return ResultDTO.errorOf("发布失败");
    }

    @ApiOperation(value = "查询说说",notes = "根据查询条件来获取说说")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "说说id",dataType = "Long")
            ,@ApiImplicitParam(name = "type", value = "说说类型",dataType = "Integer")
            ,@ApiImplicitParam(name = "creator", value = "发布者",dataType = "Long")
            ,@ApiImplicitParam(name = PageConstant.PAGE_NUM, value = "分页页码", defaultValue = PageConstant.PAGE_NUM_DEFAULT, dataType = "Integer")
            ,@ApiImplicitParam(name = PageConstant.PAGE_SIZE, value = "分页大小，不得超过20", defaultValue = PageConstant.PAGE_SIZE_DEFAULT, dataType = "Integer")
            ,@ApiImplicitParam(name = PageConstant.SORT, value = "排序字段", defaultValue = PageConstant.PAGE_SORT_DEFAULT, dataType = "String")
            ,@ApiImplicitParam(name = PageConstant.ORDER, value = "排序方向", defaultValue = PageConstant.PAGE_ORDER_DEFAULT, dataType = "String")
    })
    @ResponseBody
    @GetMapping(value = "/list")
    public Object commentList(
            HttpServletRequest request
            ,@RequestParam(value = "id", required = false) Long id
            ,@RequestParam(value = "type", required = false) Integer type
            ,@RequestParam(value = "creator", required = false) Long creator
            ,@RequestParam(value = PageConstant.PAGE_NUM, required = false, defaultValue = PageConstant.PAGE_NUM_DEFAULT) Integer page
            ,@RequestParam(value = PageConstant.PAGE_SIZE, required = false, defaultValue = PageConstant.PAGE_SIZE_DEFAULT) Integer size
            //,@RequestParam(value = PageConstant.PAGE_OFFSET, required = false, defaultValue = PageConstant.PAGE_OFFSET_DEFAULT) Integer offset
            ,@RequestParam(value = PageConstant.SORT, required = false, defaultValue = PageConstant.PAGE_SORT_DEFAULT) String sort
            ,@RequestParam(value = PageConstant.ORDER, required = false, defaultValue = PageConstant.PAGE_ORDER_DEFAULT) String order
    ) {
        //UserDTO loginUser = (UserDTO) request.getAttribute("loginUser");
        TalkQueryDTO talkQueryDTO = new TalkQueryDTO();
        talkQueryDTO.setPage(page);
        talkQueryDTO.setCreator(creator);
        talkQueryDTO.setId(id);
        talkQueryDTO.setSize(size);
        talkQueryDTO.setType(type);
        talkQueryDTO.setSort(sort);
        talkQueryDTO.setOrder(order);
        talkQueryDTO.convert();
        //PaginationDTO paginationDTO = commentService.listByCommentQueryDTO(commentQueryDTO);
        return ResultDTO.okOf(talkService.list(talkQueryDTO,null));
    }

    @UserLoginToken
    @ApiOperation(value = "删除说说",notes = "只有当作者登录后才能取消收藏")
    @DeleteMapping("/delete")
    @ResponseBody
    public Object delQuestionById(HttpServletRequest request,
                                              @RequestParam(name = "id",defaultValue = "0") Long id) {

        UserDTO user = (UserDTO)request.getAttribute("loginUser");
        //Map<String,Object> map  = new HashMap<>();
        if(id==null||id==0) {
            return ResultDTO.errorOf(CustomizeErrorCode.VALIDATE_ERROR);
        }
        else{
            int c = talkService.deleteByPrimaryKey(id);
            if(c==0) {
                return ResultDTO.errorOf(CustomizeErrorCode.TALK_NOT_FOUND);
            }
            else {
              return ResultDTO.okOf("恭喜您，成功删除"+c+"条帖子！");
            }
        }
        //return ResultDTO.errorOf(CustomizeErrorCode.UNKNOWN_EXCEPTION);

    }

    @UserLoginToken
    @PostMapping("/set")
    @ApiOperation(value = "设置调整",notes = "只有当作者登录后才能设置")
    @ResponseBody
    public Object setQuestionById(HttpServletRequest request,
                                              @RequestParam(name = "id",defaultValue = "0") Long id
            , @RequestParam(name = "rank",required = false) Integer rank
            , @RequestParam(name = "field",required = false) String field
            , @RequestParam(name = "json",required = false) String json) {

        UserDTO user = (UserDTO)request.getAttribute("loginUser");
        //UserAccount userAccount = (UserAccount) request.getSession().getAttribute("userAccount");
        Map<String,Object> map  = new HashMap<>();
        if(id==null||id==0||field==null) {
            return ResultDTO.errorOf(CustomizeErrorCode.VALIDATE_ERROR);
        }
        TalkQueryDTO talkQueryDTO = new TalkQueryDTO();
        talkQueryDTO.setId(id);
        talkQueryDTO.convert();
        PaginationDTO paginationDTO = talkService.list(talkQueryDTO, null);
        if(paginationDTO.getTotalCount()!=1){
            return ResultDTO.errorOf(CustomizeErrorCode.TALK_NOT_FOUND);
        }
        TalkVO talkVO = (TalkVO) paginationDTO.getData().get(0);
        //Question question = questionService.getQuestionById(id);
        if(!(talkVO.getUser().getId().longValue()==user.getId()||user.getGroupId()>=18)){
            return ResultDTO.errorOf(CustomizeErrorCode.No_AUTHORITY);
        }
        Talk talk = new Talk();
        BeanUtils.copyProperties(talkVO,talk);
        if("stick".equals(field)){
            if(rank==1) talk.setStatus(talk.getStatus()|2);
            if(rank==0) talk.setStatus(talk.getStatus()&253);
        }
        if("essence".equals(field)){
            if(rank==1) talk.setStatus(talk.getStatus()|1);
            if(rank==0) talk.setStatus(talk.getStatus()&254);
        }
        if("promote".equals(field)){
            talk.setGmtLatestComment(System.currentTimeMillis());
        }
        if("admin".equals(field)){
            JSONObject obj= JSON.parseObject(json);
            //talk.setColumn2(obj.getInteger("column2"));
            talk.setPermission(obj.getInteger("permission"));
        }
        Talk talk1 = talkService.update(talk, null);
        if(talk1!=null) return ResultDTO.okOf("恭喜您，设置成功！");
        return ResultDTO.errorOf(CustomizeErrorCode.UNKNOWN_EXCEPTION);
    }

}
