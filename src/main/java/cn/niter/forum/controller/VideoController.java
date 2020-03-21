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
        return "other/videos";
    }

    @GetMapping("/video")
    public String video(@RequestParam(name = "aid",required = false) String aid,
                        @RequestParam(name = "url",required = false) String url,
                        @RequestParam(name = "page", defaultValue = "1") String page,
                        @RequestParam(name = "title", defaultValue = "视频播放") String title,
                        Model model) {
        model.addAttribute("title", title);
        model.addAttribute("url", url);
       // model.addAttribute("navtype", "videonav");
        return "other/video";
    }
}

