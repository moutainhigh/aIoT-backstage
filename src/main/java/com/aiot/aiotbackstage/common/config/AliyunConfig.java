package com.aiot.aiotbackstage.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description 阿里形色接口入参
 * @Author xiaowenhui
 * @CreateTime 2020/1/7 16:46
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun")
public class AliyunConfig {

    /**
     * 接口主机地址
     */
    private String host;

    /**
     * 接口地址
     */
    private String path;

    /**
     * 调用方式
     */
    private String method;

    /**
     * 阿里云Code
     */
    private String appCode;

    /**
     * 容器类型
     */
    private String contentType;
}
