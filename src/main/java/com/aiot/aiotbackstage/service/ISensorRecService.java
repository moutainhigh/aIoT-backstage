package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.dto.RtuData;
import com.aiot.aiotbackstage.model.entity.SysSensorRecEntity;
import com.aiot.aiotbackstage.model.vo.SysSensorRecVo2;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Avernus
 */
public interface ISensorRecService extends IService<SysSensorRecEntity> {

    /**
     * 接收rtu数据
     *
     * @param rtuData
     */
    void receive(RtuData rtuData);

    /**
     * 获取害虫最大时气候信息
     *
     * @param siteId    站点ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    SysSensorRecVo2 getBiggestPestMeteInfo(String siteId, long startTime, long endTime);

    /**
     * 获取害虫最小时气候信息
     *
     * @param siteId    站点ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    SysSensorRecVo2 getMinimumPestMeteInfo(String siteId, long startTime, long endTime);
}
