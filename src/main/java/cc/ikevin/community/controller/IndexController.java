package cc.ikevin.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
       // model.addAttribute("name",name);
        return "index";
    }
}
