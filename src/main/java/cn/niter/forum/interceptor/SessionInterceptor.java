package cn.niter.forum.interceptor;

import cn.niter.forum.annotation.UserLoginToken;
import cn.niter.forum.cache.LoginUserCache;
import cn.niter.forum.dto.ResultDTO;
import cn.niter.forum.dto.UserDTO;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import cn.niter.forum.mapper.UserAccountMapper;
import cn.niter.forum.mapper.UserMapper;
import cn.niter.forum.service.AdService;
import cn.niter.forum.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author wadao
 * @version 2.0
 * @date 2020/5/1 15:17
 * @site niter.cn
 */
@Service
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private LoginUserCache loginUserCache;
    @Autowired
    private AdService adService;
    @Autowired
    TokenUtils tokenUtils;
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
      /* String origin = request.getHeader("origin");//用来说明请求从哪里发起的，包括，且仅仅包括协议和域名。post请求才有，这个参数一般只存在于CORS跨域请求中，可以看到response有对应的header：Access-Control-Allow-Origin。
       if(origin!=null&&(!host.equals(origin.split("//")[1])||referer==null)){
           response.setStatus(406);
           return false;
       }*/

        //设置广告
    /*    for (AdPosEnum adPos : AdPosEnum.values()) {
            request.getServletContext().setAttribute(adPos.name(), adService.list(adPos.name()));
        }*/
        HandlerMethod handlerMethod=(HandlerMethod)handler;
        Method method=handlerMethod.getMethod();
        String token=null;
        ResultDTO resultDTO=null;
        Cookie[] cookies = request.getCookies();
        boolean hashToken = false;
        if(cookies!=null&&cookies.length!=0){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){
                    token=cookie.getValue();
                    if(token!=null) {
                        hashToken=true;
                        resultDTO = tokenUtils.verifyToken(token);
                        if(resultDTO.getCode()==200){
                            UserDTO userDTO = (UserDTO) resultDTO.getData();
                            request.setAttribute("loginUser",userDTO);
                            loginUserCache.putLoginUser(userDTO.getId(),System.currentTimeMillis());
                             //return true;
                        }
                    }
                    //检查有没有需要用户权限的注解
                   /* if (method.isAnnotationPresent(UserLoginToken.class)) {
                        UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
                        if (userLoginToken.required()) {
                            // 执行认证
                            if (token == null||resultDTO.getCode()!=200) {
                                response.setStatus(401);
                                throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
                                //return false;
                            }*/
                            // 获取 token 中的 user id
               /* String userId;
                try {
                    userId = JWT.decode(token).getAudience().get(0);
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("401-1");
                }
                User user = userService.findUserById(userId);
                if (user == null) {
                    throw new RuntimeException("用户不存在，请重新登录");
                }*/
                            // 验证 token
              /*  JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).withIssuer("NiterUser").build();
                try {
                    DecodedJWT verify = jwtVerifier.verify(token);
                    Map<String, Claim> map=verify.getClaims();
                    Map<String, String> resultMap = new HashMap<>(map.size());
                    map.forEach((k, v) -> resultMap.put(k, v.asString()));
                    System.out.println("resultMap"+JSON.toJSONString(resultMap));

                } catch (JWTVerificationException e) {
                    throw new RuntimeException("401-2");
                }*/
                          /*  return true;
                        }
                    }*/
                    break;
                }
            }
        }


        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if ((!hashToken)||resultDTO.getCode()!=200) {
                    response.setStatus(401);
                    new CustomizeException(CustomizeErrorCode.NO_LOGIN);
                    return false;
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
