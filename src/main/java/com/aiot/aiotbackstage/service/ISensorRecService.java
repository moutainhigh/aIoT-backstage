package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.dto.RtuData;
import com.aiot.aiotbackstage.model.entity.SysSensorRecEntity;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ISensorRecService extends IService<SysSensorRecEntity> {

    void receive(RtuData rtuData);

}
