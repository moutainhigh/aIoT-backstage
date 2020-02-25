package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.util.DoubleUtils;
import com.aiot.aiotbackstage.mapper.SysInsectRecStatisMapper;
import com.aiot.aiotbackstage.mapper.SysSensorRecStatisMapper;
import com.aiot.aiotbackstage.model.entity.SysSensorRecStatisEntity;
import com.aiot.aiotbackstage.service.ISensorRecStatisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.http.impl.cookie.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SensorRecStatisServiceImpl extends ServiceImpl<SysSensorRecStatisMapper, SysSensorRecStatisEntity> implements ISensorRecStatisService {

    @Autowired
    private SysInsectRecStatisMapper sysInsectRecStatisMapper;

    @Autowired
    private SysSensorRecStatisMapper sysSensorRecStatisMapper;

    @Override
    public List<SysSensorRecStatisEntity> getPestMeteInfo(String siteId, long startTime, long endTime) {
        Date startDate = new Date(startTime);
        Date endDate = new Date(endTime);

        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startTime", DateUtils.formatDate(startDate, "yyyy-MM-dd"));
        params.put("endTime", DateUtils.formatDate(endDate, "yyyy-MM-dd"));

        List<SysSensorRecStatisEntity> result = sysSensorRecStatisMapper.findAllBySiteId(params);

        // 查询当天每小时平均值
        if (DateUtils.formatDate(startDate, "yyyy-MM-dd")
                .equals(DateUtils.formatDate(startDate, "yyyy-MM-dd"))) {
            return result;
        }

        return formatDayOrNight(result);
    }

    @Override
    public List<SysSensorRecStatisEntity> getMaxOrMinPestMeteInfo(String siteId, long startTime, long endTime, int isMax) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startTime", DateUtils.formatDate(new Date(startTime), "yyyy-MM-dd"));
        params.put("endTime", DateUtils.formatDate(new Date(endTime), "yyyy-MM-dd"));
        params.put("isMax", isMax);
        List<Map<String, Object>> pestResult = sysInsectRecStatisMapper.findMaxOrMinPestDate(params);

        if (pestResult != null && pestResult.size() == 1) {
            String pestDate = String.valueOf(pestResult.get(0).get("date"));
            params.clear();
            params.put("siteId", siteId);
            params.put("startTime", pestDate);
            params.put("endTime", pestDate);
            return formatDayOrNight(sysSensorRecStatisMapper.findAllBySiteId(params));
        }
        return null;
    }

    private List<SysSensorRecStatisEntity> formatDayOrNight(List<SysSensorRecStatisEntity> list) {
        Map<String, List<SysSensorRecStatisEntity>> map = new HashMap<>();
        List<SysSensorRecStatisEntity> tempList;
        for (SysSensorRecStatisEntity entity : list) {
            tempList = new ArrayList<>();
            if (map.containsKey(entity.getDate())) {
                tempList = map.get(entity.getDate());
            } else {
                map.put(entity.getDate(), tempList);
            }
            tempList.add(entity);
        }

        List<SysSensorRecStatisEntity> data = new ArrayList<>();
        SysSensorRecStatisEntity entity;
        for (Map.Entry<String, List<SysSensorRecStatisEntity>> entry : map.entrySet()) {
            entity = new SysSensorRecStatisEntity();
            entity.setDate(entry.getKey());
            data.add(entity);

            for (SysSensorRecStatisEntity item : entry.getValue()) {
                entity.setWindSpeed(DoubleUtils.average(entity.getWindSpeed(), item.getWindSpeed()));
                entity.setWindDirection(DoubleUtils.average(entity.getWindDirection(), item.getWindDirection()));
                entity.setHumidity(DoubleUtils.average(entity.getHumidity(), item.getHumidity()));
                entity.setTemperature(DoubleUtils.average(entity.getTemperature(), item.getTemperature()));
                entity.setNoisy(DoubleUtils.average(entity.getNoisy(), item.getNoisy()));
                entity.setPM10(DoubleUtils.average(entity.getPM10(), item.getPM10()));
                entity.setPM25(DoubleUtils.average(entity.getPM25(), item.getPM25()));
                entity.setAtmos(DoubleUtils.average(entity.getAtmos(), item.getAtmos()));

                // 白天
                if (item.getHour() > 6 && item.getHour() < 19) {
                    entity.setHumidityDay(DoubleUtils.average(entity.getHumidityDay(), item.getHumidity()));
                    entity.setTemperatureDay(DoubleUtils.average(entity.getTemperatureDay(), item.getTemperature()));
                } else {
                    entity.setHumidityNight(DoubleUtils.average(entity.getHumidityNight(), item.getHumidity()));
                    entity.setTemperatureNight(DoubleUtils.average(entity.getTemperatureNight(), item.getTemperature()));
                }
            }
        }

        return data;
    }
}
