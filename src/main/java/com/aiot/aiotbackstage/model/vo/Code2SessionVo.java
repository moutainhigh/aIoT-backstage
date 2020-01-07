package com.aiot.aiotbackstage.model.vo;

import lombok.Data;

/**
 * @Description 微信小程序返回的出参
 * @Author xiaowenhui
 * @CreateTime 2020/1/7 14:04
 */
@Data
public class Code2SessionVo {

    /**
     * 用户唯一标识
     */
    private String openid;

    /**
     * 会话密钥
     */
    private String session_key;

    /**
     * 用户在开放平台的唯一标识符
     */
    private String unionid;

    /**
     * 错误码
     * -1：系统繁忙此时请开发者稍候再试
     * 0：请求成功
     * 40029：code无效
     * 45011：频率限制，每个用户每分钟100次
     */
    private String errcode = "0";

    /**
     * 错误信息
     */
    private String errmsg;

    private int expires_in;
}
