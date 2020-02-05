package cn.niter.forum.provider;

import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import cn.niter.forum.model.User;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

@Service
@Slf4j
public class QCloudProvider {
    @Value("${qcloud.secret-id}")
    private String secretId;

    @Value("${qcloud.secret-key}")
    private String secretKey;

    // bucket名需包含appid
    @Value("${qcloud.cos.bucket-name}")
    private String bucketName;

    @Value("${qcloud.cos.region}")
    private String region;

    @Value("${qcloud.cos.objecturl}")
    private String objecturl;

    @Value("${site.main.domain}")
    private String domain;

    public String upload(InputStream fileStream, String contentType, User user, String fileName , Long contentLength) {

        String initUrl = uploadtoBucket(fileStream,"img",contentType,user,fileName,contentLength);
        String imgUrl=initUrl;
        //开始处理水印，若不处理请忽略
        String watermark = "@"+user.getName()+" "+domain;
        try {
            byte[] data = watermark.getBytes("utf-8");
            watermark= Base64.getEncoder().encodeToString(data);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        initUrl = initUrl+"?watermark/2/text/"+watermark+"/fill/IzNEM0QzRA/fontsize/16/dissolve/60/gravity/SouthEast/dx/20/dy/10/";
        System.out.println(initUrl);
        imgUrl=uploadUrltoBucket(initUrl,"img",contentType,user,fileName);


        return imgUrl;
    }

    public String uploadAvatar(InputStream inputStream, String contentType, User user, String fileName, Long contentLength)  {
        String initUrl = uploadtoBucket(inputStream,"avatar",contentType,user,fileName,contentLength);
        String avatarUrl=null;
        initUrl = initUrl+"?imageMogr2/scrop/168x168";
        avatarUrl=uploadUrltoBucket(initUrl,"avatar",contentType,user,fileName);
        initUrl = initUrl+"?imageMogr2/crop/168x168/gravity/center";
        avatarUrl=uploadUrltoBucket(initUrl,"avatar",contentType,user,fileName);
        return avatarUrl;
    }

    private String uploadUrltoBucket(String initUrl, String fileType, String contentType, User user, String fileName)  {
        URL url = null;
        String finalUrl=null;
        try {
            url = new URL(initUrl);
            URLConnection con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout(5*1000);
            // 输入流
            InputStream fileStream = con.getInputStream();
            finalUrl = uploadtoBucket(fileStream,fileType,contentType,user,fileName, Long.valueOf(con.getContentLength()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return finalUrl;
    }

    public String uploadtoBucket(InputStream inputStream, String fileType, String contentType, User user, String fileName, Long contentLength){

        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);

        String key = "upload/user/"+user.getId()+"/"+fileType+"/"+fileName;

        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 从输入流上传必须制定content length, 否则http客户端可能会缓存所有数据，存在内存OOM的情况
        objectMetadata.setContentLength(contentLength);
        // 默认下载时根据cos路径key的后缀返回响应的contenttype, 上传时设置contenttype会覆盖默认值
        objectMetadata.setContentType("contentType");

        PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
        // 设置存储类型, 默认是标准(Standard), 低频(standard_ia)
        putObjectRequest.setStorageClass(StorageClass.Standard);
        try {
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
            // putobjectResult会返回文件的etag
            String etag = putObjectResult.getETag();
            //System.out.println(etag);
        } catch (CosServiceException e) {
            //e.printStackTrace();
            log.error("upload error,{}", key, e);
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        } catch (CosClientException e) {
            //e.printStackTrace();
            log.error("upload error,{}", key, e);
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FAIL);
        }
        // 关闭客户端
        cosclient.shutdown();
        return objecturl+key;
    }

}
