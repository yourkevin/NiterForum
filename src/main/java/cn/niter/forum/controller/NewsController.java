package cn.niter.forum.controller;

import cn.niter.forum.cache.HotTagCache;
import cn.niter.forum.cache.LoginUserCache;
import cn.niter.forum.dto.NewsDTO;
import cn.niter.forum.dto.PaginationDTO;
import cn.niter.forum.model.User;
import cn.niter.forum.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class NewsController {

    @Autowired
    private HotTagCache hotTagCache;

    @Autowired
    private LoginUserCache loginUserCache;

    @Autowired
    private NewsService newsService;



    @GetMapping("/news")
    public String newsIndex(HttpServletRequest request, Model model,
                         @RequestParam(name = "page",defaultValue = "1")Integer page,
                         @RequestParam(name = "size",defaultValue = "10")Integer size,
                         @RequestParam(name = "column",defaultValue = "0") Integer column2,
                         @RequestParam(name = "search", defaultValue = "") String search,
                         @RequestParam(name = "tag", defaultValue = "") String tag,
                         @RequestParam(name = "sort", defaultValue = "new") String sort) {
        //List<QuestionDTO> topQuestions = questionService.listTopwithColumn(search, tag, sort,column2);
       // UserAccount userAccount = (UserAccount)request.getSession().getAttribute("userAccount");
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
        //model.addAttribute("topQuestions", topQuestions);
        model.addAttribute("navtype", "newsnav");
        //return "index";
        return "news/index";
    }

    @GetMapping("/news/{id}")
    public String po(@PathVariable(name = "id") Long id, HttpServletRequest request, Model model){
        NewsDTO newsDTO = newsService.getById(id);
        PaginationDTO more = newsService.listAllNews("", "", "new", 1, 10, newsDTO.getColumn2());
        List<NewsDTO> relatedNews = more.getData();
        //List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        //累加阅读数
        newsService.incView(id);
        //这里提取简短描述
        String description=newsDTO.getDescription();
        if(description.length()>100) description=description.substring(0,100);
        //System.out.println(textDescription);
        model.addAttribute("textDescription", newsDTO.getTitle()+".."+description);
        model.addAttribute("news", newsDTO);
       // model.addAttribute("comments", comments);
        model.addAttribute("relatedNews", relatedNews);
        model.addAttribute("navtype", "newsnav");
        return "news/detail";
    }

    @GetMapping("/news/list")
    @ResponseBody
    public Map<String,Object> newsList(HttpServletRequest request, Model model,
                                     @RequestParam(name = "page",defaultValue = "1")Integer page,
                                     @RequestParam(name = "size",defaultValue = "10")Integer size,
                                     @RequestParam(name = "column", required = false) Integer column2,
                                     @RequestParam(name = "search", required = false) String search,
                                     @RequestParam(name = "tag", required = false) String tag,
                                     @RequestParam(name = "sort", required = false) String sort) {
        Map<String,Object> map  = new HashMap<>();
        PaginationDTO pagination = newsService.listAllNews(search, tag, sort, page, size,column2);

        map.put("news",pagination.getData());
        //map.put("pagination",pagination);
        map.put("totalPage",pagination.getTotalPage());
        // System.out.println(pagination.getTotalPage());
        map.put("search",search);
        map.put("tag",tag);
        map.put("sort",sort);
        map.put("column",column2);
        //return "index";
        return map;
    }
}
