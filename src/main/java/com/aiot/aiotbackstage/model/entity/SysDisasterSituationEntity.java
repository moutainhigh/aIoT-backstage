package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "sys_disaster_situation")
public class SysDisasterSituationEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 站点id
     */
    @TableField(value = "site_id")
    private String siteId;

    /**
     * 多光谱数据的唯一编号
     */
    @TableField(value = "guid")
    private String guid;

    /**
     * 多光谱分析日期
     */
    @TableField(value = "date")
    private Date date;

    /**
     * 总面积，单位平方米
     */
    @TableField(value = "total_area")
    private BigDecimal totalArea;

    /**
     * 严重的
     */
    @TableField(value = "serious")
    private BigDecimal serious;

    /**
     * 中等的
     */
    @TableField(value = "medium")
    private BigDecimal medium;

    /**
     * 正常的
     */
    @TableField(value = "normal")
    private BigDecimal normal;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
}