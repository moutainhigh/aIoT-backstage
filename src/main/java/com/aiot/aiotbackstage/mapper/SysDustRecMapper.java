package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysDustRecEntity;
import com.aiot.aiotbackstage.model.entity.SysDustRecStatisEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @author Avernus
 */
public interface SysDustRecMapper extends BaseMapper<SysDustRecEntity> {

    /**
     * 查找所有土壤信息每小时平均值
     *
     * @return
     * @param params
     */
    List<SysDustRecStatisEntity> findAllAverageHourly(Map<String, Object> params);
}
