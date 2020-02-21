package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.mapper.*;
import com.aiot.aiotbackstage.model.entity.*;
import com.aiot.aiotbackstage.model.vo.SensorInfoVo;
import com.aiot.aiotbackstage.service.IGisService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GisServiceImpl implements IGisService {

    @Autowired
    private SysSiteMapper siteMapper;

    @Autowired
    private SysDisasterSituationMapper disasterSituationMapper;

    @Autowired
    private SysSeedlingGrowthMapper seedlingGrowthMapper;

    @Autowired
    private SysSensorRecMapper sensorRecMapper;

    @Autowired
    private SysInsectRecMapper insectRecMapper;

    @Autowired
    private SysInsectDeviceMapper insectDeviceMapper;

    @Autowired
    private SysDustRecMapper dustRecMapper;

    @Override
    public List<SysSiteEntity> stationInfo() {

        List<SysSiteEntity> gisStationEntities = siteMapper.selectAll();
        if(CollectionUtils.isEmpty(gisStationEntities)){
            throw new MyException(ResultStatusCode.GIS_NO_EXIT);
        }
        return gisStationEntities;
    }

    @Override
    public void seedlingGrowth(JSONObject jsonParam) {
        String guid = (String)jsonParam.get("ID");
//        String siteId = (String)jsonParam.get("siteId");   TODO 保留
        Date date = (Date)jsonParam.get("date");
        BigDecimal totalArea = (BigDecimal)jsonParam.get("totalArea");
        BigDecimal good = (BigDecimal)jsonParam.get("good");
        BigDecimal normal = (BigDecimal)jsonParam.get("normal");
        BigDecimal subHealth = (BigDecimal)jsonParam.get("subHealth");
        BigDecimal unhealthy = (BigDecimal)jsonParam.get("unhealthy");
        SysSeedlingGrowthEntity seedlingGrowthEntity=new SysSeedlingGrowthEntity();
        seedlingGrowthEntity.setGuid(guid);
//        seedlingGrowthEntity.setSiteId(siteId);
        seedlingGrowthEntity.setDate(date);
        seedlingGrowthEntity.setTotalArea(totalArea);
        seedlingGrowthEntity.setGood(good);
        seedlingGrowthEntity.setNormal(normal);
        seedlingGrowthEntity.setSubHealth(subHealth);
        seedlingGrowthEntity.setUnhealthy(unhealthy);
        seedlingGrowthEntity.setCreateTime(new Date());
        seedlingGrowthMapper.insert(seedlingGrowthEntity);

    }

    @Override
    public void disasterSituation(JSONObject jsonParam) {
        String guid = (String)jsonParam.get("ID");
//        String siteId = (String)jsonParam.get("siteId"); TODO 保留
        Date date = (Date)jsonParam.get("date");
        BigDecimal totalArea = (BigDecimal)jsonParam.get("totalArea");
        BigDecimal serious = (BigDecimal)jsonParam.get("serious");
        BigDecimal medium = (BigDecimal)jsonParam.get("medium");
        BigDecimal normal = (BigDecimal)jsonParam.get("normal");
        SysDisasterSituationEntity disasterSituationEntity=new SysDisasterSituationEntity();
//        disasterSituationEntity.setSiteId(siteId);
        disasterSituationEntity.setGuid(guid);
        disasterSituationEntity.setDate(date);
        disasterSituationEntity.setTotalArea(totalArea);
        disasterSituationEntity.setSerious(serious);
        disasterSituationEntity.setMedium(medium);
        disasterSituationEntity.setNormal(normal);
        disasterSituationEntity.setCreateTime(new Date());
        disasterSituationMapper.insert(disasterSituationEntity);
    }

    @Override
    public SensorInfoVo sensorInfo(Long stationId) {

        //气象数据
        List<SysSensorRecEntity> sysSensorRecEntities =
                sensorRecMapper.selectList(Wrappers.<SysSensorRecEntity>lambdaQuery()
                .eq(SysSensorRecEntity::getSiteId, stationId));
        //虫情数据
        List<SysInsectDeviceEntity> sysInsectDeviceEntities =
                insectDeviceMapper.selectList(Wrappers.<SysInsectDeviceEntity>lambdaQuery()
                .eq(SysInsectDeviceEntity::getSiteId, stationId.intValue()));
        List<Integer> deviceIds = sysInsectDeviceEntities.stream().map(SysInsectDeviceEntity::getId).collect(Collectors.toList());
        List<SysInsectRecEntity> sysInsectRecEntities =
                insectRecMapper.selectBatchIds(deviceIds);
        //土壤数据
        List<SysDustRecEntity> sysDustRecEntities =
                dustRecMapper.selectList(Wrappers.<SysDustRecEntity>lambdaQuery()
                .eq(SysDustRecEntity::getSiteId, stationId));
        SensorInfoVo sensorInfoVo=new SensorInfoVo();
        sensorInfoVo.setDustRecVos(sysDustRecEntities);
        sensorInfoVo.setInsectRecVos(sysInsectRecEntities);
        sensorInfoVo.setSensorRecVos(sysSensorRecEntities);
        return sensorInfoVo;
    }
}
