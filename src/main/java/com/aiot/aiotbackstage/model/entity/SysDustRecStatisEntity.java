package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@TableName("sys_dust_rec_statis")
@Accessors(chain = true)
public class SysDustRecStatisEntity {

    private Integer id;
    /**
     * 站点id
     */
    private String siteId;

    /**
     * 深度 {@link com.aiot.aiotbackstage.common.enums.RtuAddrCode}
     */
    private Integer depth;

    private String date;

    private Integer hour;

    /**
     * 含水率 %
     */
    private Double wc;

    /**
     * 温度
     * 单位 摄氏度 ℃
     */
    private Double temperature;

    /**
     * 导电率
     * 单位 us/cm
     */
    private Double ec;

    /**
     * 盐度
     * 单位 mg/L
     */
    private Double salinity;

    /**
     * 总溶解固体
     * 单位 mg/L
     */
    private Double tds;

    /**
     * 介电常数
     */
    private Double epsilon;

    private String updateTime;

    private String createTime;
}
