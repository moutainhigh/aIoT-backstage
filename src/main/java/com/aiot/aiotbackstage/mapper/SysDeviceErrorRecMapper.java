package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysDeviceErrorRecEntity;
import com.aiot.aiotbackstage.model.param.DeviceInfoNewParam;
import com.aiot.aiotbackstage.model.param.DeviceInfoOldParam;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author Avernus
 */
public interface SysDeviceErrorRecMapper extends BaseMapper<SysDeviceErrorRecEntity> {

    List<SysDeviceErrorRecEntity> deviceErrorRecOldPage(@Param("param") DeviceInfoOldParam param);

    List<SysDeviceErrorRecEntity> deviceErrorRecNewYearPage(@Param("param") DeviceInfoNewParam param);

    List<SysDeviceErrorRecEntity> deviceErrorRecNewYuePage(@Param("param") DeviceInfoNewParam param);

    Integer countYear(@Param("param") DeviceInfoNewParam param);

    Integer countYue(@Param("param") DeviceInfoNewParam param);

    Integer countOld(@Param("param") DeviceInfoOldParam param);

}
