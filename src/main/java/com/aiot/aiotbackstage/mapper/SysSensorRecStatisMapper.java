package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysSensorRecStatisEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysSensorRecStatisMapper extends BaseMapper<SysSensorRecStatisEntity> {

    /**
     * 批量插入
     *
     * @param data
     * @return
     */
    int batchInsert(@Param("data") List<SysSensorRecStatisEntity> data);

    /**
     * 通过站点ID查询每天平均值
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    List<SysSensorRecStatisEntity> findAllBySiteId(Map<String, Object> params);
}
