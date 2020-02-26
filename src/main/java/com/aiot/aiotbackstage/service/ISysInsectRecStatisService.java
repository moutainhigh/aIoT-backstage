package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysInsectRecStatisEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface ISysInsectRecStatisService extends IService<SysInsectRecStatisEntity> {

    List<Map<String, Object>> getAllSitesPestNumStat(long startDate, long endDate);

    List<Map<String, Object>> getSomeSitePestNumStat(String siteId, long startDate, long endDate);
}
