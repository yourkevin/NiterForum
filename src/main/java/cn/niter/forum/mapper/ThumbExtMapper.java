package cn.niter.forum.mapper;

import cn.niter.forum.dto.LikeQueryDTO;
import cn.niter.forum.model.Comment;
import cn.niter.forum.model.Question;

public interface ThumbExtMapper {
    int incLikeCount(Comment comment);

    int incQuestionLikeCount(Question question);

    Integer count(LikeQueryDTO likeQueryDTO);
}
