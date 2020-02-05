package cn.niter.forum.cache;

import cn.niter.forum.model.User;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
public class LoginUserCache {
    private List<User> loginUsers = new ArrayList<>();

    public void updateLoginUsers(List<User> loginUsers){
        this.loginUsers=loginUsers;

    }

}
