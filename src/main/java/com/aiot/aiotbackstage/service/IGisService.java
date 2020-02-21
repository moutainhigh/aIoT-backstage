package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysSiteEntity;
import com.aiot.aiotbackstage.model.vo.SensorInfoVo;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface IGisService {

    List<SysSiteEntity> stationInfo();

    void seedlingGrowth(JSONObject jsonParam);

    void disasterSituation(JSONObject jsonParam);

    SensorInfoVo sensorInfo(Long stationId);
}
