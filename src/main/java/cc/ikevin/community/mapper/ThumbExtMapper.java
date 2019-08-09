package cc.ikevin.community.mapper;

import cc.ikevin.community.model.Comment;

public interface ThumbExtMapper {
    int incLikeCount(Comment comment);
}
