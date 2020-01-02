package com.aiot.aiotbackstage.common.constant;

import lombok.Data;

/**
 * @ClassName Result
 * @Description  RestFul接口返回的数据模型
 * @Author xiaowenhui
 * @Email 610729719@qq.com
 * @Date 2020/01/02  10:41
 * @Version 1.0.0
 **/
@Data
public class Result {
    /**
     * 返回的代码，200表示成功，其他表示失败
     */
    private int code;
    /**
     * 成功或失败时返回的错误信息
     */
    private String msg;
    /**
     * 成功时返回的数据信息
     */
    private Object data;

    public Result(){
    }

    public Result(int code, String msg, Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result success(Object object) {
        Result result = new Result();
        result.setCode(ResultStatusCode.SUCCESS.getCode());
        result.setMsg(ResultStatusCode.SUCCESS.getMessage());
        result.setData(object);
        return result;
    }

    public static Result success() {
        return success(null);
    }

    public static Result error(ResultStatusCode resultStatusCode) {
        Result result = new Result();
        result.setCode(resultStatusCode.getCode());
        result.setMsg(resultStatusCode.getMessage());
        return result;
    }

    /**
     * 推荐使用此种方法返回
     * @param resultStatusCode 枚举信息
     * @param data 返回数据
     */
    public Result(ResultStatusCode resultStatusCode, Object data){
        this(resultStatusCode.getCode(), resultStatusCode.getMessage(), data);
    }

    public Result(int code, String msg){
        this(code, msg, null);
    }

    public Result(ResultStatusCode resultStatusCode){
        this(resultStatusCode, null);
    }


}