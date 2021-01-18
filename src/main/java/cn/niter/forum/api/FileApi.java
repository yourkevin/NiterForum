package cn.niter.forum.api;

import cn.niter.forum.annotation.UserLoginToken;
import cn.niter.forum.dto.UserDTO;
import cn.niter.forum.provider.QCloudProvider;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wadao
 * @version 1.0
 * @date 2020/9/26 21:31
 * @site niter.cn
 */
@Controller
@RequestMapping("/api/file")
//@Api(tags={"文件接口"})
@ApiIgnore()
public class FileApi {
    @Autowired
    private QCloudProvider qCloudProvider;

    //图片上传接口
    @UserLoginToken
    @ApiOperation(value = "上传文件",notes = "只有当用户登录后才能访问此接口，否则会返回401错误")
    @PostMapping("/layUpload")
    @ResponseBody
    public Map<String,Object> uploadLayImage(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        Map<String,Object> map  = new HashMap<>();
        try {
            UserDTO user = (UserDTO)request.getAttribute("loginUser");
            InputStream inputStream;
            String contentType;

            Long contentLength;
            inputStream = file.getInputStream();
            contentType = file.getContentType();
            //filename = qCloudProvider.getFileName(contentType);
            contentLength = file.getSize();
            //System.out.println("原始名字"+file.getOriginalFilename()+"类型："+file.getContentType()+"名字："+"upload/user/"+user.getId()+"/"+filename);
            // String url = qCloudProvider.upload(inputStream,contentType,"upload/user/"+user.getId()+"/"+filename,contentLength);
            String url = qCloudProvider.upload(inputStream,contentType,user,contentLength);

            map.put("code",0);
            map.put("msg","");
            map.put("data",url);
            // System.out.println(map);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code",500);
            map.put("msg","上传失败");
            map.put("data",null);
            return map;
        }
    }


}
