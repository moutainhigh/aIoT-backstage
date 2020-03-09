package com.aiot.aiotbackstage.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description OBS华为存储参数
 * @Author xiaowenhui
 * @CreateTime 2020/1/10 16:52
 */
@Data
@Component
@ConfigurationProperties(prefix = "obs")
public class ObsConfig {

    /**
     * 访问密钥ID
     */
    private String ak;

    /**
     * 秘密访问密钥
     */
    private String sk;

    /**
     * 域名
     */
    private String endPoint;

    /**
     * 桶名称
     */
    private String bucketName;

    private String url;
}
