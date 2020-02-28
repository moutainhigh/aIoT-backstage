package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
@TableName(value = "sys_menu")
public class SysMenuEntity implements Serializable {
    /**
     * 资源编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 资源名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 父级ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * URL
     */
    @TableField(value = "url")
    private String url;

    /**
     * 权限标识
     */
    @TableField(value = "perms")
    private String perms;

    /**
     * 类型：如button按钮 menu菜单
     */
    @TableField(value = "type")
    private String type;

    /**
     * 排序
     */
    @TableField(value = "priority")
    private Long priority;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}