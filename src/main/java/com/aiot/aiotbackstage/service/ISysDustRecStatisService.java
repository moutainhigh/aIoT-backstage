package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysDustRecStatisEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ISysDustRecStatisService extends IService<SysDustRecStatisEntity> {

    List<SysDustRecStatisEntity> getPestSoilInfo(String siteId, long startTime, long endTime);

    List<SysDustRecStatisEntity> getMaxOrMinPestSoilInfo(String siteId, long startTime, long endTime, int isMax);
}
