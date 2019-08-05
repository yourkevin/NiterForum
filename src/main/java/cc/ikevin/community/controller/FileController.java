package cc.ikevin.community.controller;

import cc.ikevin.community.dto.FileDTO;
import cc.ikevin.community.model.User;
import cc.ikevin.community.provider.QCloudProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
public class FileController {
    @Autowired
    private QCloudProvider qCloudProvider;

    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    @ResponseBody
    public FileDTO upload(HttpServletRequest request,
                          @RequestParam("myFile") List<MultipartFile> multipartFiles) {

       // MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        //MultipartFile file = multipartRequest.getFile("up-file8054188074344137");

        try {

            User user = (User)request.getSession().getAttribute("user");
           /* InputStream inputStream = multipartFile.getInputStream();
            String contentType = multipartFile.getContentType();
            String filename = getFileName(contentType);
            Long contentLength = multipartFile.getSize();
            System.out.println("类型："+multipartFile.getContentType()+"名字："+user.getId()+"/"+filename);
            String url = qCloudProvider.upload(inputStream,contentType,user.getId()+"/"+filename,contentLength);
            String[] data ={url};*/
            InputStream inputStream;
            String contentType;
            String filename;
            Long contentLength;
            String[] data = new String[multipartFiles.size()];
            int count = 0;
            for (MultipartFile multipartFile : multipartFiles){
                inputStream = multipartFile.getInputStream();
                contentType = multipartFile.getContentType();
                filename = getFileName(contentType);
                contentLength = multipartFile.getSize();
                System.out.println("原始名字"+multipartFile.getOriginalFilename()+"类型："+multipartFile.getContentType()+"名字："+user.getId()+"/"+filename);
                String url = qCloudProvider.upload(inputStream,contentType,user.getId()+"/"+filename,contentLength);
                data[count] = url;
                count++;
            }
            FileDTO fileDTO = new FileDTO();
            fileDTO.setErrno(0);
            fileDTO.setData(data);
            return fileDTO;
       } catch (Exception e) {
            log.error("upload error", e);
            FileDTO fileDTO = new FileDTO();
            fileDTO.setErrno(1);
           // fileDTO.setMessage("上传失败");
            return fileDTO;
        }
    }

    public String getFileName(String contentType) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStr = sdf.format(new Date());
        String[] ss = contentType.split("/");
        String str = RandomStringUtils.random(5,
                "abcdefghijklmnopqrstuvwxyz1234567890");
        String name = timeStr + "_" +str+"."+ss[1];
        return name;
    }

}
