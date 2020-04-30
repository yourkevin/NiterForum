package cn.niter.forum.controller;

import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import cn.niter.forum.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Controller
public class ChatController {

    @Value("${xianliao.sso.key}")
    private String sso_key;
    @Value("${xianliao.xlm.wid}")
    private String xlm_wid;
    @Value("${site.main.domain}")
    private String domain;


    @GetMapping("/chat")
    public String chat(HttpServletRequest request, Model model) {
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        // String xlm_wid ="14875";
        String xlm_url="https://www.xianliao.me/";
        String xlm_uid = ""+user.getId();
        String xlm_name = user.getName();
        String xlm_avatar = ""+user.getAvatarUrl();
        String xlm_time = ""+(int)(System.currentTimeMillis()/1000);
        if(!("http".equals(xlm_avatar.substring(0,4))))
            xlm_avatar = "http://"+domain+"/"+xlm_avatar;
        // String xlm_name_encode = URLEncoder.encode(xlm_name);
         String xlm_avatar_encode = URLEncoder.encode(xlm_avatar);
        String s = xlm_wid+"_"+xlm_uid+"_"+xlm_time+"_"+sso_key;
        String xlm_hash = SHA(s, "SHA-512");
        // String go = "https://xianliao.me/s/14875?mobile=1&uid="+xlm_uid+"&username="+xlm_name_encode+"&avatar="+xlm_avatar_encode+"&ts="+xlm_time+"&token="+xlm_hash;
        // String go2 = "hello?mobile=1&uid="+xlm_uid+"&username="+xlm_name_encode+"&avatar="+xlm_avatar_encode+"&ts="+xlm_time+"&token="+xlm_hash;
        model.addAttribute("xlm_wid",xlm_wid);
        model.addAttribute("xlm_uid",xlm_uid);
        model.addAttribute("xlm_name",xlm_name);
        model.addAttribute("xlm_avatar",xlm_avatar);
        model.addAttribute("xlm_time",xlm_time);
        model.addAttribute("xlm_hash",xlm_hash);
        model.addAttribute("navtype", "chatnav");
        return "other/chat";
    }

    @GetMapping("/mchat")
    public String mChat(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        //String xlm_wid ="14875";
        //String xlm_url="https://www.xianliao.me/";
        String xlm_uid = ""+user.getId();
        String xlm_name = user.getName();
        String xlm_avatar = ""+user.getAvatarUrl();
        String xlm_time = ""+(int)(System.currentTimeMillis()/1000);
        if(!("http".equals(xlm_avatar.substring(0,4))))
            xlm_avatar = "http://"+domain+"/"+xlm_avatar;
        String xlm_name_encode = URLEncoder.encode(xlm_name);
        String xlm_avatar_encode = URLEncoder.encode(xlm_avatar);
        String s = xlm_wid+"_"+xlm_uid+"_"+xlm_time+"_"+sso_key;
        String xlm_hash = SHA(s, "SHA-512");
        String go = "https://xianliao.me/s/"+xlm_wid+"?mobile=1&uid="+xlm_uid+"&username="+xlm_name_encode+"&avatar="+xlm_avatar_encode+"&ts="+xlm_time+"&token="+xlm_hash;
        return "redirect:"+go;
    }

    private String SHA(final String strText, final String strType)
    {
        // 返回值
        String strResult = null;

        // 是否是有效字符串
        if (strText != null && strText.length() > 0)
        {
            try
            {
                // SHA 加密开始
                // 创建加密对象 并傳入加密類型
                MessageDigest messageDigest = MessageDigest.getInstance(strType);
                // 传入要加密的字符串
                messageDigest.update(strText.getBytes());
                // 得到 byte 類型结果
                byte byteBuffer[] = messageDigest.digest();

                // 將 byte 轉換爲 string
                StringBuffer strHexString = new StringBuffer();
                // 遍歷 byte buffer
                for (int i = 0; i < byteBuffer.length; i++)
                {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1)
                    {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                // 得到返回結果
                strResult = strHexString.toString();
            }
            catch (NoSuchAlgorithmException e)
            {
                e.printStackTrace();
            }
        }

        return strResult;
    }
}
