package com.aiot.aiotbackstage.model.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel(value="虫情动态上报",description="虫情动态上报")
public class InsectRecReportParam {

    private Long id;


    /**
     * 所属大类
     */
    @ApiModelProperty(value="所属大类",name="category",example="所属大类")
    @NotNull(message = "所属大类不能为空")
    private String category;

    /**
     * 测报对象
     */
    @ApiModelProperty(value="测报对象",name="forecastObject",example="测报对象")
    @NotNull(message = "测报对象不能为空")
    private String forecastObject;

    /**
     * 调查时间
     */
    @ApiModelProperty(value="调查时间",name="surveyTime",example="调查时间")
    @NotNull(message = "调查时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date surveyTime;

    /**
     * 信息级别(1-严重 ，2不严重，3一般)
     */
    @ApiModelProperty(value="信息级别(1-严重 ，2不严重，3一般)",name="infoLevel",example="信息级别(1-严重 ，2不严重，3一般)")
    @NotNull(message = "信息级别不能为空")
    private Integer infoLevel;

    /**
     * 是否新发生（1-发生，2-不发生）
     */
    @ApiModelProperty(value="是否新发生（1-发生，2-不发生）",name="newOrNot",example="是否新发生（1-发生，2-不发生）")
    @NotNull(message = "是否新发生不能为空")
    private Integer newOrNot;

    /**
     * 上报人
     */
    @ApiModelProperty(value="上报人",name="reportName",example="上报人")
    @NotNull(message = "上报人不能为空")
    private String reportName;

    /**
     * 信息标题
     */
    @ApiModelProperty(value="信息标题",name="infoTitle",example="信息标题")
    @NotNull(message = "信息标题不能为空")
    private String infoTitle;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注",name="remarks",example="备注")
    private String remarks;

    /**
     * 虫子图片
     */
    @ApiModelProperty(value="虫子图片",name="pictureUrl",example="虫子图片")
    @NotNull(message = "虫子图片不能为空")
    private String pictureUrl;

}