package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.model.entity.SysCameraEntity;
import com.aiot.aiotbackstage.service.ISysCameraService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Avernus
 */
@RestController
@RequestMapping(value = "/camera")
public class CameraController {

    @Resource
    private ISysCameraService cameraService;

    @ApiOperation(value = "查询摄像头列表", notes = "查询摄像头列表")
    @RequestMapping(value = "/cameras", method = RequestMethod.GET)
    private Result cameras() {
        List<SysCameraEntity> entities = cameraService.list();
        return Result.success(entities);
    }

    @ApiOperation(value = "获取摄像头直播地址", notes = "获取摄像头直播地址")
    @RequestMapping(value = "/real", method = RequestMethod.GET)
    private Result real(@ApiParam(value="摄像头id",name="cameraId") Integer cameraId) {
        String url = cameraService.getRTSP_URL(cameraId);
        return Result.success(url);
    }

    @ApiOperation(value = "云台控制", notes = "云台控制")
    @RequestMapping(value = "/ptz", method = RequestMethod.POST)
    private Result ptz(@ApiParam(value="摄像头id", name="cameraId") Integer cameraId,
                       @ApiParam(value="转向方向", name="direction 1到8的数字,UP = 1;DOWN = 2;LEFT = 3;RIGHT = 4;LEFTUP = 5;LEFTDOWN = 6;RIGHTUP = 7;RIGHTDOWN = 8;") Integer direction) {
        boolean b = cameraService.PTZ_Control(cameraId, direction);
        return Result.success(b);
    }

}
