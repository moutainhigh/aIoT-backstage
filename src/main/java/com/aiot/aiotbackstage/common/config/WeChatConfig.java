package com.aiot.aiotbackstage.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description 微信小程序入参
 * @Author xiaowenhui
 * @CreateTime 2020/1/7 14:25
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WeChatConfig {

    /**
     * 小程序 appId
     */
    private String app_id;

    /**
     * 小程序 appSecret
     */
    private String app_secret;

    /**
     * 授权类型
     */
    private String grant_type;

    /**
     * URL
     */
    private String session_host;
}
