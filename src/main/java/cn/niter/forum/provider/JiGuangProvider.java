package cn.niter.forum.provider;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.connection.ApacheHttpClient;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.ValidSMSResult;
import cn.jsms.api.account.AccountBalanceResult;
import cn.jsms.api.account.AppBalanceResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.BatchSMSPayload;
import cn.jsms.api.common.model.BatchSMSResult;
import cn.jsms.api.common.model.RecipientPayload;
import cn.jsms.api.common.model.SMSPayload;
import cn.jsms.api.schedule.model.ScheduleResult;
import cn.jsms.api.schedule.model.ScheduleSMSPayload;
import cn.jsms.api.template.SendTempSMSResult;
import cn.jsms.api.template.TempSMSResult;
import cn.jsms.api.template.TemplatePayload;
import cn.niter.forum.dto.ResultDTO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class JiGuangProvider {
    protected static final Logger LOG = LoggerFactory.getLogger(JiGuangProvider.class);

    private static String appkey ;
    @Value("${jiguang.sms.appkey}")
    public  void setAppkey(String appkey) {
        this.appkey= appkey;
    }

    private static String masterSecret ;
    @Value("${jiguang.sms.masterSecret}")
    public  void setMasterSecret(String masterSecret) {
        this.masterSecret= masterSecret;
    }

    private static String devKey ;
    @Value("${jiguang.devKey}")
    public  void setDevKey(String devKey) {
        this.devKey= devKey;
    }

    private static String devSecret ;
    @Value("${jiguang.devSecret}")
    public  void setDevSecret(String devSecret) {
        this.devSecret= devSecret;
    }

    private static Integer tempId ;
    @Value("${jiguang.sms.tempId}")
    public  void setTempId(Integer tempId) {
        this.tempId= tempId;
    }

    private static String authorization = "OGY5Mzg2OWI4ZTM0ZWE1MGE4ZWZhMzliOmRiNzY0OTFjMjYwZjQzOGZlMTkzMDgxMw==";
    private static String prikey ="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANETgTv06rOHimjCSb/wB8zG1OBXTSJoGYwlW/iky0DBd4ZXsWUX94ouY8dfSRW6syMT6w237QHI5nHxNH8o/ulx9MHfRBfSoZp9GLj3PFIMI9UG3uN+fqF2JaV8xxI2M5aELM4xR2IXDEZH1pLmadOvaDMWyF8DzF+PGiDhdDVzAgMBAAECgYEA0B5/VYSOINm6QF+udB5mVURTYWHlyWsgiy4hKBg6ryImbZdbYYuDAIz/C9Zg9P1dFnzcTVKZ3V3zbtaw8CeJ3mCknoHrMN4KCFkaqoUPyPgqe2yjIt7ZKaa0X8iJo82zW5ATAdWRD5uCJhIRBwE30wJE80MZ3HVrhbS8aQYevEkCQQDuMmHFny+DakuXn+QwDrvJWKO2E08iF2XpkwGtqsyvUSlUf3knuiN7mGDszFebQGFxgb6Ub/34X2Ts9b0WKTUfAkEA4LPvFRroWAayu/FP3zhc0L+KHWP62iAJ5ba6eKriwS5amoqTiPksfblq+eOqLWziyEAVqYQBcHsG6jp30idBLQJAEnLsh7XwpCkTecb0kZRSjyHCbFPKiUVuq0yrkJvuBpYusVC+PYl5PhVrTGv3TRsLcRMvg6e48AfTdVcDMjg22QJACH/l6Ed3SHUgZ6mOGuR35lGIeOoiQAP8O9s4nH3iS+pj9PqO8Bx0yWCtIjyxYDyBK4/5AcuDfmy46z1A8QsZFQJBAJi/aBWJnoYxF15RJp305JpD6qSjiSZH541gOLWOleNb3prvbpWBeCKi1zQXrASw6lC/JR6dw6an8zwREXbL+/Q=";

    public static void test() throws Exception {
        String encrypted ="FIV6k62Nkm1Ayc06DT+Y3+88zKP8UrmMQj/XylZC76uX2GNm/5RjStXmBjtu9+XoCknIcVTYJO29JI5m3fJZgMxic13cjcHu5W3Y1TwYVKjikFlzHkLhpTBqVip6Tz1lN6oVAeMpZCH11hba9yK4OWWy/0Z06hJy3IUiitDp6jM=";
        String prikey ="MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANETgTv06rOHimjCSb/wB8zG1OBXTSJoGYwlW/iky0DBd4ZXsWUX94ouY8dfSRW6syMT6w237QHI5nHxNH8o/ulx9MHfRBfSoZp9GLj3PFIMI9UG3uN+fqF2JaV8xxI2M5aELM4xR2IXDEZH1pLmadOvaDMWyF8DzF+PGiDhdDVzAgMBAAECgYEA0B5/VYSOINm6QF+udB5mVURTYWHlyWsgiy4hKBg6ryImbZdbYYuDAIz/C9Zg9P1dFnzcTVKZ3V3zbtaw8CeJ3mCknoHrMN4KCFkaqoUPyPgqe2yjIt7ZKaa0X8iJo82zW5ATAdWRD5uCJhIRBwE30wJE80MZ3HVrhbS8aQYevEkCQQDuMmHFny+DakuXn+QwDrvJWKO2E08iF2XpkwGtqsyvUSlUf3knuiN7mGDszFebQGFxgb6Ub/34X2Ts9b0WKTUfAkEA4LPvFRroWAayu/FP3zhc0L+KHWP62iAJ5ba6eKriwS5amoqTiPksfblq+eOqLWziyEAVqYQBcHsG6jp30idBLQJAEnLsh7XwpCkTecb0kZRSjyHCbFPKiUVuq0yrkJvuBpYusVC+PYl5PhVrTGv3TRsLcRMvg6e48AfTdVcDMjg22QJACH/l6Ed3SHUgZ6mOGuR35lGIeOoiQAP8O9s4nH3iS+pj9PqO8Bx0yWCtIjyxYDyBK4/5AcuDfmy46z1A8QsZFQJBAJi/aBWJnoYxF15RJp305JpD6qSjiSZH541gOLWOleNb3prvbpWBeCKi1zQXrASw6lC/JR6dw6an8zwREXbL+/Q=";
        //String prikey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANETgTv06rOHimjCSb/wB8zG1OBXTSJoGYwlW/iky0DBd4ZXsWUX94ouY8dfSRW6syMT6w237QHI5nHxNH8o/ulx9MHfRBfSoZp9GLj3PFIMI9UG3uN+fqF2JaV8xxI2M5aELM4xR2IXDEZH1pLmadOvaDMWyF8DzF+PGiDhdDVzAgMBAAECgYEA0B5/VYSOINm6QF+udB5mVURTYWHlyWsgiy4hKBg6ryImbZdbYYuDAIz/C9Zg9P1dFnzcTVKZ3V3zbtaw8CeJ3mCknoHrMN4KCFkaqoUPyPgqe2yjIt7ZKaa0X8iJo82zW5ATAdWRD5uCJhIRBwE30wJE80MZ3HVrhbS8aQYevEkCQQDuMmHFny+DakuXn+QwDrvJWKO2E08iF2XpkwGtqsyvUSlUf3knuiN7mGDszFebQGFxgb6Ub/34X2Ts9b0WKTUfAkEA4LPvFRroWAayu/FP3zhc0L+KHWP62iAJ5ba6eKriwS5amoqTiPksfblq+eOqLWziyEAVqYQBcHsG6jp30idBLQJAEnLsh7XwpCkTecb0kZRSjyHCbFPKiUVuq0yrkJvuBpYusVC+PYl5PhVrTGv3TRsLcRMvg6e48AfTdVcDMjg22QJACH/l6Ed3SHUgZ6mOGuR35lGIeOoiQAP8O9s4nH3iS+pj9PqO8Bx0yWCtIjyxYDyBK4/5AcuDfmy46z1A8QsZFQJBAJi/aBWJnoYxF15RJp305JpD6qSjiSZH541gOLWOleNb3prvbpWBeCKi1zQXrASw6lC/JR6dw6an8zwREXbL+/Q=";
        String result = decrypt(encrypted, prikey);

        System.out.println(result);

  	//testSendSMSCode("***");
    //   testSendValidSMSCode();
//        testSendVoiceSMSCode();
     //   testSendTemplateSMS();
    }

  /*  public static String decrypt(String cryptograph, String prikey) throws Exception {
        java.security.Security.addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(prikey));
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] b = Base64.getDecoder().decode(cryptograph);
        return new String(cipher.doFinal(b));
    }*/



    public static Object loginTokenVerify(Object ltvd) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(ltvd));
        Request request = new Request.Builder()
                .url("https://api.verification.jpush.cn/v1/web/loginTokenVerify")
                .header("Authorization","Basic "+authorization)
                //.header("Content-Type","application/json")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(string);
            if(jsonObject.getInteger("code")==8000){
                return ResultDTO.okOf(decrypt(jsonObject.getString("phone"), prikey));
            }else {
                return ResultDTO.errorOf(jsonObject.getString("content"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String decrypt (String cryptograph, String prikey) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(prikey));
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        Cipher cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte [] b = Base64.getDecoder().decode(cryptograph);
        return new String(cipher.doFinal(b));
    }


    public static String testSendSMSCode(String phone) {
        SMSClient client = new SMSClient(masterSecret, appkey);
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber(phone)
                .setTempId(tempId)
                .build();
        try {
            SendSMSResult res = client.sendSMSCode(payload);
            System.out.println(res.toString());
            LOG.info(res.toString());
            return res.toString();
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
        return "";
    }

    public static void testSendSMSWithIHttpClient() {
        SMSClient client = new SMSClient(masterSecret, appkey);
        String authCode = ServiceHelper.getBasicAuthorization(appkey, masterSecret);
        ApacheHttpClient httpClient = new ApacheHttpClient(authCode, null, ClientConfig.getInstance());
        // NettyHttpClient httpClient = new NettyHttpClient(authCode, null, ClientConfig.getInstance());
        // ApacheHttpClient httpClient = new ApacheHttpClient(authCode, null, ClientConfig.getInstance());
        // 可以切换 HttpClient，默认使用的是 NativeHttpClient
        client.setHttpClient(httpClient);
        // 如果使用 NettyHttpClient，发送完请求后要调用 close 方法
        // client.close();
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber("13800138000")
                .setTempId(1)
                .build();
        try {
            SendSMSResult res = client.sendSMSCode(payload);
            System.out.println(res.toString());
            LOG.info(res.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public static String testSendValidSMSCode(String msg_id,String code) {
        SMSClient client = new SMSClient(masterSecret, appkey);
        try {
            ValidSMSResult res = client.sendValidSMSCode(msg_id, code);
            System.out.println(res.toString());
            LOG.info(res.toString());
            return res.toString();
        } catch (APIConnectionException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            e.printStackTrace();
            if (e.getErrorCode() == 50010) {
                // do something
            }
            System.out.println(e.getStatus() + " errorCode: " + e.getErrorCode() + " " + e.getErrorMessage());
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
        return "";
    }

    /**
     *  The default value of ttl is 60 seconds.
     */
    public static void testSendVoiceSMSCode() {
        SMSClient client = new SMSClient(masterSecret, appkey);
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber("13800138000")
                .setTTL(90)
                .build();
        try {
            SendSMSResult res = client.sendVoiceSMSCode(payload);
            LOG.info(res.toString());
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        }
    }

    public static void testSendTemplateSMS() {
        SMSClient client = new SMSClient(masterSecret, appkey);
        SMSPayload payload = SMSPayload.newBuilder()
                .setMobileNumber("")
                .setSignId(1)
                .setTempId(tempId)
                .addTempPara("code", "123456")
                .build();
        try {
            SendSMSResult res = client.sendTemplateSMS(payload);
            LOG.info(res.toString());
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        }
    }

    public static void testSendBatchTemplateSMS() {
        SMSClient client = new SMSClient(masterSecret, appkey);
        List<RecipientPayload> list = new ArrayList<RecipientPayload>();
        RecipientPayload recipientPayload1 = new RecipientPayload.Builder()
                .setMobile("13800138000")
                .addTempPara("code", "638938")
                .build();
        RecipientPayload recipientPayload2 = new RecipientPayload.Builder()
                .setMobile("13800138000")
                .addTempPara("code", "829302")
                .build();
        list.add(recipientPayload1);
        list.add(recipientPayload2);
        RecipientPayload[] recipientPayloads = new RecipientPayload[list.size()];
        BatchSMSPayload smsPayload = BatchSMSPayload.newBuilder()
                .setTempId(1)
                .setRecipients(list.toArray(recipientPayloads))
                .build();
        try {
            BatchSMSResult result = client.sendBatchTemplateSMS(smsPayload);
            LOG.info("Got result: " + result);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public static void testSendScheduleSMS() {
        SMSClient client = new SMSClient(masterSecret, appkey);
        ScheduleSMSPayload payload = ScheduleSMSPayload.newBuilder()
                .setMobileNumber("13800138000")
                .setTempId(1111)
                .setSendTime("2017-07-31 16:17:00")
                .addTempPara("number", "798560")
                .build();
        try {
            ScheduleResult result = client.sendScheduleSMS(payload);
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public static void testUpdateScheduleSMS() {
        SMSClient client = new SMSClient(masterSecret, appkey);
        ScheduleSMSPayload payload = ScheduleSMSPayload.newBuilder()
                .setMobileNumber("13800138000")
                .setTempId(1111)
                .setSendTime("2017-07-31 15:00:00")
                .addTempPara("number", "110110")
                .build();
        try {
            ScheduleResult result = client.updateScheduleSMS(payload, "dsfd");
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public static void testSendBatchScheduleSMS() {
        SMSClient client = new SMSClient(masterSecret, appkey);
        List<RecipientPayload> list = new ArrayList<RecipientPayload>();
        RecipientPayload recipientPayload1 = new RecipientPayload.Builder()
                .setMobile("13800138000")
                .addTempPara("number", "638938")
                .build();
        RecipientPayload recipientPayload2 = new RecipientPayload.Builder()
                .setMobile("13800138001")
                .addTempPara("number", "829302")
                .build();
        list.add(recipientPayload1);
        list.add(recipientPayload2);
        RecipientPayload[] recipientPayloads = new RecipientPayload[list.size()];
        ScheduleSMSPayload smsPayload = ScheduleSMSPayload.newBuilder()
                .setSendTime("2017-07-31 16:00:00")
                .setTempId(1245)
                .setRecipients(list.toArray(recipientPayloads))
                .build();
        try {
            BatchSMSResult result = client.sendBatchScheduleSMS(smsPayload);
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public static void testUpdateBatchScheduleSMS() {
        SMSClient client = new SMSClient(masterSecret, appkey);
        List<RecipientPayload> list = new ArrayList<RecipientPayload>();
        RecipientPayload recipientPayload1 = new RecipientPayload.Builder()
                .setMobile("13800138000")
                .addTempPara("number", "328393")
                .build();
        RecipientPayload recipientPayload2 = new RecipientPayload.Builder()
                .setMobile("13800138001")
                .addTempPara("number", "489042")
                .build();
        list.add(recipientPayload1);
        list.add(recipientPayload2);
        RecipientPayload[] recipientPayloads = new RecipientPayload[list.size()];
        ScheduleSMSPayload smsPayload = ScheduleSMSPayload.newBuilder()
                .setSendTime("2017-07-31 16:00:00")
                .setTempId(1245)
                .setRecipients(list.toArray(recipientPayloads))
                .build();
        try {
            BatchSMSResult result = client.updateBatchScheduleSMS(smsPayload, "dfs");
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public static void testDeleteScheduleSMS() {
        SMSClient client = new SMSClient(masterSecret, appkey);
        try {
            ResponseWrapper result = client.deleteScheduleSMS("sd");
            LOG.info("Response content: " + result.responseContent + " response code: " + result.responseCode);
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public static void testGetAccountSMSBalance() {
        SMSClient client = new SMSClient(devSecret, devKey);
        try {
            AccountBalanceResult result = client.getSMSBalance();
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public static void testGetAppSMSBalance() {
        SMSClient client = new SMSClient(masterSecret, appkey);
        try {
            AppBalanceResult result = client.getAppSMSBalance();
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }


    public void testCreateTemplate() {
        try {
            SMSClient client = new SMSClient(masterSecret, appkey);
            TemplatePayload payload = TemplatePayload.newBuilder()
                    .setTemplate("您好，您的验证码是{{code}}，2分钟内有效！")
                    .setType(1)
                    .setTTL(120)
                    .setRemark("验证短信")
                    .build();
            SendTempSMSResult result = client.createTemplate(payload);
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    // 只有审核不通过状态的模板才允许修改
    public void testUpdateTemplate() {
        try {
            SMSClient client = new SMSClient(masterSecret, appkey);
            TemplatePayload payload = TemplatePayload.newBuilder()
                    .setTempId(12345)
                    .setTemplate("您好，您的验证码是{{code}}，2分钟内有效！")
                    .setType(1)
                    .setTTL(120)
                    .setRemark("验证短信")
                    .build();
            SendTempSMSResult result = client.updateTemplate(payload, 12345);
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public void testCheckTemplate() {
        try {
            SMSClient client = new SMSClient(masterSecret, appkey);
            TempSMSResult result = client.checkTemplate(144923);
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

    public void testDeleteTemplate() {
        try {
            SMSClient client = new SMSClient(masterSecret, appkey);
            ResponseWrapper result = client.deleteTemplate(144923);
            LOG.info(result.toString());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Message: " + e.getMessage());
        }
    }

}
