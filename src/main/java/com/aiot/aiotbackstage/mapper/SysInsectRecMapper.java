package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysInsectRecEntity;
import com.aiot.aiotbackstage.model.vo.SysInsectInfoVo;
import com.aiot.aiotbackstage.model.vo.SysSiteVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface SysInsectRecMapper extends BaseMapper<SysInsectRecEntity> {

    /**
     * 查询所有站害虫数量统计
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    List<SysSiteVo> findSitesPestNumStat(Map<String, Object> params);

    /**
     * 查询单站害虫数量统计
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    List<SysInsectInfoVo> findPestNumStatBySiteId(Map<String, Object> params);

    /**
     * 按天统计害虫信息
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    List<Map<String, Object>> findPestStatByDay(Map<String, Object> params);
}
