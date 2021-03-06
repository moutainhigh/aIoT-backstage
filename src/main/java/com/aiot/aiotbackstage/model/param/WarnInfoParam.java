package com.aiot.aiotbackstage.model.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/24 10:40
 */
@Data
@ApiModel(value="预警信息上报",description="预警信息上报")
public class WarnInfoParam {

    private Long id;

    /**
     * 预警时间
     */
    @ApiModelProperty(value="预警时间",name="time",example="预警时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    /**
     * 预警站点
     */
    @ApiModelProperty(value="预警站点",name="time",example="预警站点")
    private Integer siteId;

    /**
     * 坐标
     */
    @ApiModelProperty(value="坐标",name="coordinate",example="坐标")
    private String coordinate;

    /**
     * 预警对象
     */
    @ApiModelProperty(value="预警对象",name="earlyType",example="预警对象")
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


}
