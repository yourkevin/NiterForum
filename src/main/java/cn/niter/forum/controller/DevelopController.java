package cn.niter.forum.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class DevelopController {
    @GetMapping("/dev")
    public String chat(HttpServletRequest request, Model model) {
        model.addAttribute("noticeTitle","实验室正在建造中...");
        model.addAttribute("noticeMessage","感谢您的关注，实验室即将上线，敬请期待！");
        model.addAttribute("navtype", "devnav");
        return "other/notice";
    }
}
