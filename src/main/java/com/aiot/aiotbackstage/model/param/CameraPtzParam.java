package com.aiot.aiotbackstage.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="云台控制",description="云台控制")
public class CameraPtzParam {

    @ApiModelProperty(value = "摄像头id", name = "cameraId")
    private Integer cameraId;

    @ApiModelProperty(value = "转向方向", name = "direction 1到8的数字,UP = 1;DOWN = 2;LEFT = 3;RIGHT = 4;LEFTUP = 5;LEFTDOWN = 6;RIGHTUP = 7;RIGHTDOWN = 8;")
    private Integer direction;
}
