package cn.niter.forum.service;

import cn.niter.forum.dto.*;
import cn.niter.forum.enums.CommentTypeEnum;
import cn.niter.forum.enums.LikeTypeEnum;
import cn.niter.forum.enums.NotificationStatusEnum;
import cn.niter.forum.enums.NotificationTypeEnum;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import cn.niter.forum.mapper.*;
import cn.niter.forum.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LikeService {

    @Autowired
    private ThumbMapper thumbMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ThumbExtMapper thumbExtMapper;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private TalkMapper talkMapper;
    @Autowired
    private TalkExtMapper talkExtMapper;

    @Transactional
    public Object insert(ThumbDTO thumbDTO) {
        if (thumbDTO.getTargetId() == null || thumbDTO.getTargetId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (thumbDTO.getType() == null || !LikeTypeEnum.isExist(thumbDTO.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (thumbDTO.getType() == LikeTypeEnum.COMMENT.getType()||thumbDTO.getType() == LikeTypeEnum.SUB_COMMENT.getType()) {
            // 点赞评论
            Comment dbComment = commentMapper.selectByPrimaryKey(thumbDTO.getTargetId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            ThumbExample thumbExample = new ThumbExample();
            thumbExample.createCriteria()
                    .andTargetIdEqualTo(thumbDTO.getTargetId())
                    .andTypeEqualTo(thumbDTO.getType())
                    .andLikerEqualTo(thumbDTO.getUser().getId());
            List<Thumb> thumbs = thumbMapper.selectByExample(thumbExample);
            if(thumbs.size()>=1) return ResultDTO.errorOf(CustomizeErrorCode.CAN_NOT_LIKE_AGAIN);
            Thumb thumb = new Thumb();
            BeanUtils.copyProperties(thumbDTO, thumb);
            thumb.setLiker(thumbDTO.getUser().getId());
            thumbMapper.insert(thumb);

            if(thumb.getType() == LikeTypeEnum.COMMENT.getType()){
                if(dbComment.getType()== CommentTypeEnum.TALK.getType()){//如果该评论是在说说下的
                    //判断说说是否存在
                    Talk dbTalk = talkMapper.selectByPrimaryKey(dbComment.getParentId());
                    if (dbTalk == null) {
                        throw new CustomizeException(CustomizeErrorCode.TALK_NOT_FOUND);
                    }
                    // 增加点赞数
                    dbComment.setId(thumb.getTargetId());
                    dbComment.setLikeCount(1L);
                    thumbExtMapper.incLikeCount(dbComment);
                    // 创建通知
                    createNotify(thumb, dbComment.getCommentator(), thumbDTO.getUser().getName(), dbComment.getContent(), NotificationTypeEnum.LIKE_TALK_COMMENT, dbTalk.getId());
                }
                else if(dbComment.getType()== CommentTypeEnum.QUESTION.getType()){//如果该评论是在问题下的
                    // 获取点赞的评论的问题（方便返回）
                    Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
                    if (question == null) {
                        throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
                    }
                    // 增加点赞数
                    dbComment.setId(thumb.getTargetId());
                    dbComment.setLikeCount(1L);
                    thumbExtMapper.incLikeCount(dbComment);
                    // parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());
                    // 创建通知
                    createNotify(thumb, dbComment.getCommentator(), thumbDTO.getUser().getName(), dbComment.getContent(), NotificationTypeEnum.LIKE_COMMENT, question.getId());
                }


            }else if(thumb.getType() == LikeTypeEnum.SUB_COMMENT.getType()){
                // 增加点赞数
                dbComment.setId(thumb.getTargetId());
                dbComment.setLikeCount(1L);
                thumbExtMapper.incLikeCount(dbComment);
                createNotify(thumb, dbComment.getCommentator(), thumbDTO.getUser().getName(), dbComment.getContent(), NotificationTypeEnum.LIKE_SUB_COMMENT, dbComment.getParentId());
            }

        }
        else  if (thumbDTO.getType() == LikeTypeEnum.QUESTION.getType()){
            //点赞问题
            Question question = questionMapper.selectByPrimaryKey(thumbDTO.getTargetId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            ThumbExample thumbExample = new ThumbExample();
            thumbExample.createCriteria()
                    .andTargetIdEqualTo(thumbDTO.getTargetId())
                    .andTypeEqualTo(thumbDTO.getType())
                    .andLikerEqualTo(thumbDTO.getUser().getId());
            List<Thumb> thumbs = thumbMapper.selectByExample(thumbExample);
            if(thumbs.size()>=1) return ResultDTO.errorOf(CustomizeErrorCode.CAN_NOT_LIKE_QUESTION_AGAIN);
            Thumb thumb = new Thumb();
            BeanUtils.copyProperties(thumbDTO, thumb);
            thumb.setLiker(thumbDTO.getUser().getId());
            thumbMapper.insert(thumb);
            // 增加点赞数
            question.setId(thumb.getTargetId());
            question.setLikeCount(1);
            thumbExtMapper.incQuestionLikeCount(question);
            // 创建通知
            createNotify(thumb, question.getCreator(), thumbDTO.getUser().getName(), question.getTitle(), NotificationTypeEnum.LIKE_QUESTION, question.getId());
        }
        else  if (thumbDTO.getType() == LikeTypeEnum.TALK.getType()){
            //喜欢说说
            //判断说说是否存在
            Talk dbTalk = talkMapper.selectByPrimaryKey(thumbDTO.getTargetId());
            if (dbTalk == null) {
                throw new CustomizeException(CustomizeErrorCode.TALK_NOT_FOUND);
            }
            //判断说说是否已经喜欢过了
            ThumbExample thumbExample = new ThumbExample();
            thumbExample.createCriteria()
                    .andTargetIdEqualTo(thumbDTO.getTargetId())
                    .andTypeEqualTo(thumbDTO.getType())
                    .andLikerEqualTo(thumbDTO.getUser().getId());
            List<Thumb> thumbs = thumbMapper.selectByExample(thumbExample);
            if(thumbs.size()>=1) return ResultDTO.errorOf(CustomizeErrorCode.CAN_NOT_LIKE_QUESTION_AGAIN);
            //开始插入了
            Thumb thumb = new Thumb();
            BeanUtils.copyProperties(thumbDTO, thumb);
            thumb.setLiker(thumbDTO.getUser().getId());
            thumbMapper.insert(thumb);
            // 增加点赞数
            Talk talk = new Talk();
            talk.setId(thumb.getTargetId());
            talk.setLikeCount(1);
            talkExtMapper.updateByPrimaryKeySelective(talk);
            // 创建通知
            createNotify(thumb, dbTalk.getCreator(), thumbDTO.getUser().getName(), "快来看看啥这么受欢迎~", NotificationTypeEnum.LIKE_TALK, thumb.getTargetId());
            return ResultDTO.okOf("喜欢成功！");
        }

        return ResultDTO.okOf("点赞/收藏成功！");
    }

    @Deprecated
    @Transactional
    public int insert(Thumb thumb, UserDTO user) {
        if (thumb.getTargetId() == null || thumb.getTargetId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (thumb.getType() == null || !LikeTypeEnum.isExist(thumb.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (thumb.getType() == LikeTypeEnum.COMMENT.getType()||thumb.getType() == LikeTypeEnum.SUB_COMMENT.getType()) {
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

            if(thumb.getType() == LikeTypeEnum.COMMENT.getType()){
                // 获取点赞的评论的问题（方便返回）
                Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
                if (question == null) {
                    throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
                }
                // 增加点赞数
                dbComment.setId(thumb.getTargetId());
                dbComment.setLikeCount(1L);
                thumbExtMapper.incLikeCount(dbComment);
                // parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());
                // 创建通知
                createNotify(thumb, dbComment.getCommentator(), user.getName(), dbComment.getContent(), NotificationTypeEnum.LIKE_COMMENT, question.getId());
            }else if(thumb.getType() == LikeTypeEnum.SUB_COMMENT.getType()){
                // 增加点赞数
                dbComment.setId(thumb.getTargetId());
                dbComment.setLikeCount(1L);
                thumbExtMapper.incLikeCount(dbComment);
                createNotify(thumb, dbComment.getCommentator(), user.getName(), dbComment.getContent(), NotificationTypeEnum.LIKE_SUB_COMMENT, dbComment.getParentId());
            }

            }
        else  if (thumb.getType() == LikeTypeEnum.QUESTION.getType()){
            //收藏问题
            Question question = questionMapper.selectByPrimaryKey(thumb.getTargetId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            ThumbExample thumbExample = new ThumbExample();
            thumbExample.createCriteria()
                    .andTargetIdEqualTo(thumb.getTargetId())
                    .andTypeEqualTo(thumb.getType())
                    .andLikerEqualTo(thumb.getLiker());
            List<Thumb> thumbs = thumbMapper.selectByExample(thumbExample);
            if(thumbs.size()>=1) return 2023;
            thumbMapper.insert(thumb);


            // 增加点赞数
            question.setId(thumb.getTargetId());
            question.setLikeCount(1);
            thumbExtMapper.incQuestionLikeCount(question);

            // 创建通知
            createNotify(thumb, question.getCreator(), user.getName(), question.getTitle(), NotificationTypeEnum.LIKE_QUESTION, question.getId());

        }


           return 0;
            }



    private void createNotify(Thumb thumb, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        if (receiver == thumb.getLiker()) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(thumb.getLiker());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    @Deprecated
    public Object removeLikeByIdAndType(Long userId, Long id, Integer type) {

        ThumbExample thumbExample = new ThumbExample();
        thumbExample.createCriteria().andLikerEqualTo(userId).andTypeEqualTo(type).andTargetIdEqualTo(id);
        int c = thumbMapper.deleteByExample(thumbExample);
        if(c==0) {
            return ResultDTO.errorOf("哎呀，该收藏已移除或您无权移除！");
        }
        else {
            return ResultDTO.okOf("恭喜您，成功移除"+c+"条收藏！");
        }
       // return thumbMapper.deleteByExample(thumbExample);

    }

    public int queryLike(Long targetId, Integer type, Long liker) {
        
        ThumbExample thumbExample = new ThumbExample();
        thumbExample.createCriteria().andLikerEqualTo(liker).andTypeEqualTo(type).andTargetIdEqualTo(targetId);
        return thumbMapper.selectByExample(thumbExample).size();
    }

    public PaginationDTO list(LikeQueryDTO likeQueryDTO) {
        Integer totalPage;
        Integer totalCount = thumbExtMapper.count(likeQueryDTO);
        ThumbExample thumbExample = new ThumbExample();
        thumbExample.createCriteria().andLikerEqualTo(likeQueryDTO.getLiker()).andTypeEqualTo(likeQueryDTO.getType()).andTargetIdEqualTo(likeQueryDTO.getTargetId());
        if (totalCount % likeQueryDTO.getSize() == 0) {
            totalPage = totalCount / likeQueryDTO.getSize();
        } else {
            totalPage = totalCount / likeQueryDTO.getSize() + 1;
        }
        if (likeQueryDTO.getPage() > totalPage) {
            likeQueryDTO.setPage(totalPage);
        }

        Integer offset = likeQueryDTO.getPage() < 1 ? 0 : likeQueryDTO.getSize() * (likeQueryDTO.getPage() - 1);
        likeQueryDTO.setOffset(offset);
        thumbExample.setOrderByClause(likeQueryDTO.getSort()+" "+likeQueryDTO.getOrder());
        List<Thumb> thumbs = thumbMapper.selectByExampleWithRowbounds(thumbExample,new RowBounds(likeQueryDTO.getSize()*(likeQueryDTO.getPage()-1), likeQueryDTO.getSize()));
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setTotalCount(totalCount);
        if (thumbs.size() == 0) {
            paginationDTO.setPage(0);
            paginationDTO.setTotalPage(0);
            paginationDTO.setData(new ArrayList<>());
            return paginationDTO;
        }
        paginationDTO.setData(thumbs);
        paginationDTO.setPagination(totalPage,likeQueryDTO.getPage());
        return paginationDTO;
    }


    public Object remove(ThumbDTO thumbDTO) {
        ThumbExample thumbExample = new ThumbExample();
        thumbExample.createCriteria().andLikerEqualTo(thumbDTO.getUser().getId()).andTypeEqualTo(thumbDTO.getType()).andTargetIdEqualTo(thumbDTO.getTargetId());
        int c = thumbMapper.deleteByExample(thumbExample);
        if(c==0) {
            return ResultDTO.errorOf("哎呀，该收藏已移除或您无权移除！");
        }
        else {
            if(thumbDTO.getType()==LikeTypeEnum.TALK.getType()){
                Talk talk = new Talk();
                talk.setId(thumbDTO.getTargetId());
                talk.setLikeCount(-1);
                talkExtMapper.updateByPrimaryKeySelective(talk);
            }
            return ResultDTO.okOf("恭喜您，成功移除"+c+"条收藏！");
        }
    }
}

