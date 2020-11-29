package cn.niter.forum.provider;

import cn.niter.forum.cache.TemporaryCache;
import cn.niter.forum.cache.TinifyPngCache;
import cn.niter.forum.dto.UserDTO;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.nlp.v20190408.NlpClient;
import com.tencentcloudapi.nlp.v20190408.models.KeywordsExtractionRequest;
import com.tencentcloudapi.nlp.v20190408.models.KeywordsExtractionResponse;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @author wadao
 * @version 2.0
 * @date 2020/5/1 15:17
 * @site niter.cn
 */
@Service
@Slf4j
public class QCloudProvider {

    @Value("${qcloud.secret-id}")
    private String secretId;

    @Value("${qcloud.secret-key}")
    private String secretKey;

    @Value("${qcloud.sms.appId}")
    private String smsAppId;

    @Value("${qcloud.sms.tempId}")
    private String smsTempId;

    @Value("${qcloud.sms.sign}")
    private String smsSign;

    // bucket名需包含appid
    @Value("${qcloud.cos.bucket-name}")
    private String bucketName;

    @Value("${qcloud.cos.region}")
    private String region;

    @Value("${qcloud.cos.objecturl}")
    private String objecturl;

    @Value("${qcloud.ci.objecturl}")
    private String ciObjecturl;

    @Value("${site.main.domain}")
    private String domain;

    @Value("${qcloud.ci.enable}")
    private int ciEnable;

    @Value("${tinify.enable}")
    private int tinifyEnable;

    @Value("${tinify.minContentLength}")
    private Long tinifyMinContentLength;

    @Autowired
    private TinifyPngCache tinifyPngCache;

    @Autowired
    private TemporaryCache temporaryCache;

