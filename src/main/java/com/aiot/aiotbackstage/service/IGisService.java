package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysDisasterSituationEntity;
import com.aiot.aiotbackstage.model.entity.SysSeedlingGrowthEntity;
import com.aiot.aiotbackstage.model.entity.SysSiteEntity;
import com.aiot.aiotbackstage.model.param.DisasterSituationGisParam;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.param.SeedlingGrowthGisParam;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.model.vo.SensorInfoVo;

import java.util.List;

public interface IGisService {

    List<SysSiteEntity> stationInfo();

    void seedlingGrowth(SeedlingGrowthGisParam param);

    void disasterSituation(DisasterSituationGisParam param);

    SensorInfoVo sensorInfo(Integer stationId);

    PageResult<SysSeedlingGrowthEntity> getSeedlingGrowth(PageParam pageParam);

    PageResult<SysDisasterSituationEntity> getDisasterSituation(PageParam pageParam);

    List<SysSiteEntity> stationData();
}
