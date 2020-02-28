package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 硬件故障记录表
 * @author Avernus
 */
@Data
@TableName(value = "sys_device_error_rec")
@Accessors(chain = true)
public class SysDeviceErrorRecEntity  {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer siteId;

    /**
     * {@link com.aiot.aiotbackstage.common.enums.DeviceType}
     */
    private String deviceType;

    private String subType;

    private Date startTime;

    private Date endTime;
}
