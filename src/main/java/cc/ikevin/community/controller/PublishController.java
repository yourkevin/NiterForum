package cc.ikevin.community.controller;

import cc.ikevin.community.cache.TagCache;
import cc.ikevin.community.dto.QuestionDTO;
import cc.ikevin.community.exception.CustomizeErrorCode;
import cc.ikevin.community.exception.CustomizeException;
import cc.ikevin.community.model.Question;
import cc.ikevin.community.model.User;
import cc.ikevin.community.service.QuestionService;
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

    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tags", TagCache.get());
        return "publish";
    }
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("tag") String tag,
            @RequestParam("id") Long id,
            HttpServletRequest request,
            Model model
    ){
        title = title.trim();
        tag = tag.trim();
        model.addAttribute("title",title);
       // model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.get());
        User user = (User)request.getSession().getAttribute("user");
       // System.out.println("标题|"+title+"|");
        if(user==null) {
            model.addAttribute("error","用户未登陆");
            return "publish";
        }

        if (StringUtils.isBlank(title)) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || "".equals(description)) {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (StringUtils.isBlank(tag)) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
//如果无需限制标签规范或者允许用户自定义标签，那么删掉下面这段代码就可以了
        String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入非法标签:" + invalid);
            return "publish";
        }


      /*  User user=null;
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length!=0){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){
                    String token=cookie.getValue();
                    user = userMapper.findByToken(token);
                    if(user!=null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }*/

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        //question.setGmtCreate(System.currentTimeMillis());
        //question.setGmtModified(question.getGmtCreate());
       // questionMapper.creat(question);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }

    @GetMapping("publish/{id}")
     public String edit(@PathVariable(name = "id") Long id,
                        Model model,
                        HttpServletRequest request){
        QuestionDTO question = questionService.getById(id);
        User user = (User)request.getSession().getAttribute("user");
        if (question.getCreator() != user.getId() ){
            throw new CustomizeException(CustomizeErrorCode.CAN_NOT_EDIT_QUESTION);
        }
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id", question.getId());
        model.addAttribute("tags", TagCache.get());
        model.addAttribute("navtype", "publishnav");
        return "publish";
     }


}
