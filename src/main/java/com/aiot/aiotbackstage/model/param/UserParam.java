package com.aiot.aiotbackstage.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class UserParam {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不可以为空")
    @Length(min = 1, max = 20, message = "用户名长度需要在20个字以内")
    @ApiModelProperty(value="用户名",name="username",example="demo")
    private String userName;

    /**
     * 密码
     */
    private String password;


    /**
     * 角色id
     */
    private Long  roleId;

}