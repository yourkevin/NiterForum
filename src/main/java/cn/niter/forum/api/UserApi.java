package cn.niter.forum.api;

import cn.niter.forum.constant.PageConstant;
import cn.niter.forum.dto.ResultDTO;
import cn.niter.forum.dto.UserQueryDTO;
import cn.niter.forum.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wadao
 * @version 1.0
 * @date 2020/7/6 20:54
 * @site niter.cn
 */
@Controller
@RequestMapping("/api/user")
@Api(tags={"用户接口"})
public class UserApi {

    @Autowired
    private UserService userService;



    @ApiOperation(value = "查询用户",notes = "根据查询条件来获取用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id",dataType = "Long")
            ,@ApiImplicitParam(name = "name", value = "用户名",dataType = "String")
            ,@ApiImplicitParam(name = "email", value = "邮箱",dataType = "String")
            ,@ApiImplicitParam(name = "phone", value = "手机号",dataType = "String")
            ,@ApiImplicitParam(name = PageConstant.PAGE_NUM, value = "分页页码", defaultValue = PageConstant.PAGE_NUM_DEFAULT, dataType = "Integer")
            ,@ApiImplicitParam(name = PageConstant.PAGE_SIZE, value = "分页大小，不得超过20", defaultValue = PageConstant.PAGE_SIZE_DEFAULT, dataType = "Integer")
            ,@ApiImplicitParam(name = PageConstant.SORT, value = "排序字段", defaultValue = PageConstant.PAGE_SORT_DEFAULT, dataType = "String")
            ,@ApiImplicitParam(name = PageConstant.ORDER, value = "排序方向", defaultValue = PageConstant.PAGE_ORDER_DEFAULT, dataType = "String")
    })
    @ResponseBody
    @GetMapping(value = "/list")
    public Object commentList(
            @RequestParam(value = "id", required = false) Long id
            ,@RequestParam(value = "name", required = false) String name
            ,@RequestParam(value = "email", required = false) String email
            ,@RequestParam(value = "phone", required = false) String phone
            ,@RequestParam(value = PageConstant.PAGE_NUM, required = false, defaultValue = PageConstant.PAGE_NUM_DEFAULT) Integer page
            ,@RequestParam(value = PageConstant.PAGE_SIZE, required = false, defaultValue = PageConstant.PAGE_SIZE_DEFAULT) Integer size
            ,@RequestParam(value = PageConstant.SORT, required = false, defaultValue = PageConstant.PAGE_SORT_DEFAULT) String sort
            ,@RequestParam(value = PageConstant.ORDER, required = false, defaultValue = PageConstant.PAGE_ORDER_DEFAULT) String order

    ) {
        UserQueryDTO userQueryDTO = new UserQueryDTO();
        userQueryDTO.setId(id);
        userQueryDTO.setName(name);
        userQueryDTO.setEmail(email);
        userQueryDTO.setPhone(phone);
        userQueryDTO.setPage(page);
        userQueryDTO.setSize(size);
        userQueryDTO.setSort(sort);
        userQueryDTO.setOrder(order);
        userQueryDTO.convert();
        return ResultDTO.okOf(userService.list(userQueryDTO));
    }

}
