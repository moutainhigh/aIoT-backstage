package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysInsectRecEntity;
import com.aiot.aiotbackstage.model.vo.SysInsectInfoVo;
import com.aiot.aiotbackstage.model.vo.SysSiteVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Avernus
 */
public interface ISysInsectRecService extends IService<SysInsectRecEntity> {

    /**
     * 获取全站害虫统计信息
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<SysSiteVo> getSitesPestNumStat(long startTime, long endTime);

    /**
     * 获取单站害虫统计及详细
     *
     * @param siteId
     * @param startTime
     * @param endTime
     * @return
     */
    List<SysInsectInfoVo> getPestNumStatBySiteId(String siteId, long startTime, long endTime);
}
