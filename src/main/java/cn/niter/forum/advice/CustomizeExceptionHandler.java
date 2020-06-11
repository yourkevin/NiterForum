package cn.niter.forum.advice;

import cn.niter.forum.dto.ResultDTO;
import cn.niter.forum.exception.CustomizeErrorCode;
import cn.niter.forum.exception.CustomizeException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultDTO MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        // 可以使用 warn 日志级别来记录用户输入参数错误的情况，避免用户投诉时，无所适从。如非必要，请不要在此场景打出 error 级别，避免频繁报警。
        log.warn(e.getMessage(), e);
        // 然后提取错误提示信息进行返回
        return ResultDTO.errorOf(CustomizeErrorCode.VALIDATE_ERROR, objectError.getDefaultMessage());
    }

    @ResponseBody
    @ExceptionHandler({BindException.class})
    public ResultDTO MethodArgumentNotValidExceptionHandler(BindException e) {
        // 从异常对象中拿到ObjectError对象
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        // 可以使用 warn 日志级别来记录用户输入参数错误的情况，避免用户投诉时，无所适从。如非必要，请不要在此场景打出 error 级别，避免频繁报警。
        log.warn(e.getMessage(), e);
        // 然后提取错误提示信息进行返回
        return ResultDTO.errorOf(CustomizeErrorCode.VALIDATE_ERROR, objectError.getDefaultMessage());
    }

  /*  @ResponseBody
    @ExceptionHandler(CustomizeException.class)
    public ResultDTO APIExceptionHandler(CustomizeException e) {
        log.error(e.getMessage(), e);
        return ResultDTO.errorOf(e.getCode(), e.getMessage());
    }*/


    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model, HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)||request.getServletPath().contains("api")) {//访问接口异常时弹出异常信息，非跳转
            ResultDTO resultDTO;
            // 返回 JSON
            if (e instanceof CustomizeException) {//已知自定义异常
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            } else {//未知异常
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                //response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ioe) {
            }
            return null;
        } else {
            // 访问页面错误时页面跳转
            if (e instanceof CustomizeException) {
                model.addAttribute("message", e.getMessage());
                model.addAttribute("errorCode","错误:"+((CustomizeException) e).getCode());
            } else {
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }
}
