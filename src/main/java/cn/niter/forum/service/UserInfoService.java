package cn.niter.forum.service;


import cn.niter.forum.mapper.UserInfoMapper;
import cn.niter.forum.model.UserInfo;
import cn.niter.forum.model.UserInfoExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    public int updateByUserId(UserInfo userInfo,Long userId){
        UserInfoExample userInfoExample = new UserInfoExample();
        userInfoExample.createCriteria().andUserIdEqualTo(userId);
        return userInfoMapper.updateByExampleSelective(userInfo,userInfoExample);
    }

    public UserInfo selectByUserId(Long userId) {
        //  Long id = Long.parseLong(userId);
        UserInfoExample userInfoExample = new UserInfoExample();
        userInfoExample.createCriteria().andUserIdEqualTo(userId);
        List<UserInfo> userInfos = userInfoMapper.selectByExample(userInfoExample);
        UserInfo userInfo = userInfos.get(0);
        return userInfo;
    }
}
