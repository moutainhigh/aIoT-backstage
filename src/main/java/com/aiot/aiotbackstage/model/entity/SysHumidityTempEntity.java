package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "sys_humidity_temp")
public class SysHumidityTempEntity implements Serializable {
    /**
     * 温度id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 类型(1-温度，2-湿度)
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 站点id
     */
    @TableField(value = "station_id")
    private Long stationId;

    /**
     * 区域
     */
    @TableField(value = "region")
    private String region;

    /**
     * 时间
     */
    @TableField(value = "time")
    private String time;

    /**
     * 温度值或者湿度值
     */
    @TableField(value = "var")
    private BigDecimal var;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}