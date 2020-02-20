package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.vo.SysSensorRecVo;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface IFourQingService {

    List<SysSensorRecVo> meteorological(Long stationId);

    void seedlingGrowth(JSONObject jsonParam);

    void disasterSituation(JSONObject jsonParam);
}
