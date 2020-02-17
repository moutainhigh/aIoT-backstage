package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "sys_disaster_situation")
public class SysDisasterSituationEntity implements Serializable {
    /**
     * 灾情id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 站点id
     */
    @TableField(value = "station_id")
    private Long stationId;

    /**
     * 灾情视频
     */
    @TableField(value = "disaster_video")
    private String disasterVideo;

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