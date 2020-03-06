package cn.niter.forum.controller;

import cn.niter.forum.cache.TagCache;
import cn.niter.forum.dto.QuestionDTO;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import cn.niter.forum.model.Question;
import cn.niter.forum.model.User;
import cn.niter.forum.model.UserAccount;
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

@Controller
public class PublishController {
    @Autowired
    private QuestionService questionService;


    @GetMapping("p/publish")
    public String publish2(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "p/add";
    }
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
      //  System.out.println("description:"+description);
     //   System.out.println("txtcontent:"+txtcontent);
        String defaultDescription = "<p id=\"descriptionP\"></p>";
        //String defaultDescription2= "<p id=\"descriptionP\" class=\"video\"></p>";
        //String defaultDescription3= "<p class=\"video\" id=\"descriptionP\"></p>";
        description = description.replaceAll("<p id=\"descriptionP\"></p>", ""); //剔出每次编辑产生的冗余p标签
        description = description.replaceAll("<p id=\"descriptionP\" class=\"video\"></p>", ""); //剔出每次的冗余p标签
        description = description.replaceAll("<p class=\"video\" id=\"descriptionP\"></p>", ""); //剔出每次的冗余p标签
        description = description.replaceAll("qs.niter", "qcdn.niter"); //剔出每次编辑产生的冗余p标签
        title = title.trim();
        tag = tag.trim();
        model.addAttribute("title",title);
        // model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.get());
        User user = (User)request.getSession().getAttribute("user");
        UserAccount userAccount = (UserAccount)request.getSession().getAttribute("userAccount");
     //   System.out.println("permission:"+permission);
        // System.out.println("正文|"+description+"|"+"相同="+defaultDescription.equals(description));
        if(user==null) {
            model.addAttribute("error","用户未登陆");
            return "p/publish";
        }

        if (StringUtils.isBlank(title)) {
            model.addAttribute("error", "标题不能为空");
            return "p/publish";
        }
        if (description == null || defaultDescription.equals(description)) {
            model.addAttribute("error", "问题补充不能为空");
            return "p/publish";
        }
        if (StringUtils.isBlank(tag)) {
            model.addAttribute("error", "标签不能为空");
            return "p/publish";
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
        questionService.createOrUpdate(question,userAccount);
        return "redirect:/";
    }

    @GetMapping("p/publish/{id}")
    public String edit2(@PathVariable(name = "id") Long id,
                       Model model,
                       HttpServletRequest request){
        QuestionDTO question = questionService.getById(id,0L);
        User user = (User)request.getSession().getAttribute("user");
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
