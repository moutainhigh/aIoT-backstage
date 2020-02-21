package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysInsectRecEntity;
import com.aiot.aiotbackstage.model.vo.SysDustRecVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Avernus
 */
public interface ISysDustRecService extends IService<SysInsectRecEntity> {

    List<SysDustRecVo> getBiggestPestSoilInfo(String siteId, long startTime, long endTime);

    List<SysDustRecVo> getMinimumPestSoilInfo(String siteId, long startTime, long endTime);
}
