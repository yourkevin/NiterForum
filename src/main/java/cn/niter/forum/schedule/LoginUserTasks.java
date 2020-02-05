package cn.niter.forum.schedule;

import cn.niter.forum.cache.LoginUserCache;
import cn.niter.forum.dto.UserQueryDTO;
import cn.niter.forum.mapper.UserExtMapper;
import cn.niter.forum.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class LoginUserTasks {

    @Autowired
    private UserExtMapper userExtMapper;
    @Autowired
    private LoginUserCache loginUserCache;


    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void loginUserSchedule() {
        log.info("loginUserSchedule start {}", new Date());
        int offset = 0;
        int limit = 12;
        List<User> list = new ArrayList<>();
        UserQueryDTO userQueryDTO = new UserQueryDTO();
        userQueryDTO.setOffset(offset);
        userQueryDTO.setSize(limit);
        list = userExtMapper.selectLatestLoginUser(userQueryDTO);
        loginUserCache.updateLoginUsers(list);
        log.info("loginUserSchedule stop {}", new Date());

    }
}
