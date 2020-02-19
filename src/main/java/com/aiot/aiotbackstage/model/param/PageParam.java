package com.aiot.aiotbackstage.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @Description 分页
 * @Author xiaowenhui
 * @CreateTime 2020/2/18 9:52
 */
@Data
public class PageParam {

    @ApiModelProperty(value="当前页",name="userName",example="当前页")
    @Min(value = 1, message = "当前页码不合法")
    private Integer pageNumber=1;

    @ApiModelProperty(value="每页显示的条数",name="userName",example="每页显示的条数")
    @Min(value = 1, message = "每页展示数量不合法")
    private Integer pageSize=10;

    private int offset;
    public int getOffset() {
        return (pageNumber - 1) * pageSize;
    }
}
