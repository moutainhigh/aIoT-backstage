package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "sys_preventive_measures")
public class SysPreventiveMeasuresEntity implements Serializable {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 种类名字
     */
    @TableField(value = "name")
    private String name;

    /**
     * 季节
     */
    @TableField(value = "season")
    private String season;

    /**
     * 图片
     */
    @TableField(value = "picture")
    private String picture;

    /**
     * 防治措施
     */
    @TableField(value = "measures_info")
    private String measuresInfo;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}