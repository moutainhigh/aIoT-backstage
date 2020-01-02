package com.aiot.aiotbackstage.common.swagger;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


/**
 * @ClassName SwaggerInfoConfig
 * @Description  API配置
 * @Author xiaowenhui
 * @Email 610729719@qq.com
 * @Date 2020/01/02  10:41
 * @Version 1.0.0
 **/
@Data
@Component
@ConfigurationProperties(prefix = "swagger")
@PropertySource(value = "classpath:/config/swagger.yml",encoding = "utf-8")
public class SwaggerInfoConfig {


    @Value("${basePackage}")
    private String basePackage;

    @Value("${title}")
    private String title;

    @Value("${description}")
    private String description;

    @Value("${termsOfServiceUrl}")
    private String termsOfServiceUrl;

    @Value("${version}")
    private String version ;

    @Value("${enable}")
    private Boolean enable;

    @Value("${contactName}")
    private String contactName;

    @Value("${contactEmail}")
    private String contactEmail;

    @Value("${contactUrl}")
    private String contactUrl;

}
