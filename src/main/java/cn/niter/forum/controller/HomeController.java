package cn.niter.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "15")Integer size,
                        //@RequestParam(name = "search", defaultValue = "") String search,
                        @RequestParam(name = "column", defaultValue = "2") Integer column2
                                    ) {
        model.addAttribute("column", column2);
        model.addAttribute("page", page);
        model.addAttribute("navtype", "homenav");
        return "home/index";
    }

    @GetMapping("/home/message")
    public String message(HttpServletRequest request,
                        Model model
    ) {
        model.addAttribute("navtype", "msgnav");
        return "home/message";
    }


}
