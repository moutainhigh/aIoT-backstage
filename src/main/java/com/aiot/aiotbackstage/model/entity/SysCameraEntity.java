package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Avernus
 */
@Data
@TableName("sys_camera")
@Accessors(chain = true)
public class SysCameraEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 对应站点id
     */
    private Integer siteId;

    /**
     * 摄像机类型 枪机，球机，人流量摄像头
     * {@link com.aiot.aiotbackstage.common.enums.CameraType}
     */
    private String type;

    private String name;

    /**
     * 设备序列号
     */
    private String imei;
}
