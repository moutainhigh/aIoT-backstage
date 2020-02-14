package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

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
}
