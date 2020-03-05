package com.aiot.aiotbackstage.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Avernus
 */
@Data
@Component
@ConfigurationProperties(prefix = "gis")
public class GisConfig {

    private String url;

}
