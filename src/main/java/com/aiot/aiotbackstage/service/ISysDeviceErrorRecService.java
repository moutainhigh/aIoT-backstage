package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysDeviceErrorRecEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Avernus
 */
public interface ISysDeviceErrorRecService extends IService<SysDeviceErrorRecEntity> {

    /**
     * 硬件失去链接，新增记录
     * @param siteId
     * @param deviceType
     * @param subType
     */
    void newRec(Integer siteId, String deviceType, String subType);

    /**
     * 硬件上线，刷新记录
     * @param siteId
     * @param deviceType
     * @param subType
     */
    void refreshRec(Integer siteId, String deviceType, String subType);
}
