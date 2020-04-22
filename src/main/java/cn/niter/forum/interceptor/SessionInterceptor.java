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
        String referer = request.getHeader("referer");//告知服务器请求的原始资源的URI，其用于所有类型的请求，并且包括：协议+域名+查询参数（注意，不包含锚点信息）。因为原始的URI中的查询参数可能包含ID或密码等敏感信息，如果写入referer，则可能导致信息泄露。

        String host = request.getHeader("host");//客户端指定自己想访问的WEB服务器的域名/IP 地址和端口号。 在任何类型请求中，request都会包含此header信息。
       //处理静态资源
       if (handler instanceof ResourceHttpRequestHandler){
           if(referer!=null&&(!host.equals(referer.split("//")[1].split("/")[0]))){//静态资源防盗链
               response.setStatus(403);
               return false;
           }
           return true;
       }

       //拦截非本站post请求，如果你需要改造为前后端分离项目，此处代码可能会有所影响
       String origin = request.getHeader("origin");//用来说明请求从哪里发起的，包括，且仅仅包括协议和域名。post请求才有，这个参数一般只存在于CORS跨域请求中，可以看到response有对应的header：Access-Control-Allow-Origin。
       if(origin!=null&&(!host.equals(origin.split("//")[1])||referer==null)){
           response.setStatus(406);
           return false;
       }

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
