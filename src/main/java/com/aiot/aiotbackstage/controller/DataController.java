package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.model.dto.YunFeiData;
import com.aiot.aiotbackstage.model.entity.SysInsectRecEntity;
import com.aiot.aiotbackstage.service.impl.SensorRecServiceImpl;
import com.aiot.aiotbackstage.service.impl.SysDustRecServiceImpl;
import com.aiot.aiotbackstage.service.impl.SysInsectRecServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author Avernus
 */
@Slf4j
@RestController
@RequestMapping(value = "/data")
public class DataController {

    @Autowired
    private SysInsectRecServiceImpl sysInsectRecService;

    @Autowired
    private SysDustRecServiceImpl sysDustRecService;

    @Autowired
    private SensorRecServiceImpl sensorRecService;

    @RequestMapping(value = "/insectDevice", method = RequestMethod.POST)
    public Result insectDevice(@RequestBody Map data) {
        log.error("time:{}, received:{}", System.currentTimeMillis(), data);
        return Result.success();
    }

    @RequestMapping(value = "/insects", method = RequestMethod.POST)
    public Result insects(@RequestBody SysInsectRecEntity data) {
        log.error("time:{}, received:{}", System.currentTimeMillis(), data.toString());
        sysInsectRecService.save(data);
        return Result.success();
    }

    /**
     * 全站害虫统计
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    @PostMapping("pestNumStat")
    public Result sitesPestNumStat(@RequestBody Map<String, Object> params) {
        if (!params.containsKey("startTime") || !params.containsKey("endTime")) {
            return Result.error(ResultStatusCode.PARAM_NOT_COMPLETE);
        }
        Object startTime = params.get("startTime");
        Object endTime = params.get("endTime");
        if (!(startTime instanceof Long) || !(endTime instanceof Long)) {
            return Result.error(ResultStatusCode.PARAM_IS_INVALID);
        }
        return Result.success(sysInsectRecService.getSitesPestNumStat((long) startTime, (long) endTime));
    }

    /**
     * 单站害虫统计及详情
     *
     * @param siteId    站点ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    @PostMapping("{siteId}/pestNumStat")
    public Result sitePestNumStat(@PathVariable("siteId") String siteId,
                                  @RequestBody Map<String, Object> params) {
        if (!params.containsKey("startTime") || !params.containsKey("endTime")) {
            return Result.error(ResultStatusCode.PARAM_NOT_COMPLETE);
        }
        Object startTime = params.get("startTime");
        Object endTime = params.get("endTime");
        if (!(startTime instanceof Long) || !(endTime instanceof Long)) {
            return Result.error(ResultStatusCode.PARAM_IS_INVALID);
        }
        return Result.success(sysInsectRecService.getPestNumStatBySiteId(siteId, (long) startTime, (long) endTime));
    }

    /**
     * 单站土壤信息
     *
     * @param siteId    站点ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param isMax     是否最大值筛选：1、最大值，其他：最小值
     * @return
     */
    @PostMapping("{siteId}/soilInfo")
    public Result soilInfo(@PathVariable String siteId,
                           @RequestBody Map<String, Object> params) {
        if (!params.containsKey("startTime") || !params.containsKey("endTime")) {
            return Result.error(ResultStatusCode.PARAM_NOT_COMPLETE);
        }
        Object startTime = params.get("startTime");
        Object endTime = params.get("endTime");
        if (!(startTime instanceof Long) || !(endTime instanceof Long)) {
            return Result.error(ResultStatusCode.PARAM_IS_INVALID);
        }
        if (params.containsKey("isMax") && (int) params.get("isMax") == 1) {
            return Result.success(sysDustRecService.getBiggestPestSoilInfo(siteId, (long) startTime, (long) endTime));
        } else {
            return Result.success(sysDustRecService.getMinimumPestSoilInfo(siteId, (long) startTime, (long) endTime));
        }
    }

    /**
     * 单站气候信息
     *
     * @param siteId    站点ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param isMax     是否最大值筛选：1、最大值，其他：最小值
     * @return
     */
    @PostMapping("{siteId}/meteInfo")
    public Result meteorologicalInfo(@PathVariable String siteId,
                                     @RequestBody Map<String, Object> params) {
        if (!params.containsKey("startTime") || !params.containsKey("endTime")) {
            return Result.error(ResultStatusCode.PARAM_NOT_COMPLETE);
        }
        Object startTime = params.get("startTime");
        Object endTime = params.get("endTime");
        if (!(startTime instanceof Long) || !(endTime instanceof Long)) {
            return Result.error(ResultStatusCode.PARAM_IS_INVALID);
        }
        if (params.containsKey("isMax") && (int) params.get("isMax") == 1) {
            return Result.success(sensorRecService.getBiggestPestMeteInfo(siteId, (long) startTime, (long) endTime));
        } else {
            return Result.success(sensorRecService.getMinimumPestMeteInfo(siteId, (long) startTime, (long) endTime));
        }
    }
}


