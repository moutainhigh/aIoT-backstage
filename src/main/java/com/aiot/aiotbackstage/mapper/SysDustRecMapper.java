package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysDustRecEntity;
import com.aiot.aiotbackstage.model.entity.SysDustRecStatisEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author Avernus
 */
public interface SysDustRecMapper extends BaseMapper<SysDustRecEntity> {

    /**
     * 查找所有土壤信息每小时平均值
     *
     * @param params
     * @return
     */
    List<SysDustRecStatisEntity> findAllAverageHourly(Map<String, Object> params);

    List<SysDustRecEntity> findByTimeGroupByDepth(@Param("siteId") String siteId, @Param("time") String time);
}
