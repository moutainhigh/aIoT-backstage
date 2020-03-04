package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.vo.InsectStatisticsVo;

public interface IBigScreenService {

    InsectStatisticsVo insectStatistics();

    Object dustRecStatistics();

    Object sensorRecStatistics();
}
