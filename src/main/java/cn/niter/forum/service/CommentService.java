package cn.niter.forum.service;

import cn.niter.forum.dto.*;
import cn.niter.forum.enums.CommentTypeEnum;
import cn.niter.forum.enums.NotificationStatusEnum;
import cn.niter.forum.enums.NotificationTypeEnum;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import cn.niter.forum.mapper.*;
import cn.niter.forum.model.*;
import cn.niter.forum.util.TimeUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private UserAccountExtMapper userAccountExtMapper;

   @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private TimeUtils timeUtils;

    @Value("${score1.comment.inc}")
    private Integer score1CommentInc;
    @Value("${score2.comment.inc}")
    private Integer score2CommentInc;
    @Value("${score3.comment.inc}")
    private Integer score3CommentInc;
    @Value("${score1.commented.inc}")
    private Integer score1CommentedInc;
    @Value("${score2.commented.inc}")
    private Integer score2CommentedInc;
    @Value("${score3.commented.inc}")
    private Integer score3CommentedInc;
    @Value("${user.score1.priorities}")
    private Integer score1Priorities;
    @Value("${user.score2.priorities}")
    private Integer score2Priorities;
    @Value("${user.score3.priorities}")
    private Integer score3Priorities;

    @Value("${user.group.r1.max}")
    private Integer r1Max;
    @Value("${user.group.r2.max}")
    private Integer r2Max;
    @Value("${user.group.r3.max}")
    private Integer r3Max;
    @Value("${user.group.r4.max}")
    private Integer r4Max;
    @Value("${user.group.r5.max}")
    private Integer r5Max;

    private Integer[] rMaxs = null;

    //懒汉式单例获取rMaxs
    private Integer[] getRMaxs(){
        if (rMaxs == null) {
            rMaxs = new Integer[]{r1Max,r2Max,r3Max,r4Max,r5Max};
        }
        return rMaxs;
    }

    @Deprecated
    @Transactional
    public void insert(Comment comment, UserDTO commentator,UserAccount userAccount) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            // 回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            // 获取回复的问题
            Question question = updateQuestionAfterComment(dbComment.getParentId());
            comment.setCommentCount(0);
            commentMapper.insert(comment);

            // 增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());

            // 创建通知
            createNotify(comment, dbComment.getCommentator(), commentator.getName(), parentComment.getContent(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
        }
        if (comment.getType() == CommentTypeEnum.SUB_COMMENT.getType()) {
            // 回复子评论

            // 获取回复的子评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            // 获取回复的评论
            Comment dbComment2 = commentMapper.selectByPrimaryKey(dbComment.getParentId());
            if (dbComment2 == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            // 获取回复的问题
            Question question = updateQuestionAfterComment(dbComment2.getParentId());
            Comment insertComment  = comment;
            insertComment.setParentId(dbComment.getParentId());//设置回复评论方便读取
            insertComment.setType(2);//设回2方便读取
            insertComment.setCommentCount(0);
            commentMapper.insert(insertComment);

            // 增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(dbComment2.getId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            //parentComment = commentMapper.selectByPrimaryKey(dbComment2.getId());

            // 创建通知
            createNotify(comment, dbComment.getCommentator(), commentator.getName(), dbComment.getContent(), NotificationTypeEnum.REPLY_SUB_COMMENT, question.getId());
        }
        if (comment.getType() == CommentTypeEnum.QUESTION.getType()) {
            // 回复问题
            Question question = updateQuestionAfterComment(comment.getParentId());
            comment.setCommentCount(0);
            commentMapper.insert(comment);


            // 创建通知
            createNotify(comment, question.getCreator(), commentator.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
        if(userAccount.getVipRank()!=0){//VIP积分策略，可自行修改，这里简单处理
            score1CommentInc=score1CommentInc*2;
            score2CommentInc=score2CommentInc*2;
        }
        userAccount = new UserAccount();
        userAccount.setUserId(comment.getCommentator());
        userAccount.setScore1(score1CommentInc);
        userAccount.setScore2(score2CommentInc);
        userAccount.setScore3(score3CommentInc);
        userAccount.setScore(score1CommentInc*score1Priorities+score2CommentInc*score2Priorities+score3CommentInc*score3Priorities);
        userAccountExtMapper.incScore(userAccount);
        updateUserAccoundByUserId(comment.getCommentator());
        userAccount=null;
    }

    private Question updateQuestionAfterComment(Long questionId){
        Question question = questionMapper.selectByPrimaryKey(questionId);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        //给问题设置最后回复时间
        question.setGmtLatestComment(System.currentTimeMillis());
        int updated = questionMapper.updateByPrimaryKeySelective(question);
        /*QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andIdEqualTo(question.getId());
        int updated = questionMapper.updateByExampleSelective(question, example);*/
        if (updated != 1) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        question.setCommentCount(1);
        questionExtMapper.incCommentCount(question);
        return question;
    }

    private void updateUserAccoundByUserId(Long userId){
        UserAccountExample userAccountExample = new UserAccountExample();
        userAccountExample.createCriteria().andUserIdEqualTo(userId);
        List<UserAccount> userAccounts = userAccountMapper.selectByExample(userAccountExample);
        UserAccount userAccount = userAccounts.get(0);
        int groupId=userAccount.getGroupId();
        int score=userAccount.getScore();
        Integer[] rMaxs = getRMaxs();
        Integer nowGroupId=1;
        for (Integer rMax : rMaxs) {
            if(score>rMax) nowGroupId++;
            else break;
        }
        if(groupId>0&&groupId<10&&nowGroupId!=groupId){
            userAccount.setGroupId(nowGroupId);
            userAccountMapper.updateByExample(userAccount,userAccountExample);
        }
    }

    private void createNotify(Comment comment, Long receiver, String notifierName, String outerTitle, NotificationTypeEnum notificationType, Long outerId) {
        if (receiver == comment.getCommentator()) {
            return;
        }
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(outerId);
        notification.setNotifier(comment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notification.setNotifierName(notifierName);
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);

        UserAccountExample userAccountExample = new UserAccountExample();
        userAccountExample.createCriteria().andUserIdEqualTo(receiver);
        List<UserAccount> userAccounts = userAccountMapper.selectByExample(userAccountExample);
        UserAccount userAccount = userAccounts.get(0);
        if(userAccount.getVipRank()!=0){//VIP积分策略，可自行修改，这里简单处理
            score1CommentedInc=score1CommentedInc*2;
            score2CommentedInc=score2CommentedInc*2;
        }
        userAccount.setUserId(receiver);
        userAccount.setScore1(score1CommentedInc);
        userAccount.setScore2(score2CommentedInc);
        userAccount.setScore3(score3CommentedInc);
        userAccount.setScore(score1CommentedInc*score1Priorities+score2CommentedInc*score2Priorities+score3CommentedInc*score3Priorities);
        userAccountExtMapper.incScore(userAccount);
        updateUserAccoundByUserId(receiver);
        userAccount=null;
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(type.getType());
        //commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);

        if (comments.size() == 0) {
            return new ArrayList<>();
        }
        // 获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList();
        userIds.addAll(commentators);


        // 获取评论人并转换为 Map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);

        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));



        // 转换 comment 为 commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userMap.get(comment.getCommentator()),userDTO);
            commentDTO.setUser(userDTO);
            UserAccountExample userAccountExample = new UserAccountExample();
            userAccountExample.createCriteria().andUserIdEqualTo(userMap.get(comment.getCommentator()).getId());
            List<UserAccount> userAccounts = userAccountMapper.selectByExample(userAccountExample);
            commentDTO.setUserAccount(userAccounts.get(0));
            commentDTO.setGmtModifiedStr(timeUtils.getTime(commentDTO.getGmtModified(),null));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }

    @Deprecated
    public int delCommentByIdAndType(Long userId, Integer groupId, Long id, Integer type) {
        int c=0;
        Comment comment = commentMapper.selectByPrimaryKey(id);
        if(groupId>=18){
            c=commentMapper.deleteByPrimaryKey(id);
        }
        else {
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andIdEqualTo(id).andCommentatorEqualTo(userId);
            c = commentMapper.deleteByExample(commentExample);
        }
        if(c>0&&type==1){
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andTypeEqualTo(2).andParentIdEqualTo(id);
            c+=commentMapper.deleteByExample(commentExample);
        }
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(comment.getCommentator());
        userAccount.setScore1(score1CommentInc);
        userAccount.setScore2(score2CommentInc);
        userAccount.setScore3(score3CommentInc);
        userAccount.setScore(score1CommentInc*score1Priorities+score2CommentInc*score2Priorities+score3CommentInc*score3Priorities);
        userAccountExtMapper.decScore(userAccount);
        userAccount=null;
        return c;
    }


    public int deleteByIdAndType(UserDTO userDTO, Long id, Integer type) {
        int c=0;
        Comment comment = commentMapper.selectByPrimaryKey(id);
        if(userDTO.getGroupId()>=18){//管理员可以删除
            c=commentMapper.deleteByPrimaryKey(id);
        }
        else if(userDTO.getId().longValue()==comment.getCommentator()){//评论者自己可以删除
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andIdEqualTo(id).andCommentatorEqualTo(userDTO.getId());
            c = commentMapper.deleteByExample(commentExample);
        }
        if(c>0&&type==1){
            CommentExample commentExample = new CommentExample();
            commentExample.createCriteria().andTypeEqualTo(2).andParentIdEqualTo(id);
            c+=commentMapper.deleteByExample(commentExample);
        }
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(comment.getCommentator());
        userAccount.setScore1(score1CommentInc);
        userAccount.setScore2(score2CommentInc);
        userAccount.setScore3(score3CommentInc);
        userAccount.setScore(score1CommentInc*score1Priorities+score2CommentInc*score2Priorities+score3CommentInc*score3Priorities);
        userAccountExtMapper.decScore(userAccount);
        userAccount=null;
        return c;
    }

    public PaginationDTO list(CommentQueryDTO commentQueryDTO){
        Integer totalPage;
        Integer totalCount = commentExtMapper.countBySearch(commentQueryDTO);
        CommentExample commentExample = new CommentExample();
        CommentExample.Criteria criteria = commentExample.createCriteria();
        if(commentQueryDTO.getCommentator()!=null)
            criteria.andCommentatorEqualTo(commentQueryDTO.getCommentator());
        if(commentQueryDTO.getId()!=null)
            criteria.andIdEqualTo(commentQueryDTO.getId());
        if(commentQueryDTO.getParentId()!=null)
            criteria.andParentIdEqualTo(commentQueryDTO.getParentId());
        if(commentQueryDTO.getType()!=null)
            criteria.andTypeEqualTo(commentQueryDTO.getType());

        if (totalCount % commentQueryDTO.getSize() == 0) {
            totalPage = totalCount / commentQueryDTO.getSize();
        } else {
            totalPage = totalCount / commentQueryDTO.getSize() + 1;
        }

        if (commentQueryDTO.getPage() > totalPage) {
            commentQueryDTO.setPage(totalPage);
        }

        Integer offset = commentQueryDTO.getPage() < 1 ? 0 : commentQueryDTO.getSize() * (commentQueryDTO.getPage() - 1);
        commentQueryDTO.setOffset(offset);

        commentExample.setOrderByClause(commentQueryDTO.getSort()+" "+commentQueryDTO.getOrder());
        //commentExample.setOrderByClause("gmt_modified desc");
        List<Comment> comments = commentMapper.selectByExampleWithRowbounds(commentExample,new RowBounds(commentQueryDTO.getSize()*(commentQueryDTO.getPage()-1), commentQueryDTO.getSize()));
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setTotalCount(totalCount);
        if (comments.size() == 0) {
            paginationDTO.setPage(0);
            paginationDTO.setTotalPage(0);
            paginationDTO.setData(new ArrayList<>());
            return paginationDTO;
        }
        // 获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList();
        userIds.addAll(commentators);


        // 获取评论人并转换为 Map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);

        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));



        // 转换 comment 为 commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userMap.get(comment.getCommentator()),userDTO);
            commentDTO.setUser(userDTO);
            UserAccountExample userAccountExample = new UserAccountExample();
            userAccountExample.createCriteria().andUserIdEqualTo(userMap.get(comment.getCommentator()).getId());
            List<UserAccount> userAccounts = userAccountMapper.selectByExample(userAccountExample);
            commentDTO.setUserAccount(userAccounts.get(0));
            commentDTO.setGmtModifiedStr(timeUtils.getTime(commentDTO.getGmtModified(),null));
            return commentDTO;
        }).collect(Collectors.toList());


        paginationDTO.setData(commentDTOS);
        paginationDTO.setPagination(totalPage,commentQueryDTO.getPage());
        return paginationDTO;

    }

    @Transactional
    public void insert(CommentDTO commentDTO, UserDTO userDTO) {

        if (commentDTO.getParentId() == null || commentDTO.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (commentDTO.getType() == null || !CommentTypeEnum.isExist(commentDTO.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        Comment comment = new Comment();
        if (commentDTO.getType() == CommentTypeEnum.COMMENT.getType()) {
            // 回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(commentDTO.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            // 获取回复的问题
            Question question = updateQuestionAfterComment(dbComment.getParentId());
            BeanUtils.copyProperties(commentDTO,comment);
            commentMapper.insert(comment);

            // 增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            parentComment = commentMapper.selectByPrimaryKey(comment.getParentId());

            // 创建通知
            createNotify(comment, dbComment.getCommentator(), userDTO.getName(), parentComment.getContent(), NotificationTypeEnum.REPLY_COMMENT, question.getId());
        }
        if (commentDTO.getType() == CommentTypeEnum.SUB_COMMENT.getType()) {
            // 回复子评论

            // 获取回复的子评论
            Comment dbComment = commentMapper.selectByPrimaryKey(commentDTO.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            // 获取回复的评论
            Comment dbComment2 = commentMapper.selectByPrimaryKey(dbComment.getParentId());
            if (dbComment2 == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            // 获取回复的问题
            Question question = updateQuestionAfterComment(dbComment2.getParentId());
            BeanUtils.copyProperties(commentDTO,comment);
           // Comment insertComment  = comment;
            comment.setParentId(dbComment.getParentId());//设置回复评论方便读取
            comment.setType(2);//设回2方便读取
            //insertComment.setCommentCount(0);
            commentMapper.insert(comment);

            // 增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(dbComment2.getId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            //parentComment = commentMapper.selectByPrimaryKey(dbComment2.getId());

            // 创建通知
            createNotify(comment, dbComment.getCommentator(), userDTO.getName(), dbComment.getContent(), NotificationTypeEnum.REPLY_SUB_COMMENT, question.getId());
        }
        if (commentDTO.getType() == CommentTypeEnum.QUESTION.getType()) {
            // 回复问题
            Question question = updateQuestionAfterComment(commentDTO.getParentId());
            //comment.setCommentCount(0);
            BeanUtils.copyProperties(commentDTO,comment);
            commentMapper.insert(comment);


            // 创建通知
            createNotify(comment, question.getCreator(), userDTO.getName(), question.getTitle(), NotificationTypeEnum.REPLY_QUESTION, question.getId());
        }
        if(userDTO.getVipRank()!=0){//VIP积分策略，可自行修改，这里简单处理
            score1CommentInc=score1CommentInc*2;
            score2CommentInc=score2CommentInc*2;
        }
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(commentDTO.getCommentator());
        userAccount.setScore1(score1CommentInc);
        userAccount.setScore2(score2CommentInc);
        userAccount.setScore3(score3CommentInc);
        userAccount.setScore(score1CommentInc*score1Priorities+score2CommentInc*score2Priorities+score3CommentInc*score3Priorities);
        userAccountExtMapper.incScore(userAccount);
        updateUserAccoundByUserId(commentDTO.getCommentator());
        userAccount=null;


    }
}
