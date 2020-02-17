package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.vo.FourQingVo;
import com.aiot.aiotbackstage.model.vo.SysSensorRecVo;

import java.util.List;

public interface IFourQingService {

    FourQingVo monitorInfo(Long stationId);

    List<SysSensorRecVo> meteorological(Long stationId);
}
