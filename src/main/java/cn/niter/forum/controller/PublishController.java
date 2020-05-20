package cn.niter.forum.controller;

import cn.niter.forum.annotation.UserLoginToken;
import cn.niter.forum.cache.TagCache;
import cn.niter.forum.dto.QuestionDTO;
import cn.niter.forum.dto.ResultDTO;
import cn.niter.forum.dto.UserDTO;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import cn.niter.forum.model.Question;
import cn.niter.forum.provider.BaiduCloudProvider;
import cn.niter.forum.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wadao
 * @version 2.0
 * @date 2020/5/1 15:17
 * @site niter.cn
 */

@Controller
public class PublishController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private BaiduCloudProvider baiduCloudProvider;

    @UserLoginToken
    @GetMapping("p/publish")
    public String publish2(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "p/add";
    }

    @UserLoginToken
    @PostMapping("p/publish")
    public String doPublish2(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("tag") String tag,
            @RequestParam("column2") Integer column2,
            @RequestParam("permission") Integer permission,
            @RequestParam("id") Long id,
            HttpServletRequest request,
            Model model
    ){

        String defaultDescription = "<p id=\"descriptionP\"></p>";
        description = description.replaceAll("<p id=\"descriptionP\"></p>", ""); //剔出每次编辑产生的冗余p标签
        title = title.trim();
        tag = tag.trim();
        model.addAttribute("title",title);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.get());
        model.addAttribute("column2", column2);
        model.addAttribute("id", id);
        model.addAttribute("navtype", "publishnav");
        model.addAttribute("permission", permission);
        UserDTO user = (UserDTO)request.getAttribute("loginUser");
        //UserAccount userAccount = (UserAccount)request.getSession().getAttribute("userAccount");

      /*  if(user==null) {
            model.addAttribute("error","用户未登陆");
            model.addAttribute("description", description);
            return "p/add";
        }*/

        if (StringUtils.isBlank(title)) {
            model.addAttribute("error", "标题不能为空");
            return "p/add";
        }
        if (description == null || defaultDescription.equals(description)) {
            model.addAttribute("error", "问题补充不能为空");
            return "p/add";
        }
        if (StringUtils.isBlank(tag)) {
            model.addAttribute("error", "标签不能为空");
            return "p/add";
        }
        //审核
        ResultDTO resultDTO = baiduCloudProvider.getTextCensorReult(questionService.getTextDescriptionFromHtml(title+description+tag));
        if(resultDTO.getCode()!=1){
            model.addAttribute("error",resultDTO.getMessage());
            model.addAttribute("description", description);
            return "p/add";
        }
//如果无需限制标签规范或者允许用户自定义标签，那么删掉下面这段代码就可以了
      /*  String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入非法标签:" + invalid);
            return "p/publish";
        }*/

        Question question = new Question();
        question.setPermission(permission);
        question.setColumn2(column2);
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        //question.setGmtCreate(System.currentTimeMillis());
        //question.setGmtModified(question.getGmtCreate());
        // questionMapper.creat(question);
        questionService.createOrUpdate(question,user);
        return "redirect:/forum";
    }

    @GetMapping("p/publish/{id}")
    public String edit2(@PathVariable(name = "id") Long id,
                       Model model,
                       HttpServletRequest request){
        QuestionDTO question = questionService.getById(id,0L);
        UserDTO user = (UserDTO)request.getAttribute("loginUser");
        if (question.getCreator().longValue() != user.getId().longValue() ){
            throw new CustomizeException(CustomizeErrorCode.CAN_NOT_EDIT_QUESTION);
        }
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("column2", question.getColumn2());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        model.addAttribute("tags", TagCache.get());
        model.addAttribute("navtype", "publishnav");
        return "p/add";
    }





}
