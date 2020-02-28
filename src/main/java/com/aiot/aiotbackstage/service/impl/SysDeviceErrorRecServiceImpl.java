package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.SysDeviceErrorRecMapper;
import com.aiot.aiotbackstage.model.entity.SysDeviceErrorRecEntity;
import com.aiot.aiotbackstage.service.ISysDeviceErrorRecService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Avernus
 */
@Service
public class SysDeviceErrorRecServiceImpl extends ServiceImpl<SysDeviceErrorRecMapper, SysDeviceErrorRecEntity> implements ISysDeviceErrorRecService {


    @Override
    public void newRec(Integer siteId, String deviceType, String subType) {
        SysDeviceErrorRecEntity rec = baseMapper.selectOne(new QueryWrapper<SysDeviceErrorRecEntity>()
                .eq("site_id", siteId)
                .eq("device_type", deviceType)
                .eq("sub_type", subType)
                .isNull("end_time"));
        if (rec != null) {
            return;
        }
        SysDeviceErrorRecEntity entity = new SysDeviceErrorRecEntity()
                .setSiteId(siteId)
                .setDeviceType(deviceType)
                .setSubType(subType)
                .setStartTime(new Date());
        baseMapper.insert(entity);
    }

    @Override
    public void refreshRec(Integer siteId, String deviceType, String subType) {
        SysDeviceErrorRecEntity entity = new SysDeviceErrorRecEntity()
                .setEndTime(new Date());
        baseMapper.update(entity, new QueryWrapper<SysDeviceErrorRecEntity>()
                .eq("site_id", siteId)
                .eq("device_type", deviceType)
                .eq("sub_type", subType)
                .isNull("end_time"));
    }
}
