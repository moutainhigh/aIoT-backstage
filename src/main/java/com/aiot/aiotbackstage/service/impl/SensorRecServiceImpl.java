package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.enums.RtuAddrCode;
import com.aiot.aiotbackstage.common.enums.SensorType;
import com.aiot.aiotbackstage.common.enums.WindDirection;
import com.aiot.aiotbackstage.mapper.SysSensorRecMapper;
import com.aiot.aiotbackstage.mapper.SysSiteMapper;
import com.aiot.aiotbackstage.model.dto.RtuData;
import com.aiot.aiotbackstage.model.entity.SysSensorRecEntity;
import com.aiot.aiotbackstage.model.entity.SysSiteEntity;
import com.aiot.aiotbackstage.service.ISensorRecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service
public class SensorRecServiceImpl extends ServiceImpl<SysSensorRecMapper, SysSensorRecEntity> implements ISensorRecService {

    @Resource
    private SysSiteMapper sysSiteMapper;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void receive(RtuData rtuData) {
        SysSiteEntity site = sysSiteMapper.selectById(rtuData.getRtu());
        if (site == null) {
            return;
        }
        RtuAddrCode code = RtuAddrCode.trans(rtuData.getAddr());
        if (code == null) {
            return;
        }
        switch (code) {
            case WIND_SPEED:

            case WIND_DIRECTION:
                WindDirection trans = WindDirection.trans(rtuData.getData()[0]);
                if (trans == null) {
                    return;
                }
                SysSensorRecEntity wd = new SysSensorRecEntity()
                        .setSiteId(site.getId())
                        .setSensor(SensorType.wind_direciont.name())
                        .setTime(new Date(System.currentTimeMillis()))
                        .setValue(trans.name());
                save(wd);
            case DUST:

            case ATMOS:
                SysSensorRecEntity humidity = new SysSensorRecEntity()
                        .setSiteId(site.getId())
                        .setSensor(SensorType.humidity.name())
                        .setTime(new Date(System.currentTimeMillis()))
                        .setValue(rtuData.getData()[0] / 10 + "");
                SysSensorRecEntity temperature = new SysSensorRecEntity()
                        .setSiteId(site.getId())
                        .setSensor(SensorType.temperature.name())
                        .setTime(new Date(System.currentTimeMillis()))
                        .setValue(rtuData.getData()[1] / 10  + "");
                SysSensorRecEntity noisy = new SysSensorRecEntity()
                        .setSiteId(site.getId())
                        .setSensor(SensorType.noisy.name())
                        .setTime(new Date(System.currentTimeMillis()))
                        .setValue(rtuData.getData()[2] / 10  + "");
                SysSensorRecEntity PM25 = new SysSensorRecEntity()
                        .setSiteId(site.getId())
                        .setSensor(SensorType.PM25.name())
                        .setTime(new Date(System.currentTimeMillis()))
                        .setValue(rtuData.getData()[0]  + "");
                SysSensorRecEntity PM10 = new SysSensorRecEntity()
                        .setSiteId(site.getId())
                        .setSensor(SensorType.PM10.name())
                        .setTime(new Date(System.currentTimeMillis()))
                        .setValue(rtuData.getData()[0]  + "");
                SysSensorRecEntity atmos = new SysSensorRecEntity()
                        .setSiteId(site.getId())
                        .setSensor(SensorType.atmos.name())
                        .setTime(new Date(System.currentTimeMillis()))
                        .setValue(rtuData.getData()[0] / 10  + "");
                save(humidity, temperature, noisy, PM10, PM25, atmos);
            default:

        }
    }

    private void save(SysSensorRecEntity... entities) {
        List<SysSensorRecEntity> list = Arrays.asList(entities);
        for (SysSensorRecEntity entity : list) {
            redisTemplate.opsForValue().set("SENSOR-VALUE:" + entity.getSiteId() + ":" + entity.getSensor(), entity.getValue(), 60, TimeUnit.SECONDS);
        }
        saveBatch(list);
    }
}
