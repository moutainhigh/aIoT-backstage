package com.aiot.aiotbackstage.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="获取录像",description="获取录像")
public class CameraRecRaram {

    @ApiModelProperty(value = "摄像头id", name = "cameraId")
    private Integer cameraId;

    @ApiModelProperty(value = "起始时间", name = "beginTime")
    private long beginTime;

    @ApiModelProperty(value = "结束时间", name = "endTime")
    private long endTime;
}
