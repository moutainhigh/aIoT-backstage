package com.aiot.aiotbackstage.server.schedule;

import com.aiot.aiotbackstage.common.enums.SensorType;
import com.aiot.aiotbackstage.common.util.ListUtils;
import com.aiot.aiotbackstage.mapper.*;
import com.aiot.aiotbackstage.model.entity.SysSensorRecStatisEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class DataStatisSchedule {

    @Autowired
    private SysInsectRecMapper sysInsectRecMapper;

    @Autowired
    private SysInsectRecStatisMapper sysInsectRecStatisMapper;

    @Autowired
    private SysDustRecMapper sysDustRecMapper;

    @Autowired
    private SysDustRecStatisMapper sysDustRecStatisMapper;

    @Autowired
    private SysSensorRecMapper sysSensorRecMapper;

    @Autowired
    private SysSensorRecStatisMapper sysSensorRecStatisMapper;

    private static final int PARTITION_SIZE = 1000;

    // 每5秒执行一次
//    @Scheduled(cron = "0 * * * * ?")
    public void start() {
        long start = System.currentTimeMillis();
        log.info("start stat : {}", start);

//        // 害虫情况
//        List<SysInsectRecStatisEntity> pestHourly = sysInsectRecMapper.findAllPestNumHourly();
//        int pestInsertCount = 0;
//        for (List<SysInsectRecStatisEntity> entities : ListUtils.partition(pestHourly, PARTITION_SIZE)) {
//            pestInsertCount += sysInsectRecStatisMapper.batchInsert(entities);
//        }
//        log.info("insert pest stat : {}", pestInsertCount);

//        // 土壤信息
//        List<SysDustRecStatisEntity> soilHourly = sysDustRecMapper.findAllAverageHourly();
//        int soilInsertCount = 0;
//        for (List<SysDustRecStatisEntity> entities : ListUtils.partition(soilHourly, PARTITION_SIZE)) {
//            soilInsertCount += sysDustRecStatisMapper.batchInsert(entities);
//        }
//        log.info("insert soil stat : {}", soilInsertCount);

        // 气象信息
        List<SysSensorRecStatisEntity> meteDaily = formatMeteHourly(sysSensorRecMapper.findAllAverageHourly());
        int meteInsertCount = 0;
        for (List<SysSensorRecStatisEntity> entities : ListUtils.partition(meteDaily, PARTITION_SIZE)) {
            meteInsertCount += sysSensorRecStatisMapper.batchInsert(entities);
        }
        log.info("insert mete stat : {}", meteInsertCount);

        long end = System.currentTimeMillis();
        log.info("end stat : {}, run time : {}", end, end - start);
    }

    private List<SysSensorRecStatisEntity> formatMeteHourly(List<Map<String, Object>> list) {
        List<Map<String, Object>> tempList;
        Map<String, Map<String, Map<Integer, List<Map<String, Object>>>>> tempSiteMap = new HashMap<>();
        Map<String, Map<Integer, List<Map<String, Object>>>> tempDateMap;
        Map<Integer, List<Map<String, Object>>> tempHourMap;
        String siteId;
        String date;
        Integer hour;
        for (Map<String, Object> item : list) {
            if (!item.containsKey("site_id")
                    || !item.containsKey("date")
                    || !item.containsKey("hour")) {
                continue;
            }
            siteId = String.valueOf(item.get("site_id"));
            date = (String) item.get("date");
            hour = Integer.valueOf((String) item.get("hour"));

            tempList = new ArrayList<>();
            if (tempSiteMap.containsKey(siteId)) {
                tempDateMap = tempSiteMap.get(siteId);
                if (tempDateMap.containsKey(date)) {
                    tempHourMap = tempDateMap.get(date);
                    if (tempHourMap.containsKey(hour)) {
                        tempList = tempHourMap.get(hour);
                    } else {
                        tempHourMap.put(hour, tempList);
                    }
                } else {
                    tempHourMap = new HashMap<>();
                    tempHourMap.put(hour, tempList);
                    tempDateMap.put(date, tempHourMap);
                }
            } else {
                tempHourMap = new HashMap<>();
                tempHourMap.put(hour, tempList);

                tempDateMap = new HashMap<>();
                tempDateMap.put(date, tempHourMap);

                tempSiteMap.put(siteId, tempDateMap);
            }
            tempList.add(item);
        }

        List<SysSensorRecStatisEntity> data = new ArrayList<>(list.size());
        SysSensorRecStatisEntity entity;
        for (Map.Entry<String, Map<String, Map<Integer, List<Map<String, Object>>>>> siteEntry : tempSiteMap.entrySet()) {
            for (Map.Entry<String, Map<Integer, List<Map<String, Object>>>> dateEntry : siteEntry.getValue().entrySet()) {
                for (Map.Entry<Integer, List<Map<String, Object>>> hourEntry : dateEntry.getValue().entrySet()) {
                    entity = new SysSensorRecStatisEntity();
                    data.add(entity);

                    entity.setSiteId(siteEntry.getKey());
                    entity.setDate(dateEntry.getKey());
                    entity.setHour(hourEntry.getKey());

                    for (Map<String, Object> item : hourEntry.getValue()) {
                        formatMeteItem(entity, item);
                    }
                }
            }
        }
        return data;
    }

    private void formatMeteItem(SysSensorRecStatisEntity entity, Map<String, Object> item) {
        if (SensorType.wind_speed.name().equals(item.get("sensor"))) {
            entity.setWindSpeed((Double) item.get("value"));
        } else if (SensorType.wind_direction.name().equals(item.get("sensor"))) {
            entity.setWindDirection((Double) item.get("value"));
        } else if (SensorType.humidity.name().equals(item.get("sensor"))) {
            entity.setHumidity((Double) item.get("value"));
        } else if (SensorType.temperature.name().equals(item.get("sensor"))) {
            entity.setTemperature((Double) item.get("value"));
        } else if (SensorType.noise.name().equals(item.get("sensor"))) {
            entity.setNoisy((Double) item.get("value"));
        } else if (SensorType.PM10.name().equals(item.get("sensor"))) {
            entity.setPM10((Double) item.get("value"));
        } else if (SensorType.PM25.name().equals(item.get("sensor"))) {
            entity.setPM25((Double) item.get("value"));
        } else if (SensorType.atmos.name().equals(item.get("sensor"))) {
            entity.setAtmos((Double) item.get("value"));
        }
    }
}
