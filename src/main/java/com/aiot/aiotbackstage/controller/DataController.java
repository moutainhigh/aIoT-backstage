package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Constants;
import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.util.DateUtils;
import com.aiot.aiotbackstage.model.dto.YunFeiData;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.server.schedule.DataStatisSchedule;
import com.aiot.aiotbackstage.service.impl.SensorRecStatisServiceImpl;
import com.aiot.aiotbackstage.service.impl.SysDustRecStatisServiceImpl;
import com.aiot.aiotbackstage.service.impl.SysInsectRecServiceImpl;
import com.aiot.aiotbackstage.service.impl.SysInsectRecStatisServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
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
        sysInsectRecService.save(data);
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
        String startDate;
        String endDate;
        startDate = String.valueOf(params.get("startDate"));
        endDate = String.valueOf(params.get("endDate"));
        if (startDate.isEmpty()
                || endDate.isEmpty()
                || "null".equals(startDate)
                || "null".equals(endDate)) {
            startDate = DateUtils.format(new Date(), "yyyy-MM-dd");
            endDate = startDate;
        } else {
            if (!DateUtils.isValid(startDate, "yyyy-MM-dd")
                    || !DateUtils.isValid(endDate, "yyyy-MM-dd")) {
                return Result.error(ResultStatusCode.PARAM_IS_INVALID);
            }
        }
        return Result.success(sysInsectRecStatisService.getAllSitesPestNumStat(startDate, endDate));
    }

    /**
     * 单站害虫统计及详情
     *
     * @param siteId       站点ID
     * @param startDate    开始日期
     * @param endDate      结束日期
     * @param isNearlyWeek 是否是最近一周：1、是；其他：否
     * @param pageSize     分页大小
     * @param pageNumber   页码
     * @return
     */
    @PostMapping("site/pestNumStat")
    public Result sitePestNumStat(@RequestBody Map<String, Object> params) {
        int pageSize;
        int pageIndex;
        if (!params.containsKey("pageSize")) {
            pageSize = Constants.Page.PAGE_SIZE;
        } else {
            pageSize = Integer.parseInt(String.valueOf(params.get("pageSize")));
            if (pageSize > Constants.Page.MAX_PAGE_SIZE) {
                return Result.error(ResultStatusCode.PARAM_IS_INVALID);
            }
        }
        if (!params.containsKey("pageNumber")) {
            pageIndex = 1;
        } else {
            pageIndex = Integer.parseInt(String.valueOf(params.get("pageNumber")));
            if (pageIndex <= 0) {
                return Result.error(ResultStatusCode.PARAM_IS_INVALID);
            }
        }
        if (!params.containsKey("siteId")) {
            return Result.error(ResultStatusCode.PARAM_NOT_COMPLETE);
        }
        String siteId = String.valueOf(params.get("siteId"));
        String startDate;
        String endDate;
        if (params.containsKey("isNearlyWeek")
                && Integer.parseInt(String.valueOf(params.get("isNearlyWeek"))) == 1) {
            Calendar now = Calendar.getInstance();
            endDate = DateUtils.format(now.getTime(), "yyyy-MM-dd");
            now.add(Calendar.DAY_OF_MONTH, -7);
            startDate = DateUtils.format(now.getTime(), "yyyy-MM-dd");
        } else {
            startDate = String.valueOf(params.get("startDate"));
            endDate = String.valueOf(params.get("endDate"));
            if (startDate.isEmpty()
                    || endDate.isEmpty()
                    || "null".equals(startDate)
                    || "null".equals(endDate)) {
                startDate = DateUtils.format(new Date(), "yyyy-MM-dd");
                endDate = startDate;
            } else {
                if (!DateUtils.isValid(startDate, "yyyy-MM-dd")
                        || !DateUtils.isValid(endDate, "yyyy-MM-dd")) {
                    return Result.error(ResultStatusCode.PARAM_IS_INVALID);
                }
            }
        }
        return Result.success(sysInsectRecStatisService.getSomeSitePestNumStat(siteId, startDate, endDate, pageSize, pageIndex));
    }

    /**
     * 单站土壤信息
     *
     * @param siteId       站点ID
     * @param startDate    开始日期
     * @param endDate      结束日期
     * @param isMax        是否最大值筛选：1、最大值；0：最小值，不传：不与虫害关联
     * @param isNearlyWeek 是否是最近一周：1、是；其他：否
     * @param pageSize     分页大小
     * @param pageNumber   页码
     * @return
     */
    @PostMapping("site/soilInfo")
    public Result soilInfo(@RequestBody Map<String, Object> params) {
        int pageSize;
        int pageIndex;
        if (!params.containsKey("pageSize")) {
            pageSize = Constants.Page.PAGE_SIZE;
        } else {
            pageSize = Integer.parseInt(String.valueOf(params.get("pageSize")));
            if (pageSize > Constants.Page.MAX_PAGE_SIZE) {
                return Result.error(ResultStatusCode.PARAM_IS_INVALID);
            }
        }
        if (!params.containsKey("pageNumber")) {
            pageIndex = 1;
        } else {
            pageIndex = Integer.parseInt(String.valueOf(params.get("pageNumber")));
            if (pageIndex <= 0) {
                return Result.error(ResultStatusCode.PARAM_IS_INVALID);
            }
        }
        if (!params.containsKey("siteId")) {
            return Result.error(ResultStatusCode.PARAM_NOT_COMPLETE);
        }
        String siteId = String.valueOf(params.get("siteId"));
        String startDate;
        String endDate;
        if (params.containsKey("isNearlyWeek")
                && Integer.parseInt(String.valueOf(params.get("isNearlyWeek"))) == 1) {
            Calendar now = Calendar.getInstance();
            endDate = DateUtils.format(now.getTime(), "yyyy-MM-dd");
            now.add(Calendar.DAY_OF_MONTH, -7);
            startDate = DateUtils.format(now.getTime(), "yyyy-MM-dd");
        } else {
            startDate = String.valueOf(params.get("startDate"));
            endDate = String.valueOf(params.get("endDate"));
            if (startDate.isEmpty()
                    || endDate.isEmpty()
                    || "null".equals(startDate)
                    || "null".equals(endDate)) {
                startDate = DateUtils.format(new Date(), "yyyy-MM-dd");
                endDate = startDate;
            } else {
                if (!DateUtils.isValid(startDate, "yyyy-MM-dd")
                        || !DateUtils.isValid(endDate, "yyyy-MM-dd")) {
                    return Result.error(ResultStatusCode.PARAM_IS_INVALID);
                }
            }
        }

        String isMax = String.valueOf(params.get("isMax"));
        if (params.containsKey("isMax")
                && !isMax.isEmpty()
                && !"null".equals(isMax)
                && (Integer.parseInt(isMax) == 0 || Integer.parseInt(isMax) == 1)) {
            return Result.success(sysDustRecStatisService.getMaxOrMinPestSoilInfo(siteId, startDate, endDate, Integer.parseInt(isMax), pageIndex, pageSize));
        } else {
            return Result.success(sysDustRecStatisService.getPestSoilInfo(siteId, startDate, endDate, pageIndex, pageSize));
        }
    }

    /**
     * 单站气候信息
     *
     * @param siteId       站点ID
     * @param startDate    开始日期
     * @param endDate      结束日期
     * @param isMax        是否最大值筛选：1、最大值，0：最小值，不传：不与虫害关联
     * @param isNearlyWeek 是否是最近一周：1、是；其他：否
     * @param pageSize     分页大小
     * @param pageNumber   页码
     * @return
     */
    @PostMapping("site/meteInfo")
    public Result meteorologicalInfo(@RequestBody Map<String, Object> params) {
        int pageSize;
        int pageIndex;
        if (!params.containsKey("pageSize")) {
            pageSize = Constants.Page.PAGE_SIZE;
        } else {
            pageSize = Integer.parseInt(String.valueOf(params.get("pageSize")));
            if (pageSize > Constants.Page.MAX_PAGE_SIZE) {
                return Result.error(ResultStatusCode.PARAM_IS_INVALID);
            }
        }
        if (!params.containsKey("pageNumber")) {
            pageIndex = 1;
        } else {
            pageIndex = Integer.parseInt(String.valueOf(params.get("pageNumber")));
            if (pageIndex <= 0) {
                return Result.error(ResultStatusCode.PARAM_IS_INVALID);
            }
        }
        if (!params.containsKey("siteId") || !params.containsKey("startDate") || !params.containsKey("endDate")) {
            return Result.error(ResultStatusCode.PARAM_NOT_COMPLETE);
        }
        String siteId = String.valueOf(params.get("siteId"));
        String startDate;
        String endDate;
        if (params.containsKey("isNearlyWeek")
                && Integer.parseInt(String.valueOf(params.get("isNearlyWeek"))) == 1) {
            Calendar now = Calendar.getInstance();
            endDate = DateUtils.format(now.getTime(), "yyyy-MM-dd");
            now.add(Calendar.DAY_OF_MONTH, -7);
            startDate = DateUtils.format(now.getTime(), "yyyy-MM-dd");
        } else {
            startDate = String.valueOf(params.get("startDate"));
            endDate = String.valueOf(params.get("endDate"));
            if (startDate.isEmpty()
                    || endDate.isEmpty()
                    || "null".equals(startDate)
                    || "null".equals(endDate)) {
                startDate = DateUtils.format(new Date(), "yyyy-MM-dd");
                endDate = startDate;
            } else {
                if (!DateUtils.isValid(startDate, "yyyy-MM-dd")
                        || !DateUtils.isValid(endDate, "yyyy-MM-dd")) {
                    return Result.error(ResultStatusCode.PARAM_IS_INVALID);
                }
            }
        }

        String isMax = String.valueOf(params.get("isMax"));
        if (params.containsKey("isMax")
                && !isMax.isEmpty()
                && !"null".equals(isMax)
                && (Integer.parseInt(isMax) == 0 || Integer.parseInt(isMax) == 1)) {
            return Result.success(sensorRecStatisService.getMaxOrMinPestMeteInfo(siteId, startDate, endDate, Integer.parseInt(isMax), pageIndex, pageSize));
        } else {
            return Result.success(sensorRecStatisService.getPestMeteInfo(siteId, startDate, endDate, pageIndex, pageSize));
        }
    }

    @PostMapping("statis")
    public Result statis() {
        dataStatisSchedule.manual();
        return Result.success();
    }

    public static void main(String[] args) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 3);
        System.out.println(DateUtils.format(c.getTime()));
        c.add(Calendar.DAY_OF_MONTH, -7);
        System.out.println(DateUtils.format(c.getTime()));
    }
}