    public String upload(InputStream fileStream, String contentType, UserDTO user,  Long contentLength) {

      /*  if("image/png".equals(contentType)&&tinifyEnable==1&&contentLength>tinifyMinContentLength){//进行图片质量压缩（png），若不处理请忽略
            try {
                fileStream = tinifyProvider.getStreamfromInputStream(fileStream);
                contentLength = (long)fileStream.available();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        String fileName = getFileName(contentType);
        String imgUrl = uploadtoBucket(fileStream,"img",contentType,user,fileName,contentLength);
        //String imgUrl=initUrl;
        if("image/png".equals(contentType)&&tinifyEnable==1&&contentLength>tinifyMinContentLength){//进行图片质量压缩（png），若不处理请忽略
            System.out.println("add:"+imgUrl);
            tinifyPngCache.add(imgUrl,user,fileName);
        }
        if(ciEnable==1){//开启腾讯云数据万象后进行图片审核，若不处理请忽略
            //ImageInfo imageInfo = getImageInfo(imgUrl);
            imgUrl = imgUrl.replace(objecturl,ciObjecturl);
        }
        return imgUrl;
    }


    public String upload(InputStream fileStream, String contentType, UserDTO user, String fileName , Long contentLength) {

      /*  if("image/png".equals(contentType)&&tinifyEnable==1&&contentLength>tinifyMinContentLength){//进行图片质量压缩（png），若不处理请忽略
            try {
                fileStream = tinifyProvider.getStreamfromInputStream(fileStream);
                contentLength = (long)fileStream.available();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        String initUrl = uploadtoBucket(fileStream,"img",contentType,user,fileName,contentLength);
        String imgUrl=initUrl;
        if("image/png".equals(contentType)&&tinifyEnable==1&&contentLength>tinifyMinContentLength){//进行图片质量压缩（png），若不处理请忽略
            System.out.println("add:"+imgUrl);
            tinifyPngCache.add(imgUrl,user,fileName);
        }
        if(ciEnable==1){//开启腾讯云数据万象后可以生成水印，进行图片审核与质量压缩（jpg），若不处理请忽略
            ImageInfo imageInfo = getImageInfo(imgUrl);
            //大于400*150才生成水印
            if(Integer.parseInt(imageInfo.getWidth())>400&&Integer.parseInt(imageInfo.getHeight())>150){
                String watermark = "@"+user.getName()+" "+domain;
                try {
                    byte[] data = watermark.getBytes("utf-8");
                    String encodeBase64 = new BASE64Encoder().encode(data);
                    watermark = encodeBase64.replace('+', '-');
                    watermark = watermark.replace('/', '_');
                    //watermark = watermark.replaceAll("=", "");
                    //watermark= Base64.getEncoder().encodeToString(data);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                imgUrl = initUrl+"?imageView2/q/75|watermark/2/text/"+watermark+"/fill/IzNEM0QzRA/fontsize/18/dissolve/80/gravity/SouthEast/dx/20/dy/10/";

                //initUrl = initUrl+"?imageView2/q/75|watermark/2/text/"+watermark+"/fill/IzNEM0QzRA/fontsize/18/dissolve/80/gravity/SouthEast/dx/20/dy/10/";
                //imgUrl=uploadUrltoBucket(initUrl,"img",contentType,user,fileName);
            }
            imgUrl = imgUrl.replace(objecturl,ciObjecturl);
        }


        return imgUrl;
    }

    public String uploadAvatar(InputStream inputStream, String contentType, UserDTO user, String fileName, Long contentLength)  {
        String initUrl = uploadtoBucket(inputStream,"avatar",contentType,user,fileName,contentLength);
        String avatarUrl=initUrl;
        //开启腾讯云数据万象后可以上传的头像进行智能剪切，若不处理请忽略
        if(ciEnable==1){
        initUrl = initUrl+"?imageMogr2/scrop/168x168/crop/168x168/gravity/center";
        avatarUrl=uploadUrltoBucket(initUrl,"avatar",contentType,user,fileName);
        //initUrl = initUrl+"?imageMogr2/crop/168x168/gravity/center";
        //avatarUrl=uploadUrltoBucket(initUrl,"avatar",contentType,user,fileName);
        }
        return avatarUrl;
    }

    private String uploadUrltoBucket(String initUrl, String fileType, String contentType, UserDTO user, String fileName)  {
        URL url = null;
        String finalUrl=null;
        InputStream fileStream=null;
        try {
            url = new URL(initUrl);
            URLConnection con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout(5*1000);
            // 输入流
            fileStream = con.getInputStream();
            finalUrl = uploadtoBucket(fileStream,fileType,contentType,user,fileName, Long.valueOf(con.getContentLength()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {//解决流资源未释放的问题
            if(fileStream!=null){
                try {
                    fileStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return finalUrl;
    }

    public String uploadtoBucket(InputStream inputStream, String fileType, String contentType, UserDTO user, String fileName, Long contentLength){

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

    private ImageInfo getImageInfo(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+"?imageInfo")
                .build();
        ImageInfo imageInfo=null;
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            System.out.println(string);
            imageInfo = JSON.parseObject(string, ImageInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

             return imageInfo;
    }


    public String sendSms(String session, String phone){

        try {
            /* 必要步骤：
             * 实例化一个认证对象，入参需要传入腾讯云账户密钥对 secretId 和 secretKey
             * 本示例采用从环境变量读取的方式，需要预先在环境变量中设置这两个值
             * 您也可以直接在代码中写入密钥对，但需谨防泄露，不要将代码复制、上传或者分享给他人
             * CAM 密钥查询：https://console.cloud.tencent.com/cam/capi*/
            Credential cred = new Credential(secretId, secretKey);

//            // 实例化一个 http 选项，可选，无特殊需求时可以跳过
//            HttpProfile httpProfile = new HttpProfile();
//            // 设置代理
//            httpProfile.setProxyHost("host");
//            httpProfile.setProxyPort(port);
//            /* SDK 默认使用 POST 方法。
//             * 如需使用 GET 方法，可以在此处设置，但 GET 方法无法处理较大的请求 */
//            httpProfile.setReqMethod("POST");
//            /* SDK 有默认的超时时间，非必要请不要进行调整
//             * 如有需要请在代码中查阅以获取最新的默认值 */
//            httpProfile.setConnTimeout(60);
//            /* SDK 会自动指定域名，通常无需指定域名，但访问金融区的服务时必须手动指定域名
//             * 例如 SMS 的上海金融区域名为 sms.ap-shanghai-fsi.tencentcloudapi.com */
//            httpProfile.setEndpoint("sms.tencentcloudapi.com");

            /* 非必要步骤:
             * 实例化一个客户端配置对象，可以指定超时时间等配置 */
            HttpProfile httpProfile = new HttpProfile();
            ClientProfile clientProfile = new ClientProfile();
            /* SDK 默认用 TC3-HMAC-SHA256 进行签名
             * 非必要请不要修改该字段 */
            clientProfile.setSignMethod("HmacSHA256");
            clientProfile.setHttpProfile(httpProfile);
            /* 实例化 SMS 的 client 对象
             * 第二个参数是地域信息，可以直接填写字符串 ap-guangzhou，或者引用预设的常量 */
            SmsClient client = new SmsClient(cred, "",clientProfile);
            /* 实例化一个请求对象，根据调用的接口和实际情况，可以进一步设置请求参数
             * 您可以直接查询 SDK 源码确定接口有哪些属性可以设置
             * 属性可能是基本类型，也可能引用了另一个数据结构
             * 推荐使用 IDE 进行开发，可以方便地跳转查阅各个接口和数据结构的文档说明 */
            SendSmsRequest req = new SendSmsRequest();

            /* 填充请求参数，这里 request 对象的成员变量即对应接口的入参
             * 您可以通过官网接口文档或跳转到 request 对象的定义处查看请求参数的定义
             * 基本类型的设置:
             * 帮助链接：
             * 短信控制台：https://console.cloud.tencent.com/smsv2
             * sms helper：https://cloud.tencent.com/document/product/382/3773 */

            /* 短信应用 ID: 在 [短信控制台] 添加应用后生成的实际 SDKAppID，例如1400006666 */
            //String appid = "1400009099";
            req.setSmsSdkAppid(smsAppId);

            /* 短信签名内容: 使用 UTF-8 编码，必须填写已审核通过的签名，可登录 [短信控制台] 查看签名信息 */
            //String sign = "签名内容";
            req.setSign(smsSign);

            /* 国际/港澳台短信 senderid: 国内短信填空，默认未开通，如需开通请联系 [sms helper] */
            //String senderid = "xxx";
            //req.setSenderId(senderid);

            /* 用户的 session 内容: 可以携带用户侧 ID 等上下文信息，server 会原样返回 */
            //String session = "xxx";
            req.setSessionContext(session);

            /* 短信码号扩展号: 默认未开通，如需开通请联系 [sms helper]
            String extendcode = "xxx";
            req.setExtendCode(extendcode);
            */
            /* 模板 ID: 必须填写已审核通过的模板 ID，可登录 [短信控制台] 查看模板 ID */
            //String templateID = "400000";
            req.setTemplateID(smsTempId);

            /* 下发手机号码，采用 e.164 标准，+[国家或地区码][手机号]
             * 例如+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号，最多不要超过200个手机号*/
            String[] phoneNumbers = {"+86"+phone};
            req.setPhoneNumberSet(phoneNumbers);

            /* 模板参数: 若无模板参数，则设置为空*/
            String code = String.valueOf(new Random().nextInt(899999) + 100000);
            String[] templateParams = {smsSign+session,code,"5"};
            req.setTemplateParamSet(templateParams);

            /* 通过 client 对象调用 SendSms 方法发起请求。注意请求方法名与请求对象是对应的
             * 返回的 res 是一个 SendSmsResponse 类的实例，与请求对象对应 */
            SendSmsResponse res = client.SendSms(req);

            // 输出 JSON 格式的字符串回包
            System.out.println(SendSmsResponse.toJsonString(res));
            SendStatus[] sendStatusSet = res.getSendStatusSet();
            for (SendStatus sendStatus : sendStatusSet) {
                if("Ok".equals(sendStatus.getCode())){
                    temporaryCache.putPhoneCode(sendStatus.getSerialNo(),sendStatus.getPhoneNumber()+code);
                }
                return SendSmsResponse.toJsonString(sendStatus);
            }

            // 可以取出单个值，您可以通过官网接口文档或跳转到 response 对象的定义处查看返回字段的定义
            //System.out.println(res.getRequestId());

        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getKeywords(String text , int num , double score){
        String keyWordString = "";
        try{

            Credential cred = new Credential(secretId, secretKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("nlp.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            NlpClient client = new NlpClient(cred, "ap-guangzhou", clientProfile);

            String params = "{\"Num\":"+num+",\"Text\":\""+text+"\"}";
            KeywordsExtractionRequest req = KeywordsExtractionRequest.fromJsonString(params, KeywordsExtractionRequest.class);

            KeywordsExtractionResponse resp = client.KeywordsExtraction(req);
            JSONObject obj= JSON.parseObject(KeywordsExtractionRequest.toJsonString(resp));
            JSONArray keywords = obj.getJSONArray("Keywords");
            List<Keywords> keywordsList = JSONObject.parseArray(keywords.toJSONString(), Keywords.class);

            if(keywordsList.size()>0)
                for (Keywords keyword : keywordsList) {
                    if(keyword.getScore()>score) keyWordString=keyWordString+","+keyword.getWord();
                }
            //System.out.println("keyWordString:"+keyWordString.substring(1));
            //System.out.println(KeywordsExtractionRequest.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return keyWordString;//返回格式  ,k1,k2,k3,k4.....,kn
    }

    public String getFileName(String contentType) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStr = sdf.format(new Date());
        String[] ss = contentType.split("/");
        String str = RandomStringUtils.random(2,
                "abcdefghijklmnopqrstuvwxyz1234567890");
        String name = timeStr + "_" +str+"."+ss[1];
        return name;
    }

    @Data
    static class  Keywords{
        Double Score;
        String Word;
    }

    @Data
    static class  ImageInfo{
        String format;
        String width;
        String height;
        String size;
        String md5;
        String photo_rgb;
    }

}
