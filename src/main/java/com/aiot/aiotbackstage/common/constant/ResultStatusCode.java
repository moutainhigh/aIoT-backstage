package com.aiot.aiotbackstage.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName ResultStatusCode
 * @Description  RestFul接口返回的数据
 * @Author xiaowenhui
 * @Email 610729719@qq.com
 * @Date 2020/01/02  10:41
 * @Version 1.0.0
 **/
@AllArgsConstructor
@Getter
public enum ResultStatusCode {
    //成功
    SUCCESS(200,"成功"),
    //参数错误100-199
    PARAM_IS_INVALID(101,"参数无效"),
    PARAM_IS_BLANK(102,"参数为空"),
    PARAM_TYPE_BIND_ERROR(103,"参数类型错误"),
    PARAM_NOT_COMPLETE(104,"参数缺失"),
    BAD_REQUEST(105, "参数解析失败"),
    TOKEN_NO_EXIT(106, "token令牌不存在"),
    TOKEN_ERR(107, "令牌无效"),
    TOKEN_TIMEOUT(108, "令牌已过期"),

    METHOD_NOT_ALLOWED(405, "不支持当前请求方法"),
    SYSTEM_ERR(500, "服务器运行异常"),

    //用户错误200-299,
    USER_LIST_NO_EXIT(200, "用户数据不存在"),


    //部门错误300-399


    //权限和权限模块的错误450-499


    //权限和权限模块的错误550-599


    //商户管理的错误600-699


    //店铺管理的错误700-799


    ;
    private Integer code;

    private String message;

}
