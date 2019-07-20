package cc.ikevin.community.controller;

import cc.ikevin.community.dto.QuestionDTO;
import cc.ikevin.community.mapper.UserMapper;
import cc.ikevin.community.model.User;
import cc.ikevin.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model) {
       // model.addAttribute("name",name);
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length!=0){
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("token")){
                String token=cookie.getValue();
                User user = userMapper.findByToken(token);
                if(user!=null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }
        }

        List<QuestionDTO> questionList = questionService.list();
        model.addAttribute("questions",questionList);
        return "index";
    }
}
