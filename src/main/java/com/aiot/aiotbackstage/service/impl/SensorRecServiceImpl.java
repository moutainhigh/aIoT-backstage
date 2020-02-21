package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.enums.RtuAddrCode;
import com.aiot.aiotbackstage.common.enums.SensorType;
import com.aiot.aiotbackstage.common.enums.WindDirection;
import com.aiot.aiotbackstage.common.util.DoubleUtils;
import com.aiot.aiotbackstage.mapper.SysDustRecMapper;
import com.aiot.aiotbackstage.mapper.SysInsectRecMapper;
import com.aiot.aiotbackstage.mapper.SysSensorRecMapper;
import com.aiot.aiotbackstage.mapper.SysSiteMapper;
import com.aiot.aiotbackstage.model.dto.RtuData;
import com.aiot.aiotbackstage.model.entity.SysDustRecEntity;
import com.aiot.aiotbackstage.model.entity.SysSensorRecEntity;
import com.aiot.aiotbackstage.model.entity.SysSiteEntity;
import com.aiot.aiotbackstage.model.vo.SysDustRecVo;
import com.aiot.aiotbackstage.model.vo.SysSensorRecVo2;
import com.aiot.aiotbackstage.service.ISensorRecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.http.impl.cookie.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SysSensorRecMapper sysSensorRecMapper;

    @Autowired
    private SysInsectRecMapper sysInsectRecMapper;

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
                SysSensorRecEntity humidity = new SysSensorRecEntity(site.getId(), SensorType.humidity.name(), rtuData.getData()[0] / 10 + "", time);
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

    @Override
    public SysSensorRecVo2 getBiggestPestMeteInfo(String siteId, long startTime, long endTime) {
        List<SysSensorRecVo2> stat = getWholeDayMeteInfo(siteId, startTime, endTime);

        Map<String, Object> params = new HashMap<>();
        params.put("startTime", DateUtils.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss"));
        params.put("endTime", DateUtils.formatDate(new Date(endTime), "yyyy-MM-dd HH:mm:ss"));
        List<Map<String, Object>> pestStatByDay = sysInsectRecMapper.findPestStatByDay(params);

        // 获取虫害最大日期
        Optional<Map<String, Object>> max = pestStatByDay.stream().max((Comparator.comparing(o -> ((Double) o.get("insect_num")))));
        if (max.isPresent()) {
            String biggestPestDate = (String) max.get().get("date");
            for (SysSensorRecVo2 item : stat) {
                if (item.getDate().equals(biggestPestDate)) {
                    return item;
                }
            }
        }
        return null;
    }

    @Override
    public SysSensorRecVo2 getMinimumPestMeteInfo(String siteId, long startTime, long endTime) {
        List<SysSensorRecVo2> stat = getWholeDayMeteInfo(siteId, startTime, endTime);

        Map<String, Object> params = new HashMap<>();
        params.put("startTime", DateUtils.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss"));
        params.put("endTime", DateUtils.formatDate(new Date(endTime), "yyyy-MM-dd HH:mm:ss"));
        List<Map<String, Object>> pestStatByDay = sysInsectRecMapper.findPestStatByDay(params);

        // 获取虫害最大日期
        Optional<Map<String, Object>> min = pestStatByDay.stream().max((Comparator.comparing(o -> ((Double) o.get("insect_num")))));
        if (min.isPresent()) {
            String minimumPestDate = (String) min.get().get("date");
            for (SysSensorRecVo2 item : stat) {
                if (item.getDate().equals(minimumPestDate)) {
                    return item;
                }
            }
        }
        return null;
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

    private List<SysSensorRecVo2> getWholeDayMeteInfo(String siteId, long startTime, long endTime) {
        List<SysSensorRecVo2> dayStat = getDayMeteInfo(siteId, startTime, endTime);
        List<SysSensorRecVo2> nightStat = getNightMeteInfo(siteId, startTime, endTime);

        Map<String, SysSensorRecVo2> map = new HashMap<>();
        for (int i = 0; i < dayStat.size(); i++) {
            SysSensorRecVo2 dayItem = dayStat.get(i);
            map.put(dayItem.getDate(), dayItem);
        }
        for (int i = 0; i < nightStat.size(); i++) {
            SysSensorRecVo2 nightItem = nightStat.get(i);
            if (map.containsKey(nightItem.getDate())) {
                SysSensorRecVo2 dayItem = map.get(nightItem.getDate());

                dayItem.setDate(dayItem.getDate());
                dayItem.setWindSpeed(DoubleUtils.average(dayItem.getWindSpeed(), nightItem.getWindSpeed()));
                dayItem.setWindDirection(DoubleUtils.average(dayItem.getWindDirection(), nightItem.getWindDirection()));
                dayItem.setNoisy(DoubleUtils.average(dayItem.getNoisy(), nightItem.getNoisy()));
                dayItem.setPM10(DoubleUtils.average(dayItem.getPM10(), nightItem.getPM10()));
                dayItem.setPM25(DoubleUtils.average(dayItem.getPM25(), nightItem.getPM25()));
                dayItem.setAtmos(DoubleUtils.average(dayItem.getAtmos(), nightItem.getAtmos()));
                // 温湿度区分白天黑夜
                dayItem.setHumidity(dayItem.getHumidity());
                dayItem.setHumidityNightly(nightItem.getHumidity());
                dayItem.setTemperature(dayItem.getTemperature());
                dayItem.setTemperatureNightly(nightItem.getTemperature());
            } else {
                map.put(nightItem.getDate(), nightItem);
            }
        }

        List<SysSensorRecVo2> stat = new ArrayList<>(map.size());
        map.forEach((key, value) -> {
            stat.add(value);
        });

        return stat;
    }

    private List<SysSensorRecVo2> getDayMeteInfo(String siteId, long startTime, long endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startTime", DateUtils.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss"));
        params.put("endTime", DateUtils.formatDate(new Date(endTime), "yyyy-MM-dd HH:mm:ss"));
        params.put("sTime", "06:00:01");
        params.put("eTime", "18:59:59");
        return formatStatMap(sysSensorRecMapper.findAllAverageByDay(params));
    }

    private List<SysSensorRecVo2> getNightMeteInfo(String siteId, long startTime, long endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startTime", DateUtils.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss"));
        params.put("endTime", DateUtils.formatDate(new Date(endTime), "yyyy-MM-dd HH:mm:ss"));
        params.put("sTime", "00:00:00");
        params.put("eTime", "06:00:00");
        List<SysSensorRecVo2> nightOneStat = formatStatMap(sysSensorRecMapper.findAllAverageByDay(params));

        params.clear();
        params.put("siteId", siteId);
        params.put("startTime", DateUtils.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss"));
        params.put("endTime", DateUtils.formatDate(new Date(endTime), "yyyy-MM-dd HH:mm:ss"));
        params.put("sTime", "19:00:00");
        params.put("eTime", "23:59:59");
        List<SysSensorRecVo2> nightTwoStat = formatStatMap(sysSensorRecMapper.findAllAverageByDay(params));

        Map<String, SysSensorRecVo2> nightMap = new HashMap<>();
        for (int i = 0; i < nightOneStat.size(); i++) {
            SysSensorRecVo2 nightOneItem = nightOneStat.get(i);
            nightMap.put(nightOneItem.getDate(), nightOneItem);
        }

        for (int i = 0; i < nightTwoStat.size(); i++) {
            SysSensorRecVo2 nightTwoItem = nightTwoStat.get(i);
            if (nightMap.containsKey(nightTwoItem.getDate())) {
                SysSensorRecVo2 nightOneItem = nightMap.get(nightTwoItem.getDate());
                nightOneItem.setDate(nightOneItem.getDate());
                nightOneItem.setWindSpeed(DoubleUtils.average(nightOneItem.getWindSpeed(), nightTwoItem.getWindSpeed()));
                nightOneItem.setWindDirection(DoubleUtils.average(nightOneItem.getWindDirection(), nightTwoItem.getWindDirection()));
                nightOneItem.setHumidity(DoubleUtils.average(nightOneItem.getHumidity(), nightTwoItem.getHumidity()));
                nightOneItem.setTemperature(DoubleUtils.average(nightOneItem.getTemperature(), nightTwoItem.getTemperature()));
                nightOneItem.setNoisy(DoubleUtils.average(nightOneItem.getNoisy(), nightTwoItem.getNoisy()));
                nightOneItem.setPM10(DoubleUtils.average(nightOneItem.getPM10(), nightTwoItem.getPM10()));
                nightOneItem.setPM25(DoubleUtils.average(nightOneItem.getPM25(), nightTwoItem.getPM25()));
                nightOneItem.setAtmos(DoubleUtils.average(nightOneItem.getAtmos(), nightTwoItem.getAtmos()));
            } else {
                nightMap.put(nightTwoItem.getDate(), nightTwoItem);
            }
        }

        List<SysSensorRecVo2> nightStat = new ArrayList<>(nightMap.size());
        nightMap.forEach((key, value) -> {
            nightStat.add(value);
        });

        return nightStat;
    }

    private List<SysSensorRecVo2> formatStatMap(List<Map<String, Object>> list) {
        List<SysSensorRecVo2> data = new ArrayList<>();
        SysSensorRecVo2 tempVo;
        List<Map<String, Object>> tempList;
        Map<String, List<Map<String, Object>>> tempMap = new HashMap<>();
        for (Map<String, Object> item : list) {
            if (!item.containsKey("date")) {
                continue;
            }
            tempList = new ArrayList<>();
            if (tempMap.containsKey(item.get("date"))) {
                tempList = tempMap.get(item.get("date"));
            } else {
                tempMap.put((String) item.get("date"), tempList);
            }
            tempList.add(item);
        }

        for (Map.Entry<String, List<Map<String, Object>>> entry : tempMap.entrySet()) {
            tempVo = new SysSensorRecVo2();
            data.add(tempVo);

            tempVo.setDate(entry.getKey());
            for (Map<String, Object> item : entry.getValue()) {
                if (SensorType.wind_speed.name().equals(item.get("sensor"))) {
                    tempVo.setWindSpeed((Double) item.get("value"));
                } else if (SensorType.wind_direction.name().equals(item.get("sensor"))) {
                    tempVo.setWindDirection((Double) item.get("value"));
                } else if (SensorType.humidity.name().equals(item.get("sensor"))) {
                    tempVo.setHumidity((Double) item.get("value"));
                } else if (SensorType.temperature.name().equals(item.get("sensor"))) {
                    tempVo.setTemperature((Double) item.get("value"));
                } else if (SensorType.noise.name().equals(item.get("sensor"))) {
                    tempVo.setNoisy((Double) item.get("value"));
                } else if (SensorType.PM10.name().equals(item.get("sensor"))) {
                    tempVo.setPM10((Double) item.get("value"));
                } else if (SensorType.PM25.name().equals(item.get("sensor"))) {
                    tempVo.setPM25((Double) item.get("value"));
                } else if (SensorType.atmos.name().equals(item.get("sensor"))) {
                    tempVo.setAtmos((Double) item.get("value"));
                }
            }
        }
        return data;
    }
}
