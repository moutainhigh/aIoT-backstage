package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysDustRecEntity;
import com.aiot.aiotbackstage.model.vo.SysDustRecVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @author Avernus
 */
public interface SysDustRecMapper extends BaseMapper<SysDustRecEntity> {

    /**
     * 根据深度查找所有土壤信息平均值
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    List<SysDustRecVo> findAllDepthAverageByDay(Map<String, Object> params);
}
