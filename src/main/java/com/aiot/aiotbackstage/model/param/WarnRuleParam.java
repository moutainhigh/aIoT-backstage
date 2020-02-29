package com.aiot.aiotbackstage.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/24 10:40
 */
@Data
@ApiModel(value="预警库新增",description="预警库新增")
public class WarnRuleParam {

    /**
     * 预警id
     */
    @ApiModelProperty(value="预警id(修改时必传)",name="earlyType",example="预警id")
    private Long id;

    /**
     * 预警对象
     */
    @ApiModelProperty(value="预警对象（1-苗情\n" +
            "2-灾情\n" +
            "3-气象\n" +
            "4-土壤\n" +
            "5-虫情\n" +
            "）",name="earlyType",example="预警对象")
    private String earlyType;

    /**
     * 预警名称
     */
    @ApiModelProperty(value="预警名称",name="earlyName",example="预警名称")
    private String earlyName;

    /**
     * 土壤深度
     */
    @ApiModelProperty(value="土壤深度",name="earlyDepth",example="土壤深度")
    private String earlyDepth;

    /**
     * 预警程度
     */
    @ApiModelProperty(value="预警程度",name="earlyDegree",example="预警程度")
    private String earlyDegree;

    /**
     * 预警内容
     */
    @ApiModelProperty(value="预警内容",name="earlyContent",example="预警内容")
    private String earlyContent;

    /**
     * 预警阈值
     */
    @ApiModelProperty(value="预警阈值",name="earlyThreshold",example="预警阈值")
    private String earlyThreshold;

}
