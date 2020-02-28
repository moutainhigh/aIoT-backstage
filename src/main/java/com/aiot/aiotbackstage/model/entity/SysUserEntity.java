package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@TableName(value = "sys_user")
public class SysUserEntity {
    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户的唯一标识
     */
    @TableField(value = "wx_openid")
    private String wxOpenid;

    /**
     * 会话key
     */
    @TableField(value = "session_key")
    private String sessionKey;

    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 登陆类型
     */
    @TableField(value = "login_type")
    private Integer loginType;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 角色名称
     */
    @TableField(exist = false)
    private String roleName;

    /**
     * 角色id
     */
    @TableField(exist = false)
    private Long roleId;

}