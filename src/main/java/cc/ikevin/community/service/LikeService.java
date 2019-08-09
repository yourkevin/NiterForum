package cc.ikevin.community.service;

import cc.ikevin.community.enums.LikeTypeEnum;
import cc.ikevin.community.exception.CustomizeErrorCode;
import cc.ikevin.community.exception.CustomizeException;
import cc.ikevin.community.mapper.CommentMapper;
import cc.ikevin.community.mapper.ThumbExtMapper;
import cc.ikevin.community.mapper.ThumbMapper;
import cc.ikevin.community.model.Comment;
import cc.ikevin.community.model.Thumb;
import cc.ikevin.community.model.ThumbExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LikeService {

    @Autowired
    private ThumbMapper thumbMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ThumbExtMapper thumbExtMapper;

    @Transactional
    public int insert(Thumb thumb) {
        if (thumb.getTargetId() == null || thumb.getTargetId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (thumb.getType() == null || !LikeTypeEnum.isExist(thumb.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (thumb.getType() == LikeTypeEnum.COMMENT.getType()) {
            // 点赞评论
            Comment dbComment = commentMapper.selectByPrimaryKey(thumb.getTargetId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            ThumbExample thumbExample = new ThumbExample();
            thumbExample.createCriteria()
                    .andTargetIdEqualTo(thumb.getTargetId())
                    .andTypeEqualTo(thumb.getType())
                    .andLikerEqualTo(thumb.getLiker());
            List<Thumb> thumbs = thumbMapper.selectByExample(thumbExample);
            if(thumbs.size()>=1) return 2022;
            thumbMapper.insert(thumb);

            // 增加点赞数
            dbComment.setId(thumb.getTargetId());
            dbComment.setLikeCount(1L);
            thumbExtMapper.incLikeCount(dbComment);
           // parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());

            }
        else {
            //点赞问题（待实现）

        }
           return 0;
            }

    }

