package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 风速/风向/百叶箱传感器数值记录实体
 * @author Avernus
 */
@Data
@TableName("sys_sensor_rec")
@Accessors(chain = true)
public class SysSensorRecEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 站点id
     */
    private Integer siteId;
    /**
     * 指标类型
     * 先由地址码 {@link com.aiot.aiotbackstage.common.enums.RtuAddrCode} 确认硬件类型,
     * 再由字节位置确定指标类型 {@link com.aiot.aiotbackstage.common.enums.SensorType}
     */
    private String sensor;
    /**
     * 指标值
     */
    private String value;
    /**
     * 时间
     */
    private Date time;

    public SysSensorRecEntity(Integer siteId, String sensor, String value, Date time) {
        this.siteId = siteId;
        this.sensor = sensor;
        this.value = value;
        this.time = time;
    }
}
