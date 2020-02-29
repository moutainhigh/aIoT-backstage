package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysDisasterSituationEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.vo.SysDisasterSituationVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysDisasterSituationMapper extends BaseMapper<SysDisasterSituationEntity> {

    List<SysDisasterSituationEntity> disasterSituationPage(@Param("page") PageParam page);

    /**
     * 查找时间段内灾情
     *
     * @param params
     * @return
     */
    int countAll(Map<String, Object> params);

    /**
     * 查找时间段内灾情
     *
     * @param params
     * @return
     */
    List<SysDisasterSituationVo> findAll(Map<String, Object> params);
}