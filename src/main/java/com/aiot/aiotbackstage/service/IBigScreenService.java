package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.vo.InsectStatisticsVo;

import java.util.Map;

public interface IBigScreenService {

    InsectStatisticsVo insectStatistics();

    Map<String,Object> dustRecStatistics();

    Object sensorRecStatistics();
}
