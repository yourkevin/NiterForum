package cn.niter.forum.controller;


import cn.niter.forum.cache.HotTagCache;
import cn.niter.forum.cache.LoginUserCache;
import cn.niter.forum.dto.CommentDTO;
import cn.niter.forum.dto.PaginationDTO;
import cn.niter.forum.dto.TalkQueryDTO;
import cn.niter.forum.dto.UserDTO;
import cn.niter.forum.enums.CommentTypeEnum;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import cn.niter.forum.model.User;
import cn.niter.forum.model.UserAccount;
import cn.niter.forum.service.CommentService;
import cn.niter.forum.service.QuestionService;
import cn.niter.forum.service.TalkService;
import cn.niter.forum.vo.TalkVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wadao
 * @version 2.0
 * @date 2020/5/1 17:33
 * @site niter.cn
 */

@Controller
public class TalkController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotTagCache hotTagCache;

    @Autowired
    private LoginUserCache loginUserCache;

    @Value("${vaptcha.vid}")
    private String vaptcha_vid;

    @Autowired
    private CommentService commentService;

    @Autowired
    private TalkService talkService;

    @GetMapping("/talk")
    public String tIndex(HttpServletRequest request, Model model,
                         @RequestParam(name = "page",defaultValue = "1")Integer page,
                         @RequestParam(name = "size",defaultValue = "10")Integer size,
                         @RequestParam(name = "column",defaultValue = "0") Integer column2,
                         @RequestParam(name = "search", defaultValue = "") String search,
                         @RequestParam(name = "tag", defaultValue = "") String tag,
                         @RequestParam(name = "sort", defaultValue = "new") String sort) {
        //List<QuestionDTO> topQuestions = questionService.listTopwithColumn(search, tag, sort,column2);
       // UserAccount userAccount = (UserAccount)request.getSession().getAttribute("userAccount");
        //PaginationDTO pagination = questionService.listwithColumn(search, tag, sort, page,size,column2,userAccount);
        List<String> tags = hotTagCache.getHots();
        List<User> loginUsers = loginUserCache.getLoginUsers();
        //System.out.println("users"+loginUsers);
        model.addAttribute("loginUsers", loginUsers);
        //model.addAttribute("pagination",pagination);
        model.addAttribute("search", search);
        model.addAttribute("tag", tag);
        model.addAttribute("tags", tags);
        model.addAttribute("sort", sort);
        model.addAttribute("column", column2);
        model.addAttribute("page", page);
        //model.addAttribute("topQuestions", topQuestions);
        model.addAttribute("navtype", "talknav");
        model.addAttribute("vaptcha_vid", vaptcha_vid);
        //return "index";
        return "t/index";
    }


    @GetMapping(value = {"/t/{id}"})
    public String comment(@PathVariable(name = "id") Long id, HttpServletRequest request, Model model){

        UserDTO viewUser = (UserDTO)request.getAttribute("loginUser");
        /*if(viewUser==null){
            model.addAttribute("rsTitle", "您无权访问！");
            model.addAttribute("rsMessage", "高级回复页游客不可见，快去登录吧");
            return "result";
        }*/
        TalkVO talkVO;
        TalkQueryDTO talkQueryDTO = new TalkQueryDTO();
        talkQueryDTO.setId(id);
        talkQueryDTO.convert();
        PaginationDTO paginationDTO = talkService.list(talkQueryDTO,viewUser);
        if(paginationDTO.getTotalCount()!=1)
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        else{
            talkVO = (TalkVO) paginationDTO.getData().get(0);
        }
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTypeEnum.TALK);

        //commentQueryDTO.convert();
       /* List<CommentDTO> commentDTOs = commentService.list(commentQueryDTO).getData();
        if(commentDTOs.size()==1){
            commentDTO = commentDTOs.get(0);
        }else {
            model.addAttribute("rsTitle", "该页面无法访问！");
            model.addAttribute("rsMessage", "请确认路径是否正确");
            return "result";
        }*/
       // List<CommentDTO> subComments = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
       // QuestionDTO questionDTO = questionService.getById(commentDTO.getParentId(), viewUser.getId());
        model.addAttribute("talk", talkVO);
        model.addAttribute("comments", comments);
        //model.addAttribute("subComments", subComments);
        model.addAttribute("navtype", "talknav");
        model.addAttribute("vaptcha_vid", vaptcha_vid);
        return "t/detail";
    }



    @Deprecated
    @GetMapping("/t/list")
    @ResponseBody
    public Map<String,Object> tList(HttpServletRequest request, Model model,
                                    @RequestParam(name = "page",defaultValue = "1")Integer page,
                                    @RequestParam(name = "size",defaultValue = "10")Integer size,
                                    @RequestParam(name = "column", required = false) Integer column2,
                                    @RequestParam(name = "search", required = false) String search,
                                    @RequestParam(name = "tag", required = false) String tag,
                                    @RequestParam(name = "sort", required = false) String sort) {
        //List<QuestionDTO> topQuestions = questionService.listTopwithColumn(search, tag, sort,column2);
        Map<String,Object> map  = new HashMap<>();
        UserDTO loginuser = (UserDTO) request.getAttribute("loginUser");
        UserAccount userAccount =null;
        if(loginuser!=null){
            userAccount = new UserAccount();
            BeanUtils.copyProperties(loginuser,userAccount);
            userAccount.setUserId(loginuser.getId());
        }        PaginationDTO pagination = questionService.listwithColumn(search, tag, sort, page,size,column2,userAccount);
        map.put("questions",pagination.getData());
        map.put("totalPage",pagination.getTotalPage());
        map.put("search",search);
        map.put("tag",tag);
        map.put("sort",sort);
        map.put("column",column2);

        return map;
    }



}
