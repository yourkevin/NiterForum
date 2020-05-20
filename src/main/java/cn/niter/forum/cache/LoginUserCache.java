package cn.niter.forum.cache;

import cn.niter.forum.model.User;
import lombok.Data;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Data
public class LoginUserCache {
    private List<User> loginUsers = new ArrayList<>();

    ExpiringMap<Long,Long> loginUserMap = ExpiringMap.builder()
            .maxSize(16)//最大容量，防止恶意注入
            .expiration(11, TimeUnit.MINUTES)//过期时间10分钟
            .expirationPolicy(ExpirationPolicy.CREATED)
            .variableExpiration()
            .build();

    public void updateLoginUsers(List<User> loginUsers){
        this.loginUsers=loginUsers;

    }

    public void putLoginUser(Long uid,Long gmt){
        loginUserMap.put(uid,gmt);
    }

    public Long getLoginUser(Long uid){
        return loginUserMap.get(uid);
    }

}
