package com.aiot.aiotbackstage.model.vo;

import com.aiot.aiotbackstage.model.entity.SysDisasterSituationEntity;
import com.aiot.aiotbackstage.model.entity.SysMeteorologicalInfoEntity;
import com.aiot.aiotbackstage.model.entity.SysSeedlingGrowthEntity;
import lombok.Data;

import java.util.List;

/**
 * 四情数据
 */
@Data
public class FourQingVo {

    /**
     * 站点Id
     */
    private Long stationId;

    /**
     * 灾情
     */
    private List<SysDisasterSituationEntity> disasterSituation;

    /**
     * 气象信息
     */
    private List<SysMeteorologicalInfoEntity> meteorologicalInfo;

    /**
     *苗情信息
     */
    private List<SysSeedlingGrowthEntity> seedlingGrowth;

    /**
     * 土壤温度
     */
    private List<SysTempRegionVo> tempRegionVos;

    /**
     * 土壤湿度
     */
    private List<SysHumidityRegionVo> humidityRegionVos;
}
