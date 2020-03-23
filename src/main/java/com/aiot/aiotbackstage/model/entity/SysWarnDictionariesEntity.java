package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

@Data
@TableName(value = "sys_warn_dictionaries")
public class SysWarnDictionariesEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 规则对象
     */
    @TableField(value = "type")
    private String type;

    @TableField(value = "name")
    private String name;

    @TableField(value = "type_name")
    private String typeName;

    @TableField(value = "unit")
    private String unit;

    /**
     * 属性值
     */
    @TableField(value = "value")
    private String value;

    private static final long serialVersionUID = 1L;
}