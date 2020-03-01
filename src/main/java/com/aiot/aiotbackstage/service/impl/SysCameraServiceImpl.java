package com.aiot.aiotbackstage.service.impl;

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
    private DahuaSDK sdk;

    @Override
    public String getRTSP_URL(Integer cameraId) {
        if (DahuaSDK.m_nDLLHandle == -1) {
            sdk.init();
        }
        SysCameraEntity cameraEntity = baseMapper.selectById(cameraId);
        String s = DahuaSDK.channelMap.get(cameraEntity.getName());
        if (s == null) {
            return "";
        }
        return sdk.real(s);
    }

    @Override
    public boolean PTZ_Control(Integer cameraId, Integer direction) {
        if (DahuaSDK.m_nDLLHandle == -1) {
            sdk.init();
        }
        SysCameraEntity cameraEntity = baseMapper.selectById(cameraId);
        String s = DahuaSDK.channelMap.get(cameraEntity.getName());
        if (s == null) {
            return false;
        }
        sdk.ptzDirectCtrl(s, direction);
        return true;
    }
}
