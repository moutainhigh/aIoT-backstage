package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysInsectRecStatisEntity;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface ISysInsectRecStatisService extends IService<SysInsectRecStatisEntity> {

    List<Map<String, Object>> getAllSitesPestNumStat(String startDate, String endDate);

    PageResult<Map<String, Object>> getSomeSitePestNumStat(String siteId, String startDate, String endDate, int pageSize, int pageIndex,int i);

    Map<String, List<String>> getPestNumStat(String startDate, String endDate);

    Map<String, List<String>> getPerPestNumStat();

    Map<String, List<String>> getPestNumStatDaily(String startDate, String endDate);
}
