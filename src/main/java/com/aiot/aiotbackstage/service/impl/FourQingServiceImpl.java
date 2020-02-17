package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.mapper.*;
import com.aiot.aiotbackstage.model.entity.*;
import com.aiot.aiotbackstage.model.vo.*;
import com.aiot.aiotbackstage.service.IFourQingService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FourQingServiceImpl implements IFourQingService {

    @Autowired
    private SysDisasterSituationMapper disasterSituationMapper;

    @Autowired
    private SysHumidityTempMapper humidityTempMapper;

    @Autowired
    private SysMeteorologicalInfoMapper meteorologicalInfoMapper;

    @Autowired
    private SysSeedlingGrowthMapper seedlingGrowthMapper;

    @Autowired
    private SysSensorRecMapper sysSensorRecMapper;


    @Override
    public FourQingVo monitorInfo(Long stationId) {

        FourQingVo qingVo=new FourQingVo();
        qingVo.setStationId(stationId);
        //灾情
        List<SysDisasterSituationEntity> disasterSituationEntities =
                disasterSituationMapper.selectList(Wrappers
                        .<SysDisasterSituationEntity>lambdaQuery()
                        .eq(SysDisasterSituationEntity::getStationId, stationId));
        if(CollectionUtils.isEmpty(disasterSituationEntities)){
            qingVo.setDisasterSituation(null);
        }
        qingVo.setDisasterSituation(disasterSituationEntities);

        //气象信息
        List<SysMeteorologicalInfoEntity> meteorologicalInfoEntities =
                meteorologicalInfoMapper.selectList(Wrappers
                        .<SysMeteorologicalInfoEntity>lambdaQuery()
                        .eq(SysMeteorologicalInfoEntity::getStationId, stationId));
        if(CollectionUtils.isEmpty(meteorologicalInfoEntities)){
            qingVo.setMeteorologicalInfo(null);
        }
        qingVo.setMeteorologicalInfo(meteorologicalInfoEntities);


        List<SysSeedlingGrowthEntity> sysSeedlingGrowthEntities =
                seedlingGrowthMapper.selectList(Wrappers
                        .<SysSeedlingGrowthEntity>lambdaQuery()
                        .eq(SysSeedlingGrowthEntity::getStationId, stationId));
        if(CollectionUtils.isEmpty(sysSeedlingGrowthEntities)){
            qingVo.setSeedlingGrowth(null);
        }
        Map<Integer, List<SysSeedlingGrowthEntity>> collect = sysSeedlingGrowthEntities.stream().collect(Collectors.groupingBy(SysSeedlingGrowthEntity::getType, Collectors.toList()));
        //苗情
        qingVo.setSeedlingGrowth(collect.get(1));
        //虫情
        qingVo.setChongqing(collect.get(2));

        List<SysTempRegionVo> tempRegionVos=new ArrayList<>();
        List<SysHumidityRegionVo> humidityRegionVos=new ArrayList<>();
        List<SysHumidityTempEntity> humidityTempEntities = humidityTempMapper.selectList(Wrappers
                .<SysHumidityTempEntity>lambdaQuery()
                .eq(SysHumidityTempEntity::getStationId, stationId));
        Map<Integer, List<SysHumidityTempEntity>> typeCollect = humidityTempEntities
                .stream()
                .collect(Collectors
                        .groupingBy(SysHumidityTempEntity::getType, Collectors.toList()));
        List<SysHumidityTempEntity> humidityTempEntities1 = typeCollect.get(1);
        List<SysHumidityTempEntity> humidityTempEntities2 = typeCollect.get(2);
        TreeMap<String, List<SysHumidityTempEntity>> regionCollect = humidityTempEntities1
                .stream()
                .collect(Collectors
                .groupingBy(SysHumidityTempEntity::getRegion, TreeMap::new, Collectors.toList()));
        Set<String> strings = regionCollect.keySet();
        strings.stream().forEach(s -> {
            SysTempRegionVo sysTempRegionVo=new SysTempRegionVo();
            sysTempRegionVo.setRegion(s);
            List<SysHumidityTempEntity> humidityTempEntitieList = regionCollect.get(s);
            List<SysHumidityTempTimeVo> humidityTempTimeVo=new ArrayList<>();
            humidityTempEntitieList.stream().forEach(sysHumidityTempEntity -> {
                SysHumidityTempTimeVo sysHumidityTempTimeVo=new SysHumidityTempTimeVo();
                sysHumidityTempTimeVo.setTime(sysHumidityTempEntity.getTime());
                sysHumidityTempTimeVo.setVar(sysHumidityTempEntity.getVar());
                humidityTempTimeVo.add(sysHumidityTempTimeVo);
            });
            sysTempRegionVo.setHumidityTempTimes(humidityTempTimeVo);
            tempRegionVos.add(sysTempRegionVo);
        });
        TreeMap<String, List<SysHumidityTempEntity>> regionHumidityCollect = humidityTempEntities2
                .stream()
                .collect(Collectors
                        .groupingBy(SysHumidityTempEntity::getRegion, TreeMap::new, Collectors.toList()));
        Set<String> strings1 = regionHumidityCollect.keySet();
        strings1.stream().forEach(s -> {
            SysHumidityRegionVo humidityRegionVo=new SysHumidityRegionVo();
            humidityRegionVo.setRegion(s);
            List<SysHumidityTempEntity> humidityTempEntitieList = regionHumidityCollect.get(s);
            List<SysHumidityTempTimeVo> humidityTempTimeVo=new ArrayList<>();
            humidityTempEntitieList.stream().forEach(sysHumidityTempEntity -> {
                SysHumidityTempTimeVo sysHumidityTempTimeVo=new SysHumidityTempTimeVo();
                sysHumidityTempTimeVo.setTime(sysHumidityTempEntity.getTime());
                sysHumidityTempTimeVo.setVar(sysHumidityTempEntity.getVar());
                humidityTempTimeVo.add(sysHumidityTempTimeVo);
            });
            humidityRegionVo.setHumidityTempTimes(humidityTempTimeVo);
            humidityRegionVos.add(humidityRegionVo);
        });
        //土壤温度
        qingVo.setTempRegionVos(tempRegionVos);
        //土壤湿度
        qingVo.setHumidityRegionVos(humidityRegionVos);
        return qingVo;
    }

    @Override
    public List<SysSensorRecVo> meteorological(Long stationId) {

        List<SysSensorRecEntity> sysSensorRecEntities = sysSensorRecMapper
                .selectList(Wrappers.<SysSensorRecEntity>lambdaQuery()
                .eq(SysSensorRecEntity::getSiteId, stationId));
        if(CollectionUtils.isEmpty(sysSensorRecEntities)){
            throw new MyException(ResultStatusCode.SYSSENSORREC_NO_EXIT);
        }
        Map<Date, List<SysSensorRecEntity>> collect = sysSensorRecEntities.stream()
                .collect(Collectors.groupingBy(SysSensorRecEntity::getTime, Collectors.toList()));
        Set<Date> dates = collect.keySet();

        List<SysSensorRecVo> sensorRecVos=new ArrayList<>();
        dates.stream().forEach(date -> {
            SysSensorRecVo sensorRecVo=new SysSensorRecVo();
            sensorRecVo.setTime(date);
            List<SysSensorRecEntity> sysSensorRecList = collect.get(date);
            sysSensorRecList.stream().forEach(sysSensorRecEntity -> {
                if(sysSensorRecEntity.getSensor().equals("humidity")){
                    sensorRecVo.setHumidity(sysSensorRecEntity.getValue()+"RH%");
                }
                if(sysSensorRecEntity.getSensor().equals("temperature")){
                    sensorRecVo.setTemperature(sysSensorRecEntity.getValue()+"℃");
                }
                if(sysSensorRecEntity.getSensor().equals("noisy")){
                    sensorRecVo.setNoisy(sysSensorRecEntity.getValue()+"dB");
                }
                if(sysSensorRecEntity.getSensor().equals("PM10")){
                    sensorRecVo.setPM10(sysSensorRecEntity.getValue()+"mg/m3");
                }
                if(sysSensorRecEntity.getSensor().equals("PM25")){
                    sensorRecVo.setPM25(sysSensorRecEntity.getValue()+"μg/m³");
                }
                if(sysSensorRecEntity.getSensor().equals("atmos")){
                    sensorRecVo.setAtmos(sysSensorRecEntity.getValue()+"Pa");
                }
            });
            sensorRecVos.add(sensorRecVo);
        });
        return sensorRecVos;
    }
}
