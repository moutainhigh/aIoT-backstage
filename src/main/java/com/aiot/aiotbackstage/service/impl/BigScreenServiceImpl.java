package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.enums.RtuAddrCode;
import com.aiot.aiotbackstage.common.enums.SensorType;
import com.aiot.aiotbackstage.common.util.JsonUtils;
import com.aiot.aiotbackstage.common.util.RedisUtils;
import com.aiot.aiotbackstage.mapper.SysInsectDeviceMapper;
import com.aiot.aiotbackstage.mapper.SysInsectInfoMapper;
import com.aiot.aiotbackstage.mapper.SysInsectRecMapper;
import com.aiot.aiotbackstage.mapper.SysSiteMapper;
import com.aiot.aiotbackstage.model.entity.SysDustRecEntity;
import com.aiot.aiotbackstage.model.entity.SysInsectDeviceEntity;
import com.aiot.aiotbackstage.model.entity.SysInsectRecEntity;
import com.aiot.aiotbackstage.model.entity.SysSiteEntity;
import com.aiot.aiotbackstage.model.vo.InsectStatisticsVo;
import com.aiot.aiotbackstage.service.IBigScreenService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/3/3 15:30
 */
@Service
public class BigScreenServiceImpl implements IBigScreenService {


    @Autowired
    private SysInsectRecMapper insectRecMapper;

    @Autowired
    private SysInsectDeviceMapper insectDeviceMapper;

    @Autowired
    private SysSiteMapper siteMapper;

    @Autowired
    private RedisUtils redisUtils;



    @Override
    public InsectStatisticsVo insectStatistics() {

        List<SysInsectDeviceEntity> sysInsectDeviceEntities = insectDeviceMapper.selectList(null);
        Map<Integer, List<SysInsectDeviceEntity>> collect = sysInsectDeviceEntities.stream()
                .collect(Collectors.groupingBy(SysInsectDeviceEntity::getSiteId, Collectors.toList()));
        Set<Integer> integers = collect.keySet();
        int sumTotal=0;
        List<Map<String,Object>> list=new ArrayList<>();
        for (Integer integer:integers
             ) {
            List<SysInsectRecEntity> sysInsectRecEntities = insectRecMapper.insectRecGisInfo(integer);
            SysInsectRecEntity sysInsectRecEntity = sysInsectRecEntities.get(0);
            String result = sysInsectRecEntity.getResult();
            String[] split = result.split("#");
            int total=0;
            for (String s:split) {
                String count = s.substring(s.indexOf(",")+1,s.length());
                total+= Integer.parseInt(count);
            }
            SysSiteEntity sysSiteEntity = siteMapper.selectById(integer);
            Map<String,Object> map=new HashMap<>();
            map.put("name",sysSiteEntity.getName());
            map.put("value,",total);
            list.add(map);
            sumTotal+=total;
        }
        InsectStatisticsVo statisticsVo=new InsectStatisticsVo();
        statisticsVo.setTotal(sumTotal);
        statisticsVo.setList(list);
        return statisticsVo;
    }

