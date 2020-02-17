package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "sys_seedling_growth")
public class SysSeedlingGrowthEntity implements Serializable {
    /**
     * 苗情id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 站点id
     */
    @TableField(value = "station_id")
    private Long stationId;

    /**
     * 苗情图片
     */
    @TableField(value = "station_img")
    private String stationImg;

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