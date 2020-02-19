package com.aiot.aiotbackstage.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 害虫库接口入参
 */
@Data
@ApiModel(value="害虫库新增",description="害虫库新增")
public class AddPestBankParam {

    /**
     * 害虫名称
     */
    @ApiModelProperty(value="害虫名称",name="pestName",example="害虫名称")
    @NotNull(message = "害虫名称不能为空")
    private String pestName;

    /**
     * 害虫类型
     */
    @ApiModelProperty(value="害虫类型",name="pestName",example="害虫类型")
    @NotNull(message = "害虫类型不能为空")
    private String pestType;

    /**
     * 害虫介绍
     */
    @ApiModelProperty(value="害虫介绍",name="pestName",example="害虫介绍")
    @NotNull(message = "害虫介绍不能为空")
    private String pestIntroduce;

    /**
     * 害虫图片
     */
    @ApiModelProperty(value="害虫图片",name="pestName",example="害虫图片")
    @NotNull(message = "害虫图片不能为空")
    private String pestImg;

}