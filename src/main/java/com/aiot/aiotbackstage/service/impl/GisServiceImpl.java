package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.enums.RtuAddrCode;
import com.aiot.aiotbackstage.common.enums.SensorType;
import com.aiot.aiotbackstage.common.exception.MyException;
import com.aiot.aiotbackstage.mapper.*;
import com.aiot.aiotbackstage.model.entity.*;
import com.aiot.aiotbackstage.model.param.DisasterSituationGisParam;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.param.SeedlingGrowthGisParam;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.model.vo.SensorInfoVo;
import com.aiot.aiotbackstage.service.IEarlyWarningService;
import com.aiot.aiotbackstage.service.IGisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class GisServiceImpl implements IGisService {

    @Autowired
    private SysSiteMapper siteMapper;

    @Autowired
    private SysDisasterSituationMapper disasterSituationMapper;

    @Autowired
    private SysSeedlingGrowthMapper seedlingGrowthMapper;

    @Autowired
    private SysInsectRecMapper insectRecMapper;

    @Autowired
    private SysInsectInfoMapper insectInfoMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private IEarlyWarningService earlyWarningService;


    @Override
    public List<SysSiteEntity> stationInfo() {

        List<SysSiteEntity> gisStationEntities = siteMapper.selectList(null);
        if(CollectionUtils.isEmpty(gisStationEntities)){
            throw new MyException(ResultStatusCode.GIS_NO_EXIT);
        }
        gisStationEntities.forEach(sysSiteEntity -> {
            SensorInfoVo sensorInfoVo = sensorInfo(sysSiteEntity.getId());
            sysSiteEntity.setInfoVo(sensorInfoVo);
        });
        return gisStationEntities;
    }

    @Override
    public void seedlingGrowth(SeedlingGrowthGisParam param) {
        SysSeedlingGrowthEntity seedlingGrowthEntity=new SysSeedlingGrowthEntity();
        seedlingGrowthEntity.setGuid(param.getID());
//        seedlingGrowthEntity.setSiteId(siteId);
        seedlingGrowthEntity.setDate(param.getDate());
        seedlingGrowthEntity.setUrl(param.getImage());
        seedlingGrowthEntity.setTotalArea(param.getTotalArea());
        seedlingGrowthEntity.setGood(param.getGood());
        seedlingGrowthEntity.setNormal(param.getNormal());
        seedlingGrowthEntity.setSubHealth(param.getSubHealth());
        seedlingGrowthEntity.setUnhealthy(param.getUnhealthy());
        seedlingGrowthEntity.setCreateTime(new Date());
        seedlingGrowthMapper.insert(seedlingGrowthEntity);
        try {
            earlyWarningService.earlyWarningReport("苗情","seedlingGrowth",null,param.getUnhealthy().toString(),null);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void disasterSituation(DisasterSituationGisParam param) {
        SysDisasterSituationEntity disasterSituationEntity=new SysDisasterSituationEntity();
//        disasterSituationEntity.setSiteId(siteId);
        disasterSituationEntity.setGuid(param.getID());
        disasterSituationEntity.setUrl(param.getImage());
        disasterSituationEntity.setDate(param.getDate());
        disasterSituationEntity.setTotalArea(param.getTotalArea());
        disasterSituationEntity.setSerious(param.getSerious());
        disasterSituationEntity.setMedium(param.getMedium());
        disasterSituationEntity.setNormal(param.getNormal());
        disasterSituationEntity.setCreateTime(new Date());
        disasterSituationMapper.insert(disasterSituationEntity);
        try {
            earlyWarningService.earlyWarningReport("灾情","disasterSituation",null,param.getSerious().toString(),null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public SensorInfoVo sensorInfo(Integer stationId) {

        List<Map<String,String>> sensorRecVos=new ArrayList<>();
        List<Map<String,String>> dustRecVos=new ArrayList<>();
        //气象数据
        Arrays.stream(SensorType.values()).forEach(sensorType -> {
            Map<String,String> map=new HashMap<>();
            map.put(sensorType.name(),(String)redisTemplate.opsForValue().get("SENSOR-VALUE:" + stationId + ":"+sensorType.name()));
            sensorRecVos.add(map);
        });

        //虫情数据
        List<SysInsectRecEntity> sysInsectRecEntities = insectRecMapper.insectRecGisInfo(stationId);
        sysInsectRecEntities.forEach(sysInsectRecEntity -> {
            List<Map<String,Object>> maps=new ArrayList<>();
            String result = sysInsectRecEntity.getResult();
            String[] split = result.split("#");
            Arrays.stream(split).forEach(s -> {
                String pestId = s.substring(0, s.indexOf(","));
                SysInsectInfoEntity sysInsectInfoEntity = insectInfoMapper.selectById(pestId);
                String count = s.substring(s.indexOf(",")+1,s.length());
                Map<String,Object> map=new HashMap<>();
                map.put("pestName",sysInsectInfoEntity.getName());
                map.put("count",count);
                maps.add(map);
            });
            sysInsectRecEntity.setMaps(maps);
        });

        //土壤数据
        Arrays.stream(RtuAddrCode.values()).forEach(rtuAddrCode -> {
            Map<String,String> map=new HashMap<>();
            map.put(rtuAddrCode.name(),(String)redisTemplate.opsForValue().get("SENSOR-VALUE:" + stationId + ":"+rtuAddrCode.name()));
            dustRecVos.add(map);
        });

        SensorInfoVo infoVo=new SensorInfoVo();
        infoVo.setSensorRecVos(sensorRecVos);
        infoVo.setInsectRecVos(sysInsectRecEntities);
        infoVo.setDustRecVos(dustRecVos);
        return infoVo;
    }

    @Override
    public PageResult<SysSeedlingGrowthEntity> getSeedlingGrowth(PageParam pageParam) {
        List<SysSeedlingGrowthEntity> sysSeedlingGrowthEntities = seedlingGrowthMapper.seedingGrowthPage(pageParam);
        Integer total = seedlingGrowthMapper.selectCount(null);

        if(CollectionUtils.isEmpty(sysSeedlingGrowthEntities)){
            throw new MyException(ResultStatusCode.NO_RESULT);
        }

        return PageResult.<SysSeedlingGrowthEntity>builder()
                .total(total)
                .pageData(sysSeedlingGrowthEntities)
                .pageNumber(pageParam.getPageNumber())
                .pageSize(pageParam.getPageSize())
                .build();
    }

    @Override
    public PageResult<SysDisasterSituationEntity> getDisasterSituation(PageParam pageParam) {
        List<SysDisasterSituationEntity> disasterSituationEntities = disasterSituationMapper.disasterSituationPage(pageParam);
        Integer total = disasterSituationMapper.selectCount(null);

        if(CollectionUtils.isEmpty(disasterSituationEntities)){
            throw new MyException(ResultStatusCode.NO_RESULT);
        }

        return PageResult.<SysDisasterSituationEntity>builder()
                .total(total)
                .pageData(disasterSituationEntities)
                .pageNumber(pageParam.getPageNumber())
                .pageSize(pageParam.getPageSize())
                .build();
    }

    @Override
    public List<SysSiteEntity> stationData() {
        List<SysSiteEntity> gisStationEntities = siteMapper.selectList(null);
        if(CollectionUtils.isEmpty(gisStationEntities)){
            throw new MyException(ResultStatusCode.GIS_NO_EXIT);
        }
        return gisStationEntities;
    }
}
