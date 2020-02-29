package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 土壤墒情记录实体
 * @author Avernus
 */
@Data
@TableName("sys_dust_rec")
@Accessors(chain = true)
public class SysDustRecEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 站点id
     */
    private Integer siteId;
    /**
     * 深度 {@link com.aiot.aiotbackstage.common.enums.RtuAddrCode}
     */
    private Integer depth;
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
    /**
     * 时间
     */
    private Date time;

//    public SysDustRecEntity(Integer siteId, Integer depth, int[] datum, Date time) {
//        this.siteId = siteId;
//        this.depth = depth;
//        this.wc = Double.valueOf(datum[0]) / 100;
//        this.temperature = Double.valueOf(datum[1]) / 100;
//        this.ec = Double.valueOf(datum[2]);
//        this.salinity = Double.valueOf(datum[3]);
//        this.tds = Double.valueOf(datum[4]);
//        this.epsilon = Double.valueOf(datum[5]) / 100;
//        this.time = time;
//    }
}
