package cn.niter.forum.controller;


import cn.niter.forum.cache.HotTagCache;
import cn.niter.forum.cache.LoginUserCache;
import cn.niter.forum.dto.PaginationDTO;
import cn.niter.forum.dto.QuestionDTO;
import cn.niter.forum.model.User;
import cn.niter.forum.model.UserAccount;
import cn.niter.forum.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotTagCache hotTagCache;

    @Autowired
    private LoginUserCache loginUserCache;

    @GetMapping("/t")
    public String tIndex(HttpServletRequest request, Model model,
                         @RequestParam(name = "page",defaultValue = "1")Integer page,
                         @RequestParam(name = "size",defaultValue = "10")Integer size,
                         @RequestParam(name = "column",defaultValue = "0") Integer column2,
                         @RequestParam(name = "search", defaultValue = "") String search,
                         @RequestParam(name = "tag", defaultValue = "") String tag,
                         @RequestParam(name = "sort", defaultValue = "new") String sort) {
        List<QuestionDTO> topQuestions = questionService.listTopwithColumn(search, tag, sort,column2);
        UserAccount userAccount = (UserAccount)request.getSession().getAttribute("userAccount");
        //PaginationDTO pagination = questionService.listwithColumn(search, tag, sort, page,size,column2,userAccount);
        List<String> tags = hotTagCache.getHots();
        List<User> loginUsers = loginUserCache.getLoginUsers();
        //System.out.println("users"+loginUsers);
        model.addAttribute("loginUsers", loginUsers);
        //model.addAttribute("pagination",pagination);
        model.addAttribute("search", search);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", tags);
        model.addAttribute("sort", sort);
        model.addAttribute("column", column2);
        model.addAttribute("page", page);
        model.addAttribute("topQuestions", topQuestions);
        model.addAttribute("navtype", "communitynav");
        //return "index";
        return "t/index";
    }

    @GetMapping("/t/list")
    @ResponseBody
    public Map<String,Object> tList(HttpServletRequest request, Model model,
                                    @RequestParam(name = "page",defaultValue = "1")Integer page,
                                    @RequestParam(name = "size",defaultValue = "10")Integer size,
                                    @RequestParam(name = "column", required = false) Integer column2,
                                    @RequestParam(name = "search", required = false) String search,
                                    @RequestParam(name = "tag", required = false) String tag,
                                    @RequestParam(name = "sort", required = false) String sort) {
        //List<QuestionDTO> topQuestions = questionService.listTopwithColumn(search, tag, sort,column2);
        Map<String,Object> map  = new HashMap<>();
        UserAccount userAccount = (UserAccount)request.getSession().getAttribute("userAccount");
        PaginationDTO pagination = questionService.listwithColumn(search, tag, sort, page,size,column2,userAccount);
        map.put("questions",pagination.getData());
        map.put("totalPage",pagination.getTotalPage());
        map.put("search",search);
        map.put("tag",tag);
        map.put("sort",sort);
        map.put("column",column2);

        return map;
    }



}
