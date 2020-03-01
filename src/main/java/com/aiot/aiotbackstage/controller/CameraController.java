package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.service.ISysCameraService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Avernus
 */
@RestController()
@RequestMapping(value = "/camera")
public class CameraController {

    @Resource
    private ISysCameraService cameraService;

    @RequestMapping(value = "/real", method = RequestMethod.GET)
    private Result real(Integer cameraId) {
        String url = cameraService.getRTSP_URL(cameraId);
        return Result.success(url);
    }

    @RequestMapping(value = "/ptz", method = RequestMethod.POST)
    private Result ptz(Integer cameraId, Integer direction) {
        boolean b = cameraService.PTZ_Control(cameraId, direction);
        return Result.success(b);
    }

}
