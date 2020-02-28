package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "sys_pest_adoption")
public class SysPestAdoptionEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 分布情况
     */
    @TableField(value = "distribution_situation")
    private String distributionSituation;

    /**
     * 分布面积
     */
    @TableField(value = "distribution_area")
    private String distributionArea;

    /**
     * 所在疫区样地
     */
    @TableField(value = "distribution_address")
    private String distributionAddress;

    /**
     * 面积
     */
    @TableField(value = "area")
    private String area;

    /**
     * 株数
     */
    @TableField(value = "count")
    private Integer count;

    /**
     * 虫名
     */
    @TableField(value = "pest_bank_name")
    private String pestBankName;

    /**
     * 发病原因
     */
    @TableField(value = "cause_disease")
    private String causeDisease;

    /**
     * 来源
     */
    @TableField(value = "source")
    private String source;

    @TableField(value = "create_time")
    private Date createTime;
}