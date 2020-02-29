package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/28 15:43
 */
@Data
@TableName(value = "sys_role_menu")
public class SysRoleMenuEntity implements Serializable  {
    @TableField(value = "role_id")
    private Long roleId;

    @TableField(value = "menu_id")
    private Long menuId;

    private static final long serialVersionUID = 1L;
}
