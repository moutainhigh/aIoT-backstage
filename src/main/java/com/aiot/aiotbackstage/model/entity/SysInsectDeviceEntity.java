package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 虫情测报灯实体
 * @author Avernus
 */
@Data
@TableName("sys_insect_device")
@Accessors(chain = true)
public class SysInsectDeviceEntity {

    @TableId(type = IdType.AUTO)
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
