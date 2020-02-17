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
    //客户端错误400-499
    PARAM_IS_INVALID(401,"参数无效"),
    PARAM_IS_BLANK(402,"参数为空"),
    PARAM_TYPE_BIND_ERROR(403,"参数类型错误"),
    PARAM_NOT_COMPLETE(404,"参数缺失"),
    BAD_REQUEST(405, "参数解析失败"),
    TOKEN_NO_EXIT(406, "token令牌失效"),
    TOKEN_ERR(407, "令牌无效"),
    TOKEN_TIMEOUT(408, "令牌已过期"),
    METHOD_NOT_ALLOWED(409, "不支持当前请求方法"),

    //服务端错误500-599,
    SYSTEM_ERR(500, "服务器运行异常"),
    USER_LIST_NO_EXIT(501, "用户数据不存在"),
    FILE_ERR(502, "文件上传失败！"),
    DB_ERR(503, "文件新增失败！"),
    XINGSE_POST_ERR(504, "调用形色失败！"),
    MEASURES_NO_EXIT(505, "措施信息暂未录入！"),
    GIS_NO_EXIT(506, "GIS站点信息暂未录入！"),
    SIQING_NO_EXIT(507, "四情信息不存在！"),
    USER_NAME_NO_EXIT(508, "用户名不存在！"),
    USER_PASSWORD_NO_EXIT(509, "密码不正确！"),
    NO_ROLE_NO_EXIT(510, "您没有权限访问！"),
    LOGIN_OUT_ERR(511, "退出登录失败！"),
    SYSSENSORREC_NO_EXIT(512, "该站点气象信息暂不存在！"),



    ;
    private Integer code;

    private String message;

}
