package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.common.enums.DeviceType;
import com.aiot.aiotbackstage.mapper.SysDeviceErrorRecMapper;
import com.aiot.aiotbackstage.model.entity.SysCameraEntity;
import com.aiot.aiotbackstage.model.entity.SysDeviceErrorRecEntity;
import com.aiot.aiotbackstage.model.param.CameraPtzParam;
import com.aiot.aiotbackstage.model.param.CameraRecRaram;
import com.aiot.aiotbackstage.service.ISysCameraService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

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
    @Resource
    private SysDeviceErrorRecMapper errorRecMapper;

    @ApiOperation(value = "查询摄像头列表", notes = "查询摄像头列表")
    @RequestMapping(value = "/cameras", method = RequestMethod.GET)
    private Result cameras() {
        List<SysCameraEntity> entities = cameraService.list();
        List<SysDeviceErrorRecEntity> errorRecEntities =
                errorRecMapper.selectList(Wrappers.<SysDeviceErrorRecEntity>lambdaQuery()
                        .isNull(SysDeviceErrorRecEntity::getEndTime));
        //能查到errorRec中结束时间为null的既status为故障中
        for (SysCameraEntity entity : entities) {
            //默认为在线状态
            entity.setStatus(true);
            for (SysDeviceErrorRecEntity rec : errorRecEntities) {
                if (entity.getSiteId().equals(rec.getSiteId())
                        && entity.getType().equals(rec.getSubType())
                        && rec.getDeviceType().equals(DeviceType.CAMERA.name())) {
                    entity.setStatus(false);
                    break;
                }
            }
        }
        return Result.success(entities);
    }

    @ApiOperation(value = "获取摄像头直播地址", notes = "获取摄像头直播地址")
    @RequestMapping(value = "/real", method = RequestMethod.GET)
    private Result real(@ApiParam(value = "摄像头id", name = "cameraId") Integer cameraId) {
        String url = cameraService.getRTSP_URL(cameraId);
        return Result.success(url);
    }

    @ApiOperation(value = "云台控制", notes = "云台控制")
    @RequestMapping(value = "/ptz", method = RequestMethod.POST)
    private Result ptz(@RequestBody CameraPtzParam param) {
        boolean b = cameraService.PTZ_Control(param.getCameraId(), param.getDirection());
        return Result.success(b);
    }

    @ApiOperation(value = "获取录像", notes = "获取录像")
    @RequestMapping(value = "/rec", method = RequestMethod.POST)
    private Result ptz(@RequestBody CameraRecRaram param) {
        boolean b = cameraService.getVideoRec(param.getCameraId(), param.getBeginTime(), param.getEndTime());
        return Result.success(b);
    }

}
