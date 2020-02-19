package com.aiot.aiotbackstage.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 害虫库接口入参
 */
@Data
@ApiModel(value="害虫库修改",description="害虫库修改")
public class ModifyPestBankParam extends AddPestBankParam{

    @ApiModelProperty(value="害虫标识",name="pestName",example="害虫标识")
    @NotNull(message = "害虫标识不能为空")
    private Long pestBankId;

}