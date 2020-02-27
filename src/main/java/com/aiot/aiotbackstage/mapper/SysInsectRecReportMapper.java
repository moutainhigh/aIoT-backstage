package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysInsectRecReportEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysInsectRecReportMapper extends BaseMapper<SysInsectRecReportEntity> {


    List<SysInsectRecReportEntity> insectRecReportInfo(@Param("whetherExamine") Integer whetherExamine, @Param("page") PageParam page);

    Integer insectRecReportCount(@Param("whetherExamine") Integer whetherExamine);
}