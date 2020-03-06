package com.aiot.aiotbackstage.model.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/3/6 16:23
 */
@ApiModel(value="根据时间范围查询害虫数据",description="根据时间范围查询害虫数据")
@Data
public class InsectRecByDateParam {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

}
