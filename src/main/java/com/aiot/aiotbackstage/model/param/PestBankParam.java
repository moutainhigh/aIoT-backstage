package com.aiot.aiotbackstage.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 害虫库接口入参
 */
@Data
@ApiModel(value="害虫库查询",description="害虫库查询")
public class PestBankParam extends PageParam{

    /**
     * 害虫名称
     */
    @ApiModelProperty(value="害虫名称",name="pestName",example="害虫名称")
    private String pestName;

}