package cc.ikevin.community.service;

import cc.ikevin.community.mapper.UserMapper;
import cc.ikevin.community.model.User;
import cc.ikevin.community.model.UserExample;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);

        //User dbUser = userMapper.findByAccountId(user.getAccountId());
       if (users.size() == 0) {
            // 插入
           if(user.getName()==null)
               user.setName(getUserName("Github"));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        } else {
            //更新
            User dbUser = users.get(0);
            User updateUser = new User();
           if(dbUser.getName()==null&&user.getName()==null)//数据库为空，当前为空
               updateUser.setName(getUserName("Github"));
           if(user.getName()!=null)//当前不空
               updateUser.setName(user.getName());
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
           // updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }
    /*   if(dbUser==null){
           //插入
           user.setGmtCreate(System.currentTimeMillis());
           user.setGmtModified(user.getGmtCreate());
           userMapper.insert(user);

       }
       else{
         //更新
           dbUser.setGmtModified(System.currentTimeMillis());
           dbUser.setAvatarUrl(user.getAvatarUrl());
           dbUser.setName(user.getName());
           dbUser.setToken(user.getToken());
           userMapper.update(dbUser);
       }*/
    }
    public String getUserName(String authorizeSize) {
        String str = RandomStringUtils.random(5,
                "abcdefghijklmnopqrstuvwxyz1234567890");
        String name = authorizeSize+"用户_"+str;
        return name;
    }
}
