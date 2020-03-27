package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysCameraEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Avernus
 */
public interface ISysCameraService extends IService<SysCameraEntity> {

    /**
     * 获取rtsp视频流地址
     * @return
     */
    String getRTSP_URL(Integer cameraId);

    /**
     * 云台控制
     * @return
     */
    boolean PTZ_Control(Integer cameraId, Integer direction);

    /**
     * 获取录像
     */
    boolean getVideoRec(Integer cameraId, long beginTime, long endTime);
}
