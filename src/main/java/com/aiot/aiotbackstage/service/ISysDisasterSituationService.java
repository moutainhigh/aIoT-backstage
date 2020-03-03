package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysDisasterSituationEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface ISysDisasterSituationService extends IService<SysDisasterSituationEntity> {


    Map<String, Object> getStat(String startDate, String endDate);
}
