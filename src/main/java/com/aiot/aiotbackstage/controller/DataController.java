package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.model.dto.YunFeiData;
import com.aiot.aiotbackstage.server.schedule.DataStatisSchedule;
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
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    @PostMapping("pestNumStat")
    public Result sitesPestNumStat(@RequestBody Map<String, Object> params) {
        if (!params.containsKey("startDate") || !params.containsKey("endDate")) {
            return Result.error(ResultStatusCode.PARAM_NOT_COMPLETE);
        }
        Object startDate = params.get("startDate");
        Object endDate = params.get("endDate");
        if (!(startDate instanceof Long) || !(endDate instanceof Long)) {
            return Result.error(ResultStatusCode.PARAM_IS_INVALID);
        }
        return Result.success(sysInsectRecStatisService.getAllSitesPestNumStat((long) startDate, (long) endDate));
    }

    /**
     * 单站害虫统计及详情
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    @PostMapping("site/pestNumStat")
    public Result sitePestNumStat(@RequestBody Map<String, Object> params) {
        if (!params.containsKey("siteId") || !params.containsKey("startDate") || !params.containsKey("endDate")) {
            return Result.error(ResultStatusCode.PARAM_NOT_COMPLETE);
        }
        String siteId = String.valueOf(params.get("siteId"));
        Object startDate = params.get("startDate");
        Object endDate = params.get("endDate");
        if (!(startDate instanceof Long) || !(endDate instanceof Long)) {
            return Result.error(ResultStatusCode.PARAM_IS_INVALID);
        }
        return Result.success(sysInsectRecStatisService.getSomeSitePestNumStat(siteId, (long) startDate, (long) endDate));
    }

    /**
     * 单站土壤信息
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param isMax     是否最大值筛选：1、最大值，0：最小值，不传：不与虫害关联
     * @return
     */
    @PostMapping("site/soilInfo")
    public Result soilInfo(@RequestBody Map<String, Object> params) {
        if (!params.containsKey("siteId") || !params.containsKey("startDate") || !params.containsKey("endDate")) {
            return Result.error(ResultStatusCode.PARAM_NOT_COMPLETE);
        }
        String siteId = String.valueOf(params.get("siteId"));
        Object startDate = params.get("startDate");
        Object endDate = params.get("endDate");
        if (!(startDate instanceof Long) || !(endDate instanceof Long)) {
            return Result.error(ResultStatusCode.PARAM_IS_INVALID);
        }
        if (params.containsKey("isMax")) {
            if ((int) params.get("isMax") == 0 || (int) params.get("isMax") == 1) {
                return Result.success(sysDustRecStatisService.getMaxOrMinPestSoilInfo(siteId, (long) startDate, (long) endDate, (int) params.get("isMax")));
            } else {
                return Result.error(ResultStatusCode.PARAM_IS_INVALID);
            }
        } else {
            return Result.success(sysDustRecStatisService.getPestSoilInfo(siteId, (long) startDate, (long) endDate));
        }
    }

    /**
     * 单站气候信息
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param isMax     是否最大值筛选：1、最大值，0：最小值，不传：不与虫害关联
     * @return
     */
    @PostMapping("site/meteInfo")
    public Result meteorologicalInfo(@RequestBody Map<String, Object> params) {
        if (!params.containsKey("siteId") || !params.containsKey("startDate") || !params.containsKey("endDate")) {
            return Result.error(ResultStatusCode.PARAM_NOT_COMPLETE);
        }
        String siteId = String.valueOf(params.get("startDate"));
        Object startDate = params.get("startDate");
        Object endDate = params.get("endDate");
        if (!(startDate instanceof Long) || !(endDate instanceof Long)) {
            return Result.error(ResultStatusCode.PARAM_IS_INVALID);
        }
        if (params.containsKey("isMax")) {
            if ((int) params.get("isMax") == 0 || (int) params.get("isMax") == 1) {
                return Result.success(sensorRecStatisService.getMaxOrMinPestMeteInfo(siteId, (long) startDate, (long) endDate, (int) params.get("isMax")));
            } else {
                return Result.error(ResultStatusCode.PARAM_IS_INVALID);
            }
        } else {
            return Result.success(sensorRecStatisService.getPestMeteInfo(siteId, (long) startDate, (long) endDate));
        }
    }

    @PostMapping("statis")
    public Result statis() {
        dataStatisSchedule.manual();
        return Result.success();
    }
}


