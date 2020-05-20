package cn.niter.forum.controller;

import cn.niter.forum.cache.HotTagCache;
import cn.niter.forum.cache.LoginUserCache;
import cn.niter.forum.dto.PaginationDTO;
import cn.niter.forum.dto.QuestionDTO;
import cn.niter.forum.dto.UserDTO;
import cn.niter.forum.model.User;
import cn.niter.forum.model.UserAccount;
import cn.niter.forum.service.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wadao
 * @version 2.0
 * @date 2020/5/1 16:17
 * @site niter.cn
 */

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotTagCache hotTagCache;

    @Autowired
    private LoginUserCache loginUserCache;

    @Value("${site.main.index}")
    private int indexId;

    @GetMapping(value = {"/forum"})
    public String forum(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "15")Integer size,
                        @RequestParam(name = "column", required = false) Integer column2,
                        @RequestParam(name = "search", required = false) String search,
                        @RequestParam(name = "tag", required = false) String tag,
                        @RequestParam(name = "sort", required = false) String sort) {
        UserDTO loginuser = (UserDTO) request.getAttribute("loginUser");
        UserAccount userAccount =null;
        if(loginuser!=null){
            userAccount = new UserAccount();
            BeanUtils.copyProperties(loginuser,userAccount);
            userAccount.setUserId(loginuser.getId());
        }
        List<QuestionDTO> topQuestions = questionService.listTopwithColumn(search, tag, sort,column2);
        PaginationDTO pagination = questionService.listwithColumn(search, tag, sort, page,size,column2,userAccount);
        List<String> tags = hotTagCache.getHots();
        List<User> loginUsers = loginUserCache.getLoginUsers();
        model.addAttribute("loginUsers", loginUsers);
        model.addAttribute("pagination",pagination);
        model.addAttribute("search", search);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", tags);
        model.addAttribute("sort", sort);
        model.addAttribute("column", column2);
        model.addAttribute("topQuestions", topQuestions);
        model.addAttribute("navtype", "communitynav");
        return "index";
    }

    @GetMapping(value = {"/"})
    public String index(HttpServletRequest request,
                        Model model) {
     if(indexId==2) return "redirect:/home";
      else  return "redirect:/forum";
    }



}
