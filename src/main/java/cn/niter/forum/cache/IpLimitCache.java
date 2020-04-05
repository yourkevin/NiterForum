package cn.niter.forum.cache;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class IpLimitCache {
    ExpiringMap<String,String> interval = ExpiringMap.builder()
            .maxSize(20)//设置最大容量，增大攻击难度，值越大存储的可疑ip越多，过大会占用额外资源
            .expiration(1, TimeUnit.MINUTES)//过期时间1分钟
            .expirationPolicy(ExpirationPolicy.ACCESSED)//每次访问重置过期时间
            .variableExpiration()
            .build();

    ExpiringMap<String,Integer> ipScores = ExpiringMap.builder()
            .maxSize(100)//设置最大容量，增大攻击难度，值越大存储的可疑ip越多，过大会占用额外资源
            .expiration(1, TimeUnit.DAYS)//过期时间1天
            .expirationPolicy(ExpirationPolicy.CREATED)//每次更新重置过期时间
            .variableExpiration()
            .build();

    public int addIpScores(String ip,Integer score){
        if(ipScores.get(ip)==null){
            ipScores.put(ip,score);
        }else{
            System.out.println("ipScores:"+ipScores.get(ip));
            ipScores.put(ip,ipScores.get(ip)+score);
        } return 1;
    }

    public int showIpScores(String ip){
        if(ipScores.get(ip)==null) return 0;
        return  ipScores.get(ip);
    }
    public int putInterval(String ip,String token){
        if(getInterval(ip)==null){
            interval.put(ip,token);
            return 1;
        }else return 0;

    }

    public String getInterval(String ip){
        return interval.get(ip);
    }

}
