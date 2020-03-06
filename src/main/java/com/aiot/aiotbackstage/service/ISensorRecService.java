package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.dto.RtuData;
import com.aiot.aiotbackstage.model.entity.SysSensorRecEntity;
import com.aiot.aiotbackstage.model.param.InsectRecByDateParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author Avernus
 */
public interface ISensorRecService extends IService<SysSensorRecEntity> {

    /**
     * 接收rtu数据
     * @param rtuData
     */
    void receive(RtuData rtuData);

    Map<String, Object> current(Integer siteId);

    Object getStatByTime(String time);

     Map<String,Object> insectRecByDate(InsectRecByDateParam param);
}

