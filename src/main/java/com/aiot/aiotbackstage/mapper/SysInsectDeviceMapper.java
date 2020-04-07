package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysInsectDeviceEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author Avernus
 */
public interface SysInsectDeviceMapper extends BaseMapper<SysInsectDeviceEntity> {

    List<SysInsectDeviceEntity> selectAll();
}
