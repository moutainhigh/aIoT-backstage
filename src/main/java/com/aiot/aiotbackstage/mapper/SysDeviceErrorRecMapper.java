package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysDeviceErrorRecEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author Avernus
 */
public interface SysDeviceErrorRecMapper extends BaseMapper<SysDeviceErrorRecEntity> {

    List<SysDeviceErrorRecEntity> deviceErrorRecOldPage(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("pageParam") PageParam pageParam);

    List<SysDeviceErrorRecEntity> deviceErrorRecNewYearPage(@Param("pageParam") PageParam pageParam);

    List<SysDeviceErrorRecEntity> deviceErrorRecNewYuePage(@Param("pageParam") PageParam pageParam);

    Integer countYear();

    Integer countYue();

    Integer countOld(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
