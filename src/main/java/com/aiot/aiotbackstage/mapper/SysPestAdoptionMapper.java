package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysPestAdoptionEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysPestAdoptionMapper extends BaseMapper<SysPestAdoptionEntity> {

    List<SysPestAdoptionEntity> pestAdoptionPage(@Param("param") PageParam param);

}