package cn.niter.forum.cache;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TemporaryCache {

    ExpiringMap<String,String> mailCode = ExpiringMap.builder()
            .maxSize(20)//最大容量，防止恶意注入
            .expiration(5, TimeUnit.MINUTES)//过期时间5分钟
            .expirationPolicy(ExpirationPolicy.CREATED)
            .variableExpiration()
            .build();
    public void putMailCode(String mail,String code){
        mailCode.put(mail,code);
    }
    public String getMailCode(String mail){
        return mailCode.get(mail);
    }



    ExpiringMap<String,String> phoneCode = ExpiringMap.builder()
            .maxSize(20)//最大容量，防止恶意注入
            .expiration(5, TimeUnit.MINUTES)//过期时间5分钟
            .expirationPolicy(ExpirationPolicy.CREATED)
            .variableExpiration()
            .build();
    public void putPhoneCode(String serialNo,String phone_code){
        phoneCode.put(serialNo,phone_code);
    }

    public String getPhoneCode(String serialNo){
        return phoneCode.get(serialNo);
    }

}
