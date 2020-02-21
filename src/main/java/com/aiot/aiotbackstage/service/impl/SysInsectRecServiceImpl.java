package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.SysInsectRecMapper;
import com.aiot.aiotbackstage.model.entity.SysInsectRecEntity;
import com.aiot.aiotbackstage.model.vo.SysInsectInfoVo;
import com.aiot.aiotbackstage.model.vo.SysSiteVo;
import com.aiot.aiotbackstage.service.ISysInsectRecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.http.impl.cookie.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Avernus
 */
@Service
public class SysInsectRecServiceImpl extends ServiceImpl<SysInsectRecMapper, SysInsectRecEntity> implements ISysInsectRecService {

    @Autowired
    private SysInsectRecMapper sysInsectRecMapper;

    @Override
    public List<SysSiteVo> getSitesPestNumStat(long startTime, long endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", DateUtils.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss"));
        params.put("endTime", DateUtils.formatDate(new Date(endTime), "yyyy-MM-dd HH:mm:ss"));
        return sysInsectRecMapper.findSitesPestNumStat(params);
    }

    @Override
    public List<SysInsectInfoVo> getPestNumStatBySiteId(String siteId, long startTime, long endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startTime", DateUtils.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss"));
        params.put("endTime", DateUtils.formatDate(new Date(endTime), "yyyy-MM-dd HH:mm:ss"));
        return sysInsectRecMapper.findPestNumStatBySiteId(params);
    }
}
