package com.aiot.aiotbackstage.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/21 15:06
 */
@Data
@ApiModel(value="虫情动态上报查询条件",description="虫情动态上报查询条件")
public class GetInsectRecReportParam extends PageParam {

    @ApiModelProperty(value="是否审核通过",name="whetherExamine",example="是否审核通过0-未审核，1-通过，2-不通过")
    private Integer whetherExamine;
}
