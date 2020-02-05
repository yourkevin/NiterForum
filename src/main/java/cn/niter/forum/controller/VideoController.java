package cn.niter.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VideoController {

    @GetMapping("/videos")
    public String index(Model model) {
        model.addAttribute("navtype", "videonav");
        return "videos";
    }

    @GetMapping("/video")
    public String video(@RequestParam(name = "aid") String aid,
                        @RequestParam(name = "page", defaultValue = "1") String page,
                        Model model) {
        model.addAttribute("aid", aid);
        model.addAttribute("page", page);
        model.addAttribute("navtype", "videonav");
        return "video";
    }
}

