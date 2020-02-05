package cn.niter.forum.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ScoreController {

    @PostMapping("/sign/in")
    @ResponseBody
    public Map<String,Object> signIn(HttpServletRequest request) {
        Map<String,Object> map  = new HashMap<>();
        System.out.println("进来了");
        map.put("data","测试");
        map.put("data2","测试");

        return map;
    }


}
