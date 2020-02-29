package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysDeviceErrorRecEntity;
import com.aiot.aiotbackstage.model.param.DeviceInfoNewParam;
import com.aiot.aiotbackstage.model.param.DeviceInfoOldParam;
import com.aiot.aiotbackstage.model.param.DeviceInfoParam;
import com.aiot.aiotbackstage.model.vo.PageResult;

import java.util.Date;

public interface IDeviceService {

    PageResult<SysDeviceErrorRecEntity> deviceInfoNew(DeviceInfoNewParam param);

    PageResult<SysDeviceErrorRecEntity> deviceInfoOld(DeviceInfoOldParam param);

    void deviceInfoNewModify(DeviceInfoParam param);

    void deviceInfoOldModify(DeviceInfoParam param);

    void deviceInfoOldDel(Integer id);
}
