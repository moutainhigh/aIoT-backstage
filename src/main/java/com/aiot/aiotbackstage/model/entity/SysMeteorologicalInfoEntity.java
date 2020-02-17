package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "sys_meteorological_info")
public class SysMeteorologicalInfoEntity implements Serializable {
    /**
     * 气象信息id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 站点id
     */
    @TableField(value = "station_id")
    private Long stationId;

    /**
     * 气象属性名称
     */
    @TableField(value = "attribute_name")
    private String attributeName;

    /**
     * 气象属性值
     */
    @TableField(value = "attribute_var")
    private String attributeVar;

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

    private static final long serialVersionUID = 1L;
}