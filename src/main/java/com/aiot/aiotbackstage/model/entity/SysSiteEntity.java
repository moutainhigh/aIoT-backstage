package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("sys_site")
public class SysSiteEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 村名
     */
    private String name;
    /**
     * 基座大小
      */
    private String size;
    /**
     * 腾讯地图地址url
     */
    private String locationUrl;

    /**
     * 基座大小
     */
    private String coordinate;
}
