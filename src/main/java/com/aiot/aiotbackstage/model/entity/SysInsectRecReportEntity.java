package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "sys_insect_rec_report")
public class SysInsectRecReportEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 是否审核通过0-未审核，1-通过，2-不通过
     */
    @TableField(value = "whether_examine")
    private Integer whetherExamine;

    /**
     * 所属大类
     */
    @TableField(value = "category")
    private String category;

    /**
     * 测报对象
     */
    @TableField(value = "forecast_object")
    private String forecastObject;

    /**
     * 调查时间
     */
    @TableField(value = "survey_time")
    private Date surveyTime;

    /**
     * 信息级别(1-严重 ，2不严重，3一般)
     */
    @TableField(value = "info_level")
    private Integer infoLevel;

    /**
     * 是否新发生（1-发生，2-不发生）
     */
    @TableField(value = "new_or_not")
    private Integer newOrNot;

    /**
     * 上报人
     */
    @TableField(value = "report_name")
    private String reportName;

    /**
     * 信息标题
     */
    @TableField(value = "info_title")
    private String infoTitle;

    /**
     * 备注
     */
    @TableField(value = "remarks")
    private String remarks;

    /**
     * 虫子图片
     */
    @TableField(value = "picture_url")
    private String pictureUrl;

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
}