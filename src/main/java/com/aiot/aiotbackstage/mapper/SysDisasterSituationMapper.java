package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysDisasterSituationEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysDisasterSituationMapper extends BaseMapper<SysDisasterSituationEntity> {

    List<SysDisasterSituationEntity> disasterSituationPage(@Param("page") PageParam page);

}
