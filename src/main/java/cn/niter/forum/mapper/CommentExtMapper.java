package cn.niter.forum.mapper;

import cn.niter.forum.dto.CommentQueryDTO;
import cn.niter.forum.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);

    Integer countBySearch(CommentQueryDTO commentQueryDTO);

}
