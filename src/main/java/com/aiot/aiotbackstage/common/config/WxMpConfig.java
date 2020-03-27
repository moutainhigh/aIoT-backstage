package com.aiot.aiotbackstage.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/3/16 10:53
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx")
public class WxMpConfig {

    /**
     * 公众号appId
     */
    private String appId;

    /**
     * 公众号appSecret
     */
    private String secret;

    /**
     * 微信公众号的模板
     */
    private String device_error_template;

    private String warn_template;

}
