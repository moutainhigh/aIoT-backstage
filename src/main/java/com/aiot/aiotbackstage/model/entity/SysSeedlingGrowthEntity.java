package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName(value = "sys_seedling_growth")
public class SysSeedlingGrowthEntity {
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

    @TableField(value = "url")
    private String url;

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
     * 优质的
     */
    @TableField(value = "good")
    private BigDecimal good;

    /**
     * 正常的
     */
    @TableField(value = "normal")
    private BigDecimal normal;

    /**
     * 亚健康的
     */
    @TableField(value = "sub_health")
    private BigDecimal subHealth;

    /**
     * 不健康的
     */
    @TableField(value = "unhealthy")
    private BigDecimal unhealthy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;
}