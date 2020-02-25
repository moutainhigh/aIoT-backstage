package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.model.dto.YunFeiData;
import com.aiot.aiotbackstage.server.schedule.DataStatisSchedule;
import com.aiot.aiotbackstage.service.impl.SensorRecServiceImpl;
import com.aiot.aiotbackstage.service.impl.SensorRecStatisServiceImpl;
import com.aiot.aiotbackstage.service.impl.SysDustRecStatisServiceImpl;
import com.aiot.aiotbackstage.service.impl.SysInsectRecStatisServiceImpl;
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
    private SysInsectRecStatisServiceImpl sysInsectRecStatisService;

    @Autowired
    private SysDustRecStatisServiceImpl sysDustRecStatisService;

    @Autowired
    private SensorRecStatisServiceImpl sensorRecStatisService;

    @Autowired
    private DataStatisSchedule dataStatisSchedule;

    @RequestMapping(value = "/insectDevice", method = RequestMethod.POST)
    public Result insectDevice(@RequestBody Map data) {
        log.error("time:{}, received:{}", System.currentTimeMillis(), data);
        return Result.success();
    }

    @RequestMapping(value = "/insects", method = RequestMethod.POST)
    public Result insects(@RequestBody YunFeiData data) {
        log.error("time:{}, received:{}", System.currentTimeMillis(), data.toString());
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
        return Result.success(sysInsectRecStatisService.getAllSitesPestNumStat((long) startTime, (long) endTime));
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
        return Result.success(sysInsectRecStatisService.getSomeSitePestNumStat(siteId, (long) startTime, (long) endTime));
    }

    /**
     * 单站土壤信息
     *
     * @param siteId    站点ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param isMax     是否最大值筛选：1、最大值，0：最小值，不传：不与虫害关联
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
        if (params.containsKey("isMax")) {
            if ((int) params.get("isMax") == 0 || (int) params.get("isMax") == 1) {
                return Result.success(sysDustRecStatisService.getMaxOrMinPestSoilInfo(siteId, (long) startTime, (long) endTime, (int) params.get("isMax")));
            } else {
                return Result.error(ResultStatusCode.PARAM_IS_INVALID);
            }
        } else {
            return Result.success(sysDustRecStatisService.getPestSoilInfo(siteId, (long) startTime, (long) endTime));
        }
    }

    /**
     * 单站气候信息
     *
     * @param siteId    站点ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param isMax     是否最大值筛选：1、最大值，0：最小值，不传：不与虫害关联
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
        if (params.containsKey("isMax")) {
            if ((int) params.get("isMax") == 0 || (int) params.get("isMax") == 1) {
                return Result.success(sensorRecStatisService.getMaxOrMinPestMeteInfo(siteId, (long) startTime, (long) endTime, (int) params.get("isMax")));
            } else {
                return Result.error(ResultStatusCode.PARAM_IS_INVALID);
            }
        } else {
            return Result.success(sensorRecStatisService.getPestMeteInfo(siteId, (long) startTime, (long) endTime));
        }
    }

    @PostMapping("statis")
    public Result statis() {
        dataStatisSchedule.start();
        return Result.success();
    }
}


