package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysSensorRecStatisEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ISensorRecStatisService extends IService<SysSensorRecStatisEntity> {

    List<SysSensorRecStatisEntity> getPestMeteInfo(String siteId, long startDate, long endDate);

    List<SysSensorRecStatisEntity> getMaxOrMinPestMeteInfo(String siteId, long startDate, long endDate, int isMax);
}
