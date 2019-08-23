package cc.ikevin.community.mapper;

import cc.ikevin.community.model.Comment;
import cc.ikevin.community.model.Question;

public interface ThumbExtMapper {
    int incLikeCount(Comment comment);

    int incQuestionLikeCount(Question question);
}
