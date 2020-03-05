package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.config.DahuaConfig;
import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.common.sdk.DahuaSDK;
import com.aiot.aiotbackstage.mapper.SysCameraMapper;
import com.aiot.aiotbackstage.model.entity.SysCameraEntity;
import com.aiot.aiotbackstage.service.ISysCameraService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Avernus
 */
@Service
public class SysCameraServiceImpl extends ServiceImpl<SysCameraMapper, SysCameraEntity> implements ISysCameraService {

    @Resource
    private DahuaConfig dahuaConfig;
    @Resource
    private DahuaSDK sdk;

    /**
     * HLS拉流地址格式说明：
     *       http://平台IP:端口/live/cameraid/设备编号%24通道号/substream/码流类型.m3u8
     *       端口：默认7086端口
     *       设备编号：平台上设备编号，例如1000000
     *       通道号：设备下通道，从0（通道一）开始，取通道ID最后一个$后的值。如通道ID为1000000$1$0$0的通道号为0，设备编号为1000000
     *       码流类型：1代表主码流，2代表辅码流
     * @param cameraId
     * @return
     */
    @Override
    public String getRTSP_URL(Integer cameraId) {
        SysCameraEntity cameraEntity = baseMapper.selectById(cameraId);
        String s = (String) sdk.cameraChannel().get(cameraEntity.getName());
        if (s == null) {
            sdk.init();
            throw new MyException(ResultStatusCode.CAMERA_OFFLINE);
        }
        StringBuffer sb = new StringBuffer();
        sb.append("http://")
                .append(dahuaConfig.getIp())
                .append(":")
                .append("7086")
                .append("/live/cameraid/")
                .append(s.split("\\$")[0])
                .append("%24")
                .append(s.split("\\$")[3])
                .append("/substream/1.m3u8");
        return sb.toString();
    }

    @Override
    public boolean PTZ_Control(Integer cameraId, Integer direction) {
        SysCameraEntity cameraEntity = baseMapper.selectById(cameraId);
        String s = (String) sdk.cameraChannel().get(cameraEntity.getName());
        if (s == null) {
            sdk.init();
            throw new MyException(ResultStatusCode.CAMERA_OFFLINE);
        }
        sdk.ptzDirectCtrl(s, direction);
        return true;
    }


}
