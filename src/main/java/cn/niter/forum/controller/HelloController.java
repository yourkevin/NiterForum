package cn.niter.forum.controller;

import cn.niter.forum.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
@Controller
public class HelloController {
    @Autowired
    private AliProvider aliProvider;

    @Autowired
    private TinifyProvider tinifyProvider;

    @Autowired
    private QCloudProvider qCloudProvider;

    @Autowired
    private QqProvider qqProvider;

    @Autowired
    private BaiduCloudProvider baiduCloudProvider;

    @Value("${qcloud.keywords.enable}")
    private int keywordsEnable;

    @GetMapping("/hello")
    public String hello(HttpServletRequest request, Model model) {
       //baiduCloudProvider.getTextCensorReult("习近平");
      /*  if(keywordsEnable==0)
        System.out.println(keywordsEnable);*/
       // qqProvider.allOpenIdtoUnionId("101844155");
        //tinifyProvider.init();
       return "other/video2";
    }



}
