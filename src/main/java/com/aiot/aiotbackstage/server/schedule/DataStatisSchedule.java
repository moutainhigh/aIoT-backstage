package com.aiot.aiotbackstage.server.schedule;

import com.aiot.aiotbackstage.common.enums.SensorType;
import com.aiot.aiotbackstage.common.util.ListUtils;
import com.aiot.aiotbackstage.mapper.*;
import com.aiot.aiotbackstage.model.entity.SysDustRecStatisEntity;
import com.aiot.aiotbackstage.model.entity.SysInsectRecStatisEntity;
import com.aiot.aiotbackstage.model.entity.SysSensorRecStatisEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

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

    /**
     * 手动执行
     */
    public void manual() {
        long start = System.currentTimeMillis();
        log.info("start statis : {}", start);

        start(null, null);

        long end = System.currentTimeMillis();
        log.info("end statis : {}, run time : {}", end, end - start);
    }

    /**
     * 每小时【*：00：00】统计
     */
//    @Scheduled(cron = "0 0 * * * ?")
    public void statisHourly() {
        Calendar now = Calendar.getInstance();
        Calendar beforeTwoHour = Calendar.getInstance();
        beforeTwoHour.add(Calendar.HOUR_OF_DAY, -2);
        start(getYMDH(beforeTwoHour), getYMDH(now));
    }

    /**
     * 每天【* 00：00：00】统计
     */
//    @Scheduled(cron = "0 0 0 * * ?")
    public void statisDaily() {
        Calendar now = Calendar.getInstance();
        Calendar beforeOneDay = Calendar.getInstance();
        beforeOneDay.add(Calendar.HOUR_OF_DAY, -1);
        beforeOneDay.add(Calendar.DAY_OF_MONTH, -1);
        start(getYMDH(beforeOneDay), getYMDH(now));
    }

    private void start(String startTime, String endTime) {
        startPestStatis(startTime, endTime);
//        startSoilStatis(startTime, endTime);
//        startMeteStatis(startTime, endTime);
    }

    /**
     * 害虫统计
     */
//    @Scheduled(cron = "0 * * * * ?")
    public void startPestStatis(String startTime, String endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        List<SysInsectRecStatisEntity> pestHourly = sysInsectRecMapper.findAllPestNumHourly(params);
        int pestInsertCount = 0;
        for (List<SysInsectRecStatisEntity> entities : ListUtils.partition(pestHourly, PARTITION_SIZE)) {
            pestInsertCount += sysInsectRecStatisMapper.batchInsert(entities);
        }
        log.info("insert pest statis : {}", pestInsertCount);
    }

    /**
     * 土壤统计
     */
//    @Scheduled(cron = "0 * * * * ?")
    public void startSoilStatis(String startTime, String endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        List<SysDustRecStatisEntity> soilHourly = sysDustRecMapper.findAllAverageHourly(params);
        int soilInsertCount = 0;
        for (List<SysDustRecStatisEntity> entities : ListUtils.partition(soilHourly, PARTITION_SIZE)) {
            soilInsertCount += sysDustRecStatisMapper.batchInsert(entities);
        }
        log.info("insert soil statis : {}", soilInsertCount);
    }

    /**
     * 气象统计
     */
//    @Scheduled(cron = "0 * * * * ?")
    public void startMeteStatis(String startTime, String endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        List<SysSensorRecStatisEntity> meteDaily = formatMeteHourly(sysSensorRecMapper.findAllAverageHourly(params));
        int meteInsertCount = 0;
        for (List<SysSensorRecStatisEntity> entities : ListUtils.partition(meteDaily, PARTITION_SIZE)) {
            meteInsertCount += sysSensorRecStatisMapper.batchInsert(entities);
        }
        log.info("insert mete statis : {}", meteInsertCount);
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

    private String getYMDH(Calendar calendar) {
        return calendar.get(Calendar.YEAR)
                + "-" + calendar.get(Calendar.MONTH)
                + "-" + calendar.get(Calendar.DAY_OF_MONTH)
                + " " + calendar.get(Calendar.HOUR_OF_DAY);
    }
}
