package com.aiot.aiotbackstage.common.exception;

import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import lombok.Data;

/**
 * @ClassName MyException
 * @Description  自定义异常
 * @Author xiaowenhui
 * @Email 610729719@qq.com
 * @Date 2020/01/02  10:41
 * @Version 1.0.0
 **/
@Data
public class MyException extends RuntimeException {

    //错误吗
    private Integer code;

    //错误信息
    private String msg;

    public MyException(ResultStatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMessage();
    }

}