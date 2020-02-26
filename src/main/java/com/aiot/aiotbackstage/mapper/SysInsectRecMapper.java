package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysInsectRecEntity;
import com.aiot.aiotbackstage.model.entity.SysInsectRecStatisEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysInsectRecMapper extends BaseMapper<SysInsectRecEntity> {

    /**
     * 查询所有站害虫数量统计
     *
     * @return
     */
    List<SysInsectRecStatisEntity> findAllPestNumHourly(Map<String, Object> params);
}
