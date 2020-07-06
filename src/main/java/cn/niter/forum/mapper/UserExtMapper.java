package cn.niter.forum.mapper;

import cn.niter.forum.dto.UserQueryDTO;
import cn.niter.forum.model.User;

import java.util.List;

public interface UserExtMapper {
    List<User> selectLatestLoginUser(UserQueryDTO userQueryDTO);
    Integer count(UserQueryDTO userQueryDTO);
}
