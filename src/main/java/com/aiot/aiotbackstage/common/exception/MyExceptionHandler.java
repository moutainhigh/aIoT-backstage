package com.aiot.aiotbackstage.common.exception;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName MyExceptionHandler
 * @Description  全局异常处理
 * @Author xiaowenhui
 * @Email 610729719@qq.com
 * @Date 2020/01/02  10:41
 * @Version 1.0.0
 **/
@Slf4j
@ControllerAdvice
public class MyExceptionHandler {

    /**
     * 参数解析失败异常处理
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.error(request.getRequestURI() + ":参数解析失败",e);
        return new Result(HttpStatus.BAD_REQUEST.value(), ResultStatusCode.BAD_REQUEST.getMessage());
    }

    /**
     * 不支持当前请求方法异常处理
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        log.error(request.getRequestURI() + ":不支持当前请求方法",e);
        return new Result(HttpStatus.METHOD_NOT_ALLOWED.value(), ResultStatusCode.METHOD_NOT_ALLOWED.getMessage());
    }

    /**
     * 项目运行异常处理
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleException(Exception e, HttpServletRequest request) {
        log.error(request.getRequestURI() + ":服务运行异常",e);
        return new Result(HttpStatus.INTERNAL_SERVER_ERROR.value(),ResultStatusCode.SYSTEM_ERR.getMessage());
    }

    /**
     * 自定义异常处理
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public Result handleException(MyException e, HttpServletRequest request) {
        log.error(request.getRequestURI() + ":自定义内部异常",e.getMsg());
        return new Result(e.getCode(),e.getMsg());
    }

    /**
     * 处理访问方法时权限不足问题
     * @param req
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseBody
    public Result defaultErrorHandler(HttpServletRequest req, Exception e)  {
        return new Result(ResultStatusCode.NO_ROLE_NO_EXIT);
    }

    /**
     * 处理访问方法时权限不足问题
     * @param req
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = ArithmeticException.class)
    @ResponseBody
    public Result arithmeticException(HttpServletRequest req, Exception e)  {
        return new Result(ResultStatusCode.TOKEN_NO_EXIT);
    }

}
