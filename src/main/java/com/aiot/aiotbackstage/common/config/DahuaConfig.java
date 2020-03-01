package com.aiot.aiotbackstage.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Avernus
 */
@Data
@Component
@ConfigurationProperties(prefix = "dahua8900")
public class DahuaConfig {

    private String ip;

    private Integer port;

    private String username;

    private String password;

}
