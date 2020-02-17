package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.SysDisasterSituationMapper;
import com.aiot.aiotbackstage.mapper.SysHumidityTempMapper;
import com.aiot.aiotbackstage.mapper.SysMeteorologicalInfoMapper;
import com.aiot.aiotbackstage.mapper.SysSeedlingGrowthMapper;
import com.aiot.aiotbackstage.model.entity.SysDisasterSituationEntity;
import com.aiot.aiotbackstage.model.entity.SysHumidityTempEntity;
import com.aiot.aiotbackstage.model.entity.SysMeteorologicalInfoEntity;
import com.aiot.aiotbackstage.model.entity.SysSeedlingGrowthEntity;
import com.aiot.aiotbackstage.model.vo.FourQingVo;
import com.aiot.aiotbackstage.model.vo.SysHumidityRegionVo;
import com.aiot.aiotbackstage.model.vo.SysHumidityTempTimeVo;
import com.aiot.aiotbackstage.model.vo.SysTempRegionVo;
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

        //苗情
        List<SysSeedlingGrowthEntity> sysSeedlingGrowthEntities =
                seedlingGrowthMapper.selectList(Wrappers
                        .<SysSeedlingGrowthEntity>lambdaQuery()
                        .eq(SysSeedlingGrowthEntity::getStationId, stationId));
        if(CollectionUtils.isEmpty(sysSeedlingGrowthEntities)){
            qingVo.setSeedlingGrowth(null);
        }
        qingVo.setSeedlingGrowth(sysSeedlingGrowthEntities);

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
}
