package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "sys_insect_info")
public class SysInsectInfoEntity {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @TableField(value = "name")
    private String name;
}