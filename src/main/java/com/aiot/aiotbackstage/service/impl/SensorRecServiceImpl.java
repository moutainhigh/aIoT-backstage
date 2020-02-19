package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.enums.RtuAddrCode;
import com.aiot.aiotbackstage.common.enums.SensorType;
import com.aiot.aiotbackstage.common.enums.WindDirection;
import com.aiot.aiotbackstage.mapper.SysDustRecMapper;
import com.aiot.aiotbackstage.mapper.SysSensorRecMapper;
import com.aiot.aiotbackstage.mapper.SysSiteMapper;
import com.aiot.aiotbackstage.model.dto.RtuData;
import com.aiot.aiotbackstage.model.entity.SysDustRecEntity;
import com.aiot.aiotbackstage.model.entity.SysSensorRecEntity;
import com.aiot.aiotbackstage.model.entity.SysSiteEntity;
import com.aiot.aiotbackstage.service.ISensorRecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author Avernus
 */
@Service
public class SensorRecServiceImpl extends ServiceImpl<SysSensorRecMapper, SysSensorRecEntity> implements ISensorRecService {

    @Resource
    private SysSiteMapper sysSiteMapper;
    @Resource
    private SysDustRecMapper sysDustRecMapper;
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

        Date time = new Date(System.currentTimeMillis());
        switch (code) {
            case WIND_SPEED:
                SysSensorRecEntity ws = new SysSensorRecEntity(site.getId(), SensorType.wind_speed.name(), rtuData.getData()[0] / 10 + "", time);
                save(ws);
                break;
            case WIND_DIRECTION:
                String trans = WindDirection.trans(rtuData.getData()[0]);
                SysSensorRecEntity wd = new SysSensorRecEntity(site.getId(), SensorType.wind_direction.name(), trans, time);
                save(wd);
                break;
            case ATMOS:
                SysSensorRecEntity humidity = new SysSensorRecEntity(site.getId(), SensorType.humidity.name(),rtuData.getData()[0] / 10 + "", time);
                SysSensorRecEntity temperature = new SysSensorRecEntity(site.getId(), SensorType.temperature.name(), rtuData.getData()[1] / 10 + "", time);
                SysSensorRecEntity noisy = new SysSensorRecEntity(site.getId(), SensorType.noise.name(), rtuData.getData()[2] / 10 + "", time);
                SysSensorRecEntity PM25 = new SysSensorRecEntity(site.getId(), SensorType.PM25.name(), rtuData.getData()[3] + "", time);
                SysSensorRecEntity PM10 = new SysSensorRecEntity(site.getId(), SensorType.PM10.name(), rtuData.getData()[4] + "", time);
                SysSensorRecEntity atmos = new SysSensorRecEntity(site.getId(), SensorType.atmos.name(), rtuData.getData()[5] / 10 + "", time);
                save(humidity, temperature, noisy, PM10, PM25, atmos);
                break;
            case DUST_10CM:
                SysDustRecEntity dust10 = new SysDustRecEntity(site.getId(), 10, rtuData.getData(), time);
                save(dust10);
                break;
            case DUST_20CM:
                SysDustRecEntity dust20 = new SysDustRecEntity(site.getId(), 20, rtuData.getData(), time);
                save(dust20);
                break;
            case DUST_40CM:
                SysDustRecEntity dust40 = new SysDustRecEntity(site.getId(), 40, rtuData.getData(), time);
                save(dust40);
                break;
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

    private void save(SysDustRecEntity entity) {
        redisTemplate.opsForValue().set("SENSOR-VALUE:" + entity.getSiteId() + ":" + entity.getDepth(), entity, 60, TimeUnit.SECONDS);
        sysDustRecMapper.insert(entity);
    }
}
