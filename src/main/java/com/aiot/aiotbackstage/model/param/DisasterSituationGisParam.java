package com.aiot.aiotbackstage.model.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/26 15:22
 */
@Data
@ApiModel(value="灾情数据",description="灾情数据")
public class DisasterSituationGisParam {

    @ApiModelProperty(value="多光谱数据的唯一编号",name="earlyName",example="多光谱数据的唯一编号")
    private String ID;

    @ApiModelProperty(value="图片",name="earlyName",example="url")
    private String Image;

    @ApiModelProperty(value="多光谱分析日期",name="date",example="多光谱分析日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    @ApiModelProperty(value="总面积，单位平方米",name="totalArea",example="总面积，单位平方米")
    private BigDecimal totalArea;

    private BigDecimal serious;

    private BigDecimal medium;

    private BigDecimal normal;
}
