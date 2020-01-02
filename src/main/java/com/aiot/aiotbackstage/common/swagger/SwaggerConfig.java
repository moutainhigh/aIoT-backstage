package com.aiot.aiotbackstage.common.swagger;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName SwaggerConfig
 * @Description  API接口文档配置
 * @Author xiaowenhui
 * @Email 610729719@qq.com
 * @Date 2020/01/02  10:41
 * @Version 1.0.0
 **/
@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerConfig{

    private SwaggerInfoConfig swaggerInfoConfig;

    @Autowired
    public void setSwaggerInfo(SwaggerInfoConfig swaggerInfoConfig) {
        this.swaggerInfoConfig = swaggerInfoConfig;
    }

    @Bean
    public Docket createRestApi() {

        log.info("========================================== 当前环境是否开启Swagger：" + swaggerInfoConfig.getEnable());
        //添加head参数配置start
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("token").description("用户登陆令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        //添加head参数配置end
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(swaggerInfoConfig.getEnable())
                .apiInfo(apiInfo())
                .select()
                .apis(Predicates.or(apisFilter()))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerInfoConfig.getTitle())
                .description(swaggerInfoConfig.getDescription())
                .termsOfServiceUrl(swaggerInfoConfig.getTermsOfServiceUrl())
                .version(swaggerInfoConfig.getVersion())
                .contact(new Contact(swaggerInfoConfig.getContactName(),
                        swaggerInfoConfig.getContactUrl(),
                        swaggerInfoConfig.getContactEmail()))
                .build();
    }

    /**
     * 过滤掉不需要显示的包
     * @return
     */
    private List<Predicate<RequestHandler>> apisFilter() {
        List<Predicate<RequestHandler>> apis = new ArrayList<>();
        String basePackageStr = swaggerInfoConfig.getBasePackage();
        //包过滤
        if (StringUtils.isNotBlank(basePackageStr)) {
            //支持多个包
            String[] basePackages = basePackageStr.split(";");
            if (null != basePackages && basePackages.length > 0) {
                Predicate<RequestHandler> predicate = input -> {
                    // 按basePackage过滤
                    Class<?> declaringClass = input.declaringClass();
                    String packageName = declaringClass.getPackage().getName();
                    return Arrays.asList(basePackages).contains(packageName);
                };
                apis.add(predicate);
            }
        }
        return apis;
    }

}
