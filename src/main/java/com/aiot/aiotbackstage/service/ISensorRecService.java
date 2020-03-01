package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.dto.RtuData;
import com.aiot.aiotbackstage.model.entity.SysSensorRecEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Avernus
 */
public interface ISensorRecService extends IService<SysSensorRecEntity> {

    /**
     * 接收rtu数据
     * @param rtuData
     */
    void receive(RtuData rtuData);

    Object getStatByTime(String time);
}

