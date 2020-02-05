package cn.niter.forum.controller;

import cn.niter.forum.dto.CommentDTO;
import cn.niter.forum.dto.QuestionDTO;
import cn.niter.forum.enums.CommentTypeEnum;
import cn.niter.forum.model.UserAccount;
import cn.niter.forum.service.CommentService;
import cn.niter.forum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private Environment env;



    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id, Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        //累加阅读数
        questionService.incView(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        model.addAttribute("navtype", "communitynav");
        //return "question";
        return "redirect:/p/"+id;
    }


    @GetMapping("/p/{id}")
    public String po(@PathVariable(name = "id") Long id, HttpServletRequest request,Model model){
        QuestionDTO questionDTO = questionService.getById(id);
        UserAccount userAccount = (UserAccount)request.getSession().getAttribute("userAccount");
        if(userAccount==null){
            if(questionDTO.getPermission()!=0){
                model.addAttribute("rsTitle", "您无权访问！");
                model.addAttribute("rsMessage", "该贴设置了权限，游客不可见，快去登录吧");
                return "result";
            }
        }
        else if(questionDTO.getPermission()>userAccount.getGroupId()&&questionDTO.getCreator()!=userAccount.getUserId()){
            model.addAttribute("rsTitle", "您无权访问！");
            model.addAttribute("rsMessage", "该贴仅作者和"+env.getProperty("user.group.r"+questionDTO.getPermission())+"及以上的用户可以访问，快去多多发帖提升等级或者开通VIP畅享全站！");
            return "result";
        }
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        //累加阅读数
        questionService.incView(id);
        //这里提取简短描述
        String description=questionDTO.getDescription();
        String textDescription = description.replaceAll("</?[^>]+>", ""); //剔出<html>的标签
        textDescription = textDescription.replaceAll("<a>\\s*|\t|\r|\n</a>", "");//去除字符串中的空格,回车,换行符,制表符
        textDescription = textDescription.replaceAll("&nbsp;", "");//去除&nbsp;
        if(textDescription.length()>100) textDescription=textDescription.substring(0,100);
        //System.out.println(textDescription);
        model.addAttribute("textDescription", questionDTO.getTitle()+".."+textDescription);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestions", relatedQuestions);
        model.addAttribute("navtype", "communitynav");
        return "p/detail";
    }



}
