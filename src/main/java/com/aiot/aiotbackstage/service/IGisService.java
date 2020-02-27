package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysSiteEntity;
import com.aiot.aiotbackstage.model.param.DisasterSituationGisParam;
import com.aiot.aiotbackstage.model.param.SeedlingGrowthGisParam;
import com.aiot.aiotbackstage.model.vo.SensorInfoVo;

import java.util.List;

public interface IGisService {

    List<SysSiteEntity> stationInfo();

    void seedlingGrowth(SeedlingGrowthGisParam param);

    void disasterSituation(DisasterSituationGisParam param);

    SensorInfoVo sensorInfo(Long stationId);
}
