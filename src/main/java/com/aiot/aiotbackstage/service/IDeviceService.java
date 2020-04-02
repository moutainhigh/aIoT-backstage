package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysDeviceErrorRecEntity;
import com.aiot.aiotbackstage.model.param.DeviceInfoOldParam;
import com.aiot.aiotbackstage.model.param.DeviceInfoParam;
import com.aiot.aiotbackstage.model.vo.DeviceResultVo;
import com.aiot.aiotbackstage.model.vo.PageResult;

import java.util.List;
import java.util.Map;

public interface IDeviceService {

    List<DeviceResultVo> deviceInfoNew();

    PageResult<SysDeviceErrorRecEntity> deviceInfoOld(DeviceInfoOldParam param);

    void deviceInfoNewModify(DeviceInfoParam param);

    void deviceInfoOldModify(DeviceInfoParam param);

    void deviceInfoOldDel(Integer id);

    List<Map<String, Object>> rtuStatus();
}
