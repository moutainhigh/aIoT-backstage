package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysSensorRecStatisEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ISensorRecStatisService extends IService<SysSensorRecStatisEntity> {

    List<SysSensorRecStatisEntity> getPestMeteInfo(String siteId, long startTime, long endTime);

    List<SysSensorRecStatisEntity> getMaxOrMinPestMeteInfo(String siteId, long startTime, long endTime, int isMax);
}
