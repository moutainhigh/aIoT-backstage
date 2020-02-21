package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.vo.SysSensorRecVo;

import java.util.List;

public interface IFourQingService {

    List<SysSensorRecVo> meteorological(Long stationId);

}
