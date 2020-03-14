package cn.niter.forum.mapper;

import cn.niter.forum.model.UserAccount;

public interface UserAccountExtMapper {

    int incScore(UserAccount userAccount);
    int decScore(UserAccount userAccount);
}
