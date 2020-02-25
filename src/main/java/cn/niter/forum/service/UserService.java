package cn.niter.forum.service;

import cn.niter.forum.dto.ResultDTO;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.mapper.UserAccountMapper;
import cn.niter.forum.mapper.UserInfoMapper;
import cn.niter.forum.mapper.UserMapper;
import cn.niter.forum.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private UserAccountMapper userAccountMapper;

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        //User user2 = new User();
        //User dbUser = userMapper.findByAccountId(user.getAccountId());
       if (users.size() == 0) {
            // 插入
           if(user.getName()==null|| StringUtils.isBlank(user.getName()))
               user.setName(getUserName("Github"));
              // user.setName(getUserName("Github"));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
           UserExample userExample2 = new UserExample();
           userExample2.createCriteria()
                   .andAccountIdEqualTo(user.getAccountId());
           List<User> users2 = userMapper.selectByExample(userExample2);
           if(users2.size() != 0){//表示user表已创建
               User dbUser = users2.get(0);
               initUserTable(dbUser,new UserInfo());
           }
        } else {
            //更新
            User dbUser = users.get(0);
            User updateUser = new User();
           if(dbUser.getName()==null&&(user.getName()==null|| StringUtils.isBlank(user.getName())))//数据库为空，当前为空
               updateUser.setName(getUserName("Github"));
          /* if(user.getName()!=null)//当前不空
               updateUser.setName(user.getName());*/
            updateUser.setGmtModified(System.currentTimeMillis());
            //updateUser.setAvatarUrl(user.getAvatarUrl());
           // updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
        }
    }

    public int createOrUpdateWeibo(User user, User loginuser, UserInfo userInfo) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andWeiboAccountIdEqualTo(user.getWeiboAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        User updateUser = new User();
        if (users.size() == 0) {  // 插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            if(user.getName()==null|| StringUtils.isBlank(user.getName()))
                user.setName(getUserName("微博"));
            if (loginuser == null) {//创建
                userMapper.insert(user);
                UserExample userExample2 = new UserExample();
                userExample2.createCriteria()
                        .andWeiboAccountIdEqualTo(user.getWeiboAccountId());
                List<User> users2 = userMapper.selectByExample(userExample2);
                if(users2.size() != 0){//表示user表已创建
                    User dbUser = users2.get(0);
                    initUserTable(dbUser,userInfo);
                   /* userInfo.setUserId(dbUser.getId());
                    userInfoMapper.insert(userInfo);*/
                }
                return 1;
            }
            if (loginuser != null) {//绑定或者换绑
                updateUser.setWeiboAccountId(user.getWeiboAccountId());
                updateUserInfo(user,updateUser,loginuser,userInfo);
                return 2;
            }



            // user.setName(getUserName("Github"));

        } else {
            //登录更新
            User dbUser = users.get(0);
            if(dbUser.getName()==null&&(user.getName()==null|| StringUtils.isBlank(user.getName())))//数据库为空，当前为空
                updateUser.setName(getUserName("微博"));
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setToken(user.getToken());
            //updateUser.setAvatarUrl(user.getAvatarUrl());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
            return 3;
        }
        return 0;
    }


    public int createOrUpdateBaidu(User user, User loginuser, UserInfo userInfo) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andBaiduAccountIdEqualTo(user.getBaiduAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        User updateUser = new User();
        if (users.size() == 0) {  // 插入

            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            if(user.getName()==null|| StringUtils.isBlank(user.getName()))
                user.setName(getUserName("Baidu"));
            if (loginuser == null) {//创建
                userMapper.insert(user);
                UserExample userExample2 = new UserExample();
                userExample2.createCriteria()
                        .andBaiduAccountIdEqualTo(user.getBaiduAccountId());
                List<User> users2 = userMapper.selectByExample(userExample2);
                if(users2.size() != 0){//表示user表已创建
                    User dbUser = users2.get(0);
                    initUserTable(dbUser,userInfo);
                   /* userInfo.setUserId(dbUser.getId());
                    userInfoMapper.insert(userInfo);*/
                }
                return 1;
              //  userMapper.insert(user);

            }
            if (loginuser != null) {//绑定或者换绑
                updateUser.setBaiduAccountId(user.getBaiduAccountId());
                updateUserInfo(user,updateUser,loginuser,userInfo);
                return 2;
            }

        } else {
            //登录更新
            User dbUser = users.get(0);
            if(dbUser.getName()==null&&(user.getName()==null|| StringUtils.isBlank(user.getName())))//数据库为空，当前为空
                updateUser.setName(getUserName("Baidu"));
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setToken(user.getToken());
           // updateUser.setAvatarUrl(user.getAvatarUrl());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
            return 3;
        }
        return 0;
    }

    public void createNewBaidu(User user,UserInfo userInfo) {
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(user.getGmtCreate());
        if(user.getName()==null|| StringUtils.isBlank(user.getName()))
            user.setName(getUserName("Baidu"));
        userMapper.insert(user);

        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andBaiduAccountIdEqualTo(user.getBaiduAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() != 0){
            User dbUser = users.get(0);
            userInfo.setUserId(dbUser.getId());
            userInfoMapper.insert(userInfo);
        }


    }

    public String getUserName(String authorizeSize) {
        String str = RandomStringUtils.random(5,
                "abcdefghijklmnopqrstuvwxyz1234567890");
        String name = authorizeSize+"用户_"+str;
        return name;
    }

    public User selectUserByUserId(String userId) {
        Long id = Long.parseLong(userId);
        User user = userMapper.selectByPrimaryKey(id);
        return user;
    }

    public Object updateUserMailById(String userId,String mail) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andEmailEqualTo(mail);
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() != 0){
            return ResultDTO.errorOf(CustomizeErrorCode.SUBMIT_MAIL_FAILED);
        }
        User updateUser = new User();
        updateUser.setEmail(mail);
        UserExample example = new UserExample();
        example.createCriteria()
                .andIdEqualTo(Long.parseLong(userId));
        try{
        userMapper.updateByExampleSelective(updateUser, example);
            return ResultDTO.okOf("邮箱绑定/更新成功！！！");
        }catch (Exception e){
            return ResultDTO.errorOf(CustomizeErrorCode.SUBMIT_MAIL_FAILED);
        }

    }

    public int updateAvatarById(Long userId,String url){
        User user = userMapper.selectByPrimaryKey(userId);
        user.setAvatarUrl(url);
        return userMapper.updateByPrimaryKey(user);
    }

    public int updateUsernameById(Long userId,String username){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user.getName().equals(username)) return 1;
        else user.setName(username);
        return userMapper.updateByPrimaryKey(user);
    }

    public Object registerOrLoginWithMail(String mail,String token) {

        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andEmailEqualTo(mail);
        List<User> users = userMapper.selectByExample(userExample);
        User updateUser = new User();
        if(users.size() != 0){//登录
            User dbUser = users.get(0);
            if(dbUser.getName()==null|| StringUtils.isBlank(dbUser.getName()))//数据库为空，当前为空
                updateUser.setName(getUserName("邮箱"));

            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setToken(token);
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
            return ResultDTO.okOf(token);
        }else {
            //注册
            updateUser.setEmail(mail);
            updateUser.setName(getUserName("邮箱"));
            updateUser.setToken(token);
            updateUser.setAvatarUrl("/images/avatar/"+(int)(Math.random()*11)+".jpg");
            updateUser.setGmtCreate(System.currentTimeMillis());
            updateUser.setGmtModified(updateUser.getGmtCreate());
            userMapper.insert(updateUser);
            UserExample example = new UserExample();
            example.createCriteria()
                    .andEmailEqualTo(mail);
            List<User> insertUsers = userMapper.selectByExample(example);
            User insertUser = insertUsers.get(0);
            initUserTable(insertUser,new UserInfo());
            return ResultDTO.okOf(token);
        }



    }

    public Object updateUserPhoneById(Long userId, String phone) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andPhoneEqualTo(phone);
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() != 0){
            return ResultDTO.errorOf(CustomizeErrorCode.SUBMIT_PHONE_FAILED);
        }
        User updateUser = new User();
        updateUser.setPhone(phone);
        UserExample example = new UserExample();
        example.createCriteria()
                .andIdEqualTo(userId);
        try{
            userMapper.updateByExampleSelective(updateUser, example);
            return ResultDTO.okOf("手机绑定/更新成功！！！");
        }catch (Exception e){
            return ResultDTO.errorOf(CustomizeErrorCode.SUBMIT_PHONE_FAILED);
        }
    }

    public Object registerOrLoginWithPhone(String phone, String token) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andPhoneEqualTo(phone);
        List<User> users = userMapper.selectByExample(userExample);
        User updateUser = new User();
        if(users.size() != 0){//登录
            User dbUser = users.get(0);
            if(dbUser.getName()==null|| StringUtils.isBlank(dbUser.getName()))//数据库为空，当前为空
                updateUser.setName(getUserName("手机"));

            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setToken(token);
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
            return ResultDTO.okOf(token);
        }else {
            //注册
            updateUser.setPhone(phone);
            updateUser.setName(getUserName("手机"));
            updateUser.setToken(token);
            double random = Math.random();
            updateUser.setAvatarUrl("/images/avatar/"+(int)(Math.random()*11)+".jpg");
            updateUser.setGmtCreate(System.currentTimeMillis());
            updateUser.setGmtModified(updateUser.getGmtCreate());
            userMapper.insert(updateUser);
            UserExample example = new UserExample();
            example.createCriteria()
                    .andPhoneEqualTo(phone);
            List<User> insertUsers = userMapper.selectByExample(example);
            User insertUser = insertUsers.get(0);
            initUserTable(insertUser,new UserInfo());
           /* UserInfo userInfo = new UserInfo();
            userInfo.setUserId(insertUser.getId());
            userInfoMapper.insert(userInfo);
            UserAccount userAccount = new UserAccount();
            userAccount = initUserAccount(userAccount);
            userAccount.setUserId(insertUser.getId());
            userAccountMapper.insert(userAccount);*/

            return ResultDTO.okOf(token);
        }

    }

    public int createOrUpdateQq(User user, User loginuser, UserInfo userInfo) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andQqAccountIdEqualTo(user.getQqAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        User updateUser = new User();
        if (users.size() == 0) {  // 插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            if(user.getName()==null|| StringUtils.isBlank(user.getName()))
                user.setName(getUserName("QQ"));
            if (loginuser == null) {//创建
                userMapper.insert(user);
                UserExample userExample2 = new UserExample();
                userExample2.createCriteria()
                        .andQqAccountIdEqualTo(user.getQqAccountId());
                List<User> users2 = userMapper.selectByExample(userExample2);
                if(users2.size() != 0){//表示user表已创建
                    User dbUser = users2.get(0);
                    initUserTable(dbUser,userInfo);
                   /* userInfo.setUserId(dbUser.getId());
                    userInfoMapper.insert(userInfo);
                    UserAccount userAccount = new UserAccount();
                    userAccount = initUserAccount(userAccount);
                    userAccount.setUserId(dbUser.getId());
                    userAccount.setGroupId(1);
                    userAccount.setVipRank(0);
                    userAccount.setScore(0);
                    userAccount.setScore1(0);
                    userAccount.setScore2(0);
                    userAccount.setScore3(0);
                    userAccountMapper.insert(userAccount);*/

                }
                return 1;
            }
            if (loginuser != null) {//绑定或者换绑
                updateUser.setQqAccountId(user.getQqAccountId());
                updateUserInfo(user,updateUser,loginuser,userInfo);
              /*  updateUser.setGmtModified(System.currentTimeMillis());
                updateUser.setToken(user.getToken());
                updateUser.setAvatarUrl(user.getAvatarUrl());
                UserExample example = new UserExample();
                example.createCriteria()
                        .andIdEqualTo(loginuser.getId());
                userMapper.updateByExampleSelective(updateUser, example);

                UserInfoExample userInfoExample = new UserInfoExample();
                userInfoExample.createCriteria()
                        .andUserIdEqualTo(loginuser.getId());
                List<UserInfo> dbUserInfos = userInfoMapper.selectByExample(userInfoExample);
                if(dbUserInfos.size() == 0){//信息为空插入
                    userInfo.setUserId(loginuser.getId());
                    userInfoMapper.insert(userInfo);
                }
                else{//信息不空更新
                    UserInfoExample userInfEexample = new UserInfoExample();
                    userInfEexample.createCriteria()
                            .andUserIdEqualTo(loginuser.getId());
                    userInfoMapper.updateByExampleSelective(userInfo, userInfEexample);
                }*/
                return 2;
            }



            // user.setName(getUserName("Github"));

        } else {
            //登录更新
            User dbUser = users.get(0);
            if(dbUser.getName()==null&&(user.getName()==null|| StringUtils.isBlank(user.getName())))//数据库为空，当前为空
                updateUser.setName(getUserName("QQ"));
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setToken(user.getToken());
            //updateUser.setAvatarUrl(user.getAvatarUrl());
            UserExample example = new UserExample();
            example.createCriteria()
                    .andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
            return 3;
        }
        return 0;
    }

    public UserAccount initUserAccount(UserAccount userAccount){
        userAccount.setGroupId(1);
        userAccount.setVipRank(0);
        userAccount.setScore(0);
        userAccount.setScore1(0);
        userAccount.setScore2(0);
        userAccount.setScore3(0);
        return userAccount;
    }

    public void initUserTable(User user,UserInfo userInfo){
       // UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfoMapper.insert(userInfo);
        UserAccount userAccount = new UserAccount();
        userAccount = initUserAccount(userAccount);
        userAccount.setUserId(user.getId());
        userAccountMapper.insert(userAccount);
        userInfo=null;
        userAccount=null;
    }

    public void updateUserInfo(User user,User updateUser,User loginuser,UserInfo userInfo){
        updateUser.setGmtModified(System.currentTimeMillis());
        updateUser.setToken(user.getToken());
        updateUser.setAvatarUrl(user.getAvatarUrl());
        UserExample example = new UserExample();
        example.createCriteria()
                .andIdEqualTo(loginuser.getId());
        userMapper.updateByExampleSelective(updateUser, example);

        UserInfoExample userInfoExample = new UserInfoExample();
        userInfoExample.createCriteria()
                .andUserIdEqualTo(loginuser.getId());
        List<UserInfo> dbUserInfos = userInfoMapper.selectByExample(userInfoExample);
        if(dbUserInfos.size() == 0){//信息为空插入
            userInfo.setUserId(loginuser.getId());
            userInfoMapper.insert(userInfo);
        }
        else{//信息不空更新
            UserInfoExample userInfEexample = new UserInfoExample();
            userInfEexample.createCriteria()
                    .andUserIdEqualTo(loginuser.getId());
            userInfoMapper.updateByExampleSelective(userInfo, userInfEexample);
        }

    }

}
