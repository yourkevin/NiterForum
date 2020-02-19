package cn.niter.forum.interceptor;

import cn.niter.forum.enums.AdPosEnum;
import cn.niter.forum.mapper.UserAccountMapper;
import cn.niter.forum.mapper.UserInfoMapper;
import cn.niter.forum.mapper.UserMapper;
import cn.niter.forum.model.*;
import cn.niter.forum.service.AdService;
import cn.niter.forum.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private AdService adService;

   /* @Value("${github.redirect.uri}")
    private String redirectUri;
    @Value("${baidu.redirect.uri}")
    private String baiduRedirectUri;
    @Value("${weibo.redirect.uri}")
    private String weiboRedirectUri;
    @Value("${qq.redirect.uri}")
    private String qqRedirectUri;*/

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       /* request.getServletContext().setAttribute("redirectUri", redirectUri);
        request.getServletContext().setAttribute("baiduRedirectUri", baiduRedirectUri);
        request.getServletContext().setAttribute("weiboRedirectUri", weiboRedirectUri);
        request.getServletContext().setAttribute("qqRedirectUri", qqRedirectUri);
*/
       if (handler instanceof ResourceHttpRequestHandler)
            return true;
        //设置广告
        for (AdPosEnum adPos : AdPosEnum.values()) {
            request.getServletContext().setAttribute(adPos.name(), adService.list(adPos.name()));
        }

        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length!=0){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){
                    String token=cookie.getValue();
                    UserExample userExample = new UserExample();
                    userExample.createCriteria()
                            .andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(userExample);

                    if(users.size()!=0){
                        User user = users.get(0);
                        //System.out.println("id"+user.getId());
                        UserAccountExample userAccountExample = new UserAccountExample();
                        userAccountExample.createCriteria().andUserIdEqualTo(user.getId());
                        List<UserAccount> userAccounts = userAccountMapper.selectByExample(userAccountExample);
                        UserAccount userAccount = userAccounts.get(0);
                        UserInfoExample userInfoExample = new UserInfoExample();
                        userInfoExample.createCriteria().andUserIdEqualTo(user.getId());
                        List<UserInfo> userInfos = userInfoMapper.selectByExample(userInfoExample);
                        UserInfo userInfo = userInfos.get(0);
                        //System.out.println("infoid"+userInfo.getId());
                        request.getSession().setAttribute("user",user);
                        request.getSession().setAttribute("userAccount",userAccount);
                        request.getSession().setAttribute("userInfo",userInfo);
                        Long unreadCount = notificationService.unreadCount(users.get(0).getId());
                        request.getSession().setAttribute("unreadCount", unreadCount);
                    //    System.out.println("用户ID："+userAccount.getGroupId());
                        UserExample example = new UserExample();
                        users.get(0).setGmtModified(System.currentTimeMillis());
                        example.createCriteria()
                                .andIdEqualTo(users.get(0).getId());
                        userMapper.updateByExampleSelective(users.get(0), example);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
