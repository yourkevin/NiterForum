package cn.niter.forum.service;

import cn.niter.forum.dto.PaginationDTO;
import cn.niter.forum.dto.QuestionDTO;
import cn.niter.forum.dto.QuestionQueryDTO;
import cn.niter.forum.dto.UserDTO;
import cn.niter.forum.enums.SortEnum;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import cn.niter.forum.mapper.*;
import cn.niter.forum.model.*;
import cn.niter.forum.util.TimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAccountMapper userAccountMapper;
    @Autowired
    private UserAccountExtMapper userAccountExtMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private ThumbMapper thumbMapper;
    @Autowired
    private TimeUtils timeUtils;
    @Autowired
    private LikeService likeService;
    @Autowired
    private UserAccountService userAccountService;

    @Value("${score1.publish.inc}")
    private Integer score1PublishInc;
    @Value("${score2.publish.inc}")
    private Integer score2PublishInc;
    @Value("${score3.publish.inc}")
    private Integer score3PublishInc;
    @Value("${user.score1.priorities}")
    private Integer score1Priorities;
    @Value("${user.score2.priorities}")
    private Integer score2Priorities;
    @Value("${user.score3.priorities}")
    private Integer score3Priorities;

    @Autowired
    private Environment env;

    public PaginationDTO listwithColumn(String search, String tag, String sort, Integer page, Integer size, Integer column2,UserAccount userAccount) {

        if (StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
            search = Arrays
                    .stream(tags)
                    .filter(StringUtils::isNotBlank)
                    .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("|"));
        }


        //
        Integer totalPage;
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        if(column2!=null) questionQueryDTO.setColumn2(column2);
        if(StringUtils.isNotBlank(tag)){
            tag = tag.replace("+", "").replace("*", "").replace("?", "");
            questionQueryDTO.setTag(tag);
        }

      //  ColumnEnum columnEnum = ColumnEnum.

        for (SortEnum sortEnum : SortEnum.values()) {
            if (sortEnum.name().toLowerCase(Locale.ENGLISH).equals(sort)) {
                questionQueryDTO.setSort(sort);

                if (sortEnum == SortEnum.HOT7) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 7);
                }
                if (sortEnum == SortEnum.HOT30) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30);
                }
                break;
            }
        }


        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        Integer offset = page < 1 ? 0 : size * (page - 1);
        questionQueryDTO.setSize(size);
        questionQueryDTO.setOffset(offset);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : questions) {
         User user = userMapper.selectByPrimaryKey(question.getCreator());
            UserAccountExample userAccountExample2 = new UserAccountExample();
            userAccountExample2.createCriteria().andUserIdEqualTo(user.getId());
         //   System.out.println("user.getId()："+user.getId());
            List<UserAccount> userAccounts = userAccountMapper.selectByExample(userAccountExample2);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user,userDTO);
            questionDTO.setUser(userDTO);
            questionDTO.setUserAccount(userAccounts.get(0));
            questionDTO.setUserGroupName(env.getProperty("user.group.r"+userAccounts.get(0).getGroupId()));
            questionDTO.setGmtLatestCommentStr(timeUtils.timeStamp2Date(questionDTO.getGmtLatestComment(),null));
            String shortDescription=getShortDescription(questionDTO.getDescription(),questionDTO.getId());
            questionDTO.setShortDescription(shortDescription);
            if(questionDTO.getPermission()==0) questionDTO.setIsVisible(1);
            else if(userAccount!=null&&userAccount.getGroupId()>=questionDTO.getPermission()) questionDTO.setIsVisible(1);
            else questionDTO.setIsVisible(0);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);


        paginationDTO.setPagination(totalPage,page);
        return paginationDTO;
    }

    public static List<String> getHtmlImageSrcList(String htmlText)
    {
        List<String> imgSrc = new ArrayList<String>();
        Matcher m = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(htmlText);
        while(m.find())
        {
            imgSrc.add(m.group(1));
        }
        return imgSrc;
    }

    public String getTextDescriptionFromHtml(String html){
        String textDescription = html.replaceAll("</?[^>]+>", ""); //剔出<html>的标签
        textDescription = textDescription.replaceAll("<a>\\s*|\t|\r|\n</a>", "");//去除字符串中的空格,回车,换行符,制表符
        textDescription = textDescription.replaceAll("&nbsp;", "");//去除&nbsp;
        return textDescription;
    }

    public String getShortDescription(String description,Long id){
        String shortDescription = description;

        //System.out.println(imgSrc);
        shortDescription = shortDescription.replaceAll("<p id=\"descriptionP\" class=\"video\">","" );
        shortDescription = shortDescription.replaceAll("<p class=\"video\" id=\"descriptionP\">","" );
        shortDescription = shortDescription.replaceAll("<p id=\"descriptionP\">", "");
        shortDescription = shortDescription.replaceAll("</p>", "<br>");//去除&nbsp;
        shortDescription = shortDescription.replaceAll("</h1>", "<br>");//去除&nbsp;
        shortDescription = shortDescription.replaceAll("<h1>", "");//去除&nbsp;
        shortDescription = shortDescription.replaceAll("</h2>", "<br>");//去除&nbsp;
        shortDescription = shortDescription.replaceAll("<h2>", "");//去除&nbsp;
        shortDescription = shortDescription.replaceAll("</h3>", "<br>");//去除&nbsp;
        shortDescription = shortDescription.replaceAll("<h3>", "");//去除&nbsp;
        shortDescription = shortDescription.replaceAll("<br><br>", "<br>");//去除&nbsp;
        shortDescription = shortDescription.replaceAll("<p><br></p>", "");//去除&nbsp;
        shortDescription = shortDescription.replaceAll("<p><br>", "<p>");//去除&nbsp;
        shortDescription = shortDescription.replaceAll("<br></p>", "</p>");//去除&nbsp;
       // shortDescription = shortDescription.replaceAll("<br>", "</br>");//去除&nbsp;
        // shortDescription = shortDescription.replaceAll("</?[^>]+>", ""); //剔出<html>的标签
        shortDescription = shortDescription.replaceAll("(?!<(br|p|img).*?>)<.*?>", ""); //剔出指定标签外的<iframe>的标签
        List<String> imgSrc = getHtmlImageSrcList(shortDescription);
        shortDescription = shortDescription.replaceAll("(?!<(br|p).*?>)<.*?>", ""); //剔出指定标签外的<html>的标签
        //  shortDescription = shortDescription.replaceAll("<a>\\s*|\t|\r|\n</a>", "");//去除字符串中的空格,回车,换行符,制表符

        shortDescription = shortDescription.replaceAll("&nbsp;", "");//去除&nbsp;
        shortDescription = shortDescription.replaceAll("&nbsp;", "");//去除&nbsp;

        shortDescription = shortDescription.replaceAll("<pre>", "");
        shortDescription = shortDescription.replaceAll("</pre>", "");
        if(shortDescription.length()>200) shortDescription=shortDescription.substring(0,200)+"...";
        if(imgSrc.size()>0) shortDescription=shortDescription+"<img src=\""+imgSrc.get(0)+"\">";
        return shortDescription;
    }


    public PaginationDTO list(String search, String tag, String sort, Integer page, Integer size) {

        if (StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
            search = Arrays
                    .stream(tags)
                    .filter(StringUtils::isNotBlank)
                    .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("|"));
        }


        //
        Integer totalPage;
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);

        if(StringUtils.isNotBlank(tag)){
            tag = tag.replace("+", "").replace("*", "").replace("?", "");
            questionQueryDTO.setTag(tag);
        }


        for (SortEnum sortEnum : SortEnum.values()) {
            if (sortEnum.name().toLowerCase(Locale.ENGLISH).equals(sort)) {
                questionQueryDTO.setSort(sort);

                if (sortEnum == SortEnum.HOT7) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 7);
                }
                if (sortEnum == SortEnum.HOT30) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30);
                }
                break;
            }
        }


        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        if (page < 1) {
            page = 1;
        }
        if (page > totalPage) {
            page = totalPage;
        }

        Integer offset = page < 1 ? 0 : size * (page - 1);
        questionQueryDTO.setSize(size);
        questionQueryDTO.setOffset(offset);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user,userDTO);
            questionDTO.setUser(userDTO);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);


        paginationDTO.setPagination(totalPage,page);
        return paginationDTO;
    }

    public List<QuestionDTO> listTopwithColumn(String search, String tag, String sort, Integer column2) {

        if (StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
            search = Arrays
                    .stream(tags)
                    .filter(StringUtils::isNotBlank)
                    .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("|"));
        }
        //
      //  Integer totalPage;
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        if(column2!=null) questionQueryDTO.setColumn2(column2);
        questionQueryDTO.setSearch(search);

        if(StringUtils.isNotBlank(tag)){
            tag = tag.replace("+", "").replace("*", "").replace("?", "");
            questionQueryDTO.setTag(tag);
        }


        for (SortEnum sortEnum : SortEnum.values()) {
            if (sortEnum.name().toLowerCase(Locale.ENGLISH).equals(sort)) {
                questionQueryDTO.setSort(sort);

                if (sortEnum == SortEnum.HOT7) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 7);
                }
                if (sortEnum == SortEnum.HOT30) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30);
                }
                break;
            }
        }

        List<Question> questions = questionExtMapper.selectTop(questionQueryDTO);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
      //  PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            UserAccountExample userAccountExample = new UserAccountExample();
            userAccountExample.createCriteria().andUserIdEqualTo(user.getId());
            List<UserAccount> userAccounts = userAccountMapper.selectByExample(userAccountExample);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user,userDTO);
            questionDTO.setUser(userDTO);
            questionDTO.setUserAccount(userAccounts.get(0));
            questionDTOList.add(questionDTO);
        }

        return questionDTOList;
    }

    public List<QuestionDTO> listTop(String search, String tag, String sort) {

        if (StringUtils.isNotBlank(search)) {
            String[] tags = StringUtils.split(search, " ");
            search = Arrays
                    .stream(tags)
                    .filter(StringUtils::isNotBlank)
                    .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("|"));
        }
        //
        //  Integer totalPage;
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);

        if(StringUtils.isNotBlank(tag)){
            tag = tag.replace("+", "").replace("*", "").replace("?", "");
            questionQueryDTO.setTag(tag);
        }


        for (SortEnum sortEnum : SortEnum.values()) {
            if (sortEnum.name().toLowerCase(Locale.ENGLISH).equals(sort)) {
                questionQueryDTO.setSort(sort);

                if (sortEnum == SortEnum.HOT7) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 7);
                }
                if (sortEnum == SortEnum.HOT30) {
                    questionQueryDTO.setTime(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30);
                }
                break;
            }
        }

        List<Question> questions = questionExtMapper.selectTop(questionQueryDTO);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        //  PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user,userDTO);
            questionDTO.setUser(userDTO);
            questionDTOList.add(questionDTO);
        }

        return questionDTOList;
    }

    public PaginationDTO listByExample(Long userId, Integer page, Integer size, String likes) {
        Integer totalPage;
        ThumbExample thumbExample = new ThumbExample();
        thumbExample.createCriteria()
                .andLikerEqualTo(userId)
                .andTypeEqualTo(1);
        Integer totalCount = (int)thumbMapper.countByExample(thumbExample);
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page > totalPage) {
            page = totalPage;
        }

        if (page < 1) {
            page = 1;
        }


        Integer offset = size * (page-1);

        thumbExample.setOrderByClause("gmt_modified desc");
        List<Thumb> thumbs = thumbMapper.selectByExampleWithRowbounds(thumbExample,new RowBounds(offset, size));
        List<Question> questionList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        List<QuestionDTO> questionDTOList = new ArrayList<>();
       // Question question = new Question();
        for (Thumb thumb : thumbs) {
            questionList.add(questionMapper.selectByPrimaryKey(thumb.getTargetId()));
        }
        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user,userDTO);
            questionDTO.setUser(userDTO);
            questionDTOList.add(questionDTO);
        }

     /*   QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        example.setOrderByClause("gmt_modified desc");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();*/
       /* for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }*/
        paginationDTO.setData(questionDTOList);
        paginationDTO.setTotalCount(totalCount);
        paginationDTO.setPagination(totalPage,page);
        return paginationDTO;
    }

    public PaginationDTO listByUserId(Long userId, Integer page, Integer size) {

        Integer totalPage;
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }

        if (page > totalPage) {
            page = totalPage;
        }

        if (page < 1) {
            page = 1;
        }

        Integer offset = size * (page-1);
        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo(userId);
        example.setOrderByClause("gmt_modified desc");
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(example, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user,userDTO);
            questionDTO.setUser(userDTO);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        paginationDTO.setTotalCount(totalCount);
        paginationDTO.setPagination(totalPage,page);
        return paginationDTO;

    }

    public QuestionDTO getById(Long id,Long viewUser_id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        UserAccountExample userAccountExample = new UserAccountExample();
        userAccountExample.createCriteria().andUserIdEqualTo(user.getId());
        List<UserAccount> userAccounts = userAccountMapper.selectByExample(userAccountExample);
        UserAccount userAccount = userAccounts.get(0);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user,userDTO);
        questionDTO.setUser(userDTO);
        questionDTO.setUserAccount(userAccount);
        questionDTO = setStatuses(questionDTO,viewUser_id);
        return questionDTO;
    }

    public void createOrUpdate(Question question,UserAccount userAccount) {
        if (question.getId() == null) {
            // 创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setGmtLatestComment(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            question.setStatus(0);
            questionMapper.insert(question);
            if(userAccount.getVipRank()!=0){//VIP积分策略，可自行修改，这里简单处理
                score1PublishInc=score1PublishInc*2;
                score2PublishInc=score2PublishInc*2;
                score3PublishInc=score2PublishInc*3;
            }
            userAccount = new UserAccount();
            userAccount.setUserId(question.getCreator());
            userAccount.setScore1(score1PublishInc);
            userAccount.setScore2(score2PublishInc);
            userAccount.setScore3(score3PublishInc);
            userAccount.setScore(score1PublishInc*score1Priorities+score2PublishInc*score2Priorities+score3PublishInc*score3Priorities);
            userAccountExtMapper.incScore(userAccount);
            userAccount=null;

        } else {
            // 更新
         question.setGmtModified(System.currentTimeMillis());
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(question, example);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

/*
    public void incScore(UserAccount userAccount) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);

    }*/

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);

    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        String regexpTag = Arrays
                .stream(tags)
                .filter(StringUtils::isNotBlank)
                .map(t -> t.replace("+", "").replace("*", "").replace("?", ""))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);

        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());
        return questionDTOS;
    }


    public int delQuestionById(Long user_id, Integer group_id,Long id) {
        int c=0;
        Question question = questionMapper.selectByPrimaryKey(id);
        if(group_id>=18){
            c=questionMapper.deleteByPrimaryKey(id);
        }
        else {
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andCreatorEqualTo(user_id).andIdEqualTo(id);
            c = questionMapper.deleteByExample(questionExample);
        }
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(question.getCreator());
        userAccount.setScore1(score1PublishInc);
        userAccount.setScore2(score2PublishInc);
        userAccount.setScore3(score3PublishInc);
        userAccount.setScore(score1PublishInc*score1Priorities+score2PublishInc*score2Priorities+score3PublishInc*score3Priorities);
        userAccountExtMapper.decScore(userAccount);
        userAccount=null;

         return c;
    }



    public QuestionDTO setStatuses(QuestionDTO questionDTO,Long viewUser_id){
        questionDTO.setEdited(questionDTO.getGmtCreate().longValue()!=questionDTO.getGmtModified().longValue());
        if((questionDTO.getStatus()&1)==1) questionDTO.setEssence(true);
        if((questionDTO.getStatus()&2)==2) questionDTO.setSticky(true);
        if(viewUser_id.longValue()!=0L){
            if(likeService.queryLike(questionDTO.getId(),1,viewUser_id)>0) questionDTO.setFavorite(true);
            if(userAccountService.isAdminByUserId(viewUser_id)){
                questionDTO.setCanClassify(true);
                questionDTO.setCanDelete(true);
                questionDTO.setCanEssence(true);
                questionDTO.setCanSticky(true);
                questionDTO.setCanEdit(true);
                questionDTO.setCanPromote(true);
            }else if(viewUser_id.longValue()==questionDTO.getCreator().longValue()){
                questionDTO.setCanEdit(true);
                questionDTO.setCanClassify(true);
                questionDTO.setCanDelete(true);
            }
        }

        return questionDTO;
    }

    public Question getQuestionById(Long id){
        return questionMapper.selectByPrimaryKey(id);
    }

    public int updateQuestion(Question question){
        return questionMapper.updateByPrimaryKeySelective(question);
    }
}
