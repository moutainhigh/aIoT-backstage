package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 虫情测报灯实体
 * @author Avernus
 */
@Data
@TableName("sys_insect_device")
public class SysInsectDeviceEntity {

    private Integer id;
    /**
     * rtu站点id
     */
    private Integer siteId;
    /**
     * 设备序列号
     */
    private String imei;
}