    @Override
    public Map<String,Object> dustRecStatistics() {
        List<SysSiteEntity> sysSiteEntities = siteMapper.selectList(null);
        List<String> list=new ArrayList<>();
        List<SysDustRecEntity> entityList=new ArrayList<>();
        sysSiteEntities.forEach(sysSiteEntity -> {
            list.add(sysSiteEntity.getName());
            Object redisObject10CM = redisUtils.get("SENSOR-VALUE:" + sysSiteEntity.getId() + ":" + 10);
            Object redisObject20CM = redisUtils.get("SENSOR-VALUE:" + sysSiteEntity.getId() + ":" + 20);
            Object redisObject40CM = redisUtils.get("SENSOR-VALUE:" + sysSiteEntity.getId() + ":" + 40);
            if(redisObject10CM != null){
                SysDustRecEntity as = (SysDustRecEntity)redisObject10CM;
                entityList.add(as);
            }
            if(redisObject20CM != null){
                SysDustRecEntity as = (SysDustRecEntity)redisObject20CM;
                entityList.add(as);
            }
            if(redisObject40CM != null){
                SysDustRecEntity as = (SysDustRecEntity)redisObject40CM;
                entityList.add(as);
            }
        });
        if(!CollectionUtils.isEmpty(entityList)){
            Map<Integer, List<SysDustRecEntity>> collect = entityList.stream().collect(Collectors.groupingBy(SysDustRecEntity::getDepth, Collectors.toList()));
            Set<Integer> depths = collect.keySet();
            List<Map<String,Object>> temperatureList=new ArrayList<>();
            List<Map<String,Object>> wcList=new ArrayList<>();
            List<Map<String,Object>> salinityList=new ArrayList<>();
            depths.forEach(depth -> {
                List<SysDustRecEntity> entityList1 = collect.get(depth);
                List<Integer> siteIds=entityList1.stream().map(SysDustRecEntity::getSiteId).collect(Collectors.toList());
                List<Double> temperature=entityList1.stream().map(SysDustRecEntity::getTemperature).collect(Collectors.toList());
                List<Double> wc=entityList1.stream().map(SysDustRecEntity::getWc).collect(Collectors.toList());
                List<Double> salinity=entityList1.stream().map(SysDustRecEntity::getSalinity).collect(Collectors.toList());

                Map<String,Object> map=new HashMap<>();
                map.put("name",depth);
                if(!CollectionUtils.isEmpty(temperature)){
                    map.put("data",setValues(temperature,siteIds));
                }
                temperatureList.add(map);
                Map<String,Object> map1=new HashMap<>();
                map1.put("name",depth);
                if(!CollectionUtils.isEmpty(wc)){
                    map1.put("data",setValues(wc,siteIds));
                }
                wcList.add(map1);
                Map<String,Object> map2=new HashMap<>();
                map2.put("name",depth);
                if(!CollectionUtils.isEmpty(salinity)){
                    map2.put("data",setValues(salinity,siteIds));
                }
                salinityList.add(map2);
            });
            Map<String,Object> resultMap=new HashMap<>();
            resultMap.put("siteName",list);

            resultMap.put("temperature",temperatureList);
            resultMap.put("wc",wcList);
            resultMap.put("salinity",salinityList);
            return resultMap;
        }
        return null;
    }

    private List<Double> setValues(List<Double> doubleList,List<Integer> siteIds){
        Double[] i = {0.00,0.00,0.00,0.00,0.00,0.00,0.00,0.00};
        List<Double> doubles = Arrays.asList(i);
        for (int i1 = 0; i1 < doubles.size(); i1++) {
            for (Integer siteId : siteIds) {
                for (Double aDouble1 : doubleList) {
                    if(siteId-1 == i1){
                        doubles.set(i1,aDouble1);
                    }
                }
            }
        }
        return doubles;
    }

    @Override
    public Object sensorRecStatistics() {
        List<Map<String,Object>> sensorRecVos=new ArrayList<>();
        List<SysSiteEntity> sysSiteEntities = siteMapper.selectList(null);
        sysSiteEntities.stream().forEach(sysSiteEntity -> {
            Map<String,Object> map=new HashMap<>();
            map.put("siteName",sysSiteEntity.getName());
            //气象数据
            List<Map<String,Object>> maps=new ArrayList<>();
            Arrays.stream(SensorType.values()).forEach(sensorType -> {
                Map<String,Object> map1=new HashMap<>();
                Object o = redisUtils.get("SENSOR-VALUE:" + sysSiteEntity.getId() + ":" + sensorType.name());
                map1.put(sensorType.name(),o);
                maps.add(map1);
            });
            map.put("sensorRecVos",maps);
            sensorRecVos.add(map);
        });

        return sensorRecVos;
    }
}
