package cn.niter.forum.controller;

import cn.niter.forum.provider.*;
import org.apache.commons.codec.digest.DigestUtils;
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
        String encodekey = DigestUtils.sha256Hex("测试SHA256"+"adsdsad");
        System.out.println(encodekey);
       return "other/video2";
    }



}
