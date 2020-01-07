package com.aiot.aiotbackstage.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName UserLoginParam
 * @Description 用户登录入参
 * @Author Administrator
 * @Email 610729719@qq.com
 * @Date 2019/11/30  11:11
 * @Version 1.0
 **/

@Data
@ApiModel(value="user登录对象",description="用户登录对象user")
public class UserLoginParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 微信标识CODE
     */
    @ApiModelProperty(value="微信标识CODE",name="code",example="微信标识CODE")
    @NotNull(message = "微信标识CODE不能为空")
    private String code;

}
