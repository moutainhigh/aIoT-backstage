package com.aiot.aiotbackstage.controller;

import com.aiot.aiotbackstage.common.constant.Constants;
import com.aiot.aiotbackstage.common.constant.Result;
import com.aiot.aiotbackstage.common.constant.ResultStatusCode;
import com.aiot.aiotbackstage.common.util.DateUtils;
import com.aiot.aiotbackstage.model.dto.YunFeiData;
import com.aiot.aiotbackstage.server.schedule.DataStatisSchedule;
import com.aiot.aiotbackstage.service.impl.SensorRecStatisServiceImpl;
import com.aiot.aiotbackstage.service.impl.SysDustRecStatisServiceImpl;
import com.aiot.aiotbackstage.service.impl.SysInsectRecServiceImpl;
import com.aiot.aiotbackstage.service.impl.SysInsectRecStatisServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "数据API", description = "Data Controller")
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
    @ApiOperation(value = "全站害虫统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "开始日期", value = "startDate"),
            @ApiImplicitParam(name = "结束日期", value = "endDate")
    })
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
    @ApiOperation(value = "单站害虫统计及详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "站点ID", value = "siteId", required = true),
            @ApiImplicitParam(name = "开始日期", value = "startDate"),
            @ApiImplicitParam(name = "结束日期", value = "endDate"),
            @ApiImplicitParam(name = "是否是最近一周", value = "isNearlyWeek"),
            @ApiImplicitParam(name = "分页大小", value = "pageSize"),
            @ApiImplicitParam(name = "页码", value = "pageNumber")
    })
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
    @ApiOperation(value = "单站土壤信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "站点ID", value = "siteId", required = true),
            @ApiImplicitParam(name = "开始日期", value = "startDate"),
            @ApiImplicitParam(name = "结束日期", value = "endDate"),
            @ApiImplicitParam(name = "是否是最近一周", value = "isNearlyWeek"),
            @ApiImplicitParam(name = "分页大小", value = "pageSize"),
            @ApiImplicitParam(name = "页码", value = "pageNumber")
    })
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
            return Result.error(ResultStatusCode.PARAM_IS_INVALID);
        }
    }

    /**
     * 单站土壤统计信息
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    @ApiOperation(value = "单站土壤统计信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "站点ID", value = "siteId", required = true),
            @ApiImplicitParam(name = "开始日期", value = "startDate"),
            @ApiImplicitParam(name = "结束日期", value = "endDate")
    })
    @PostMapping("stat/site/soilInfo")
    public Result soilStatisInfo(@RequestBody Map<String, Object> params) {
        if (!params.containsKey("siteId")) {
            return Result.error(ResultStatusCode.PARAM_NOT_COMPLETE);
        }
        String siteId = String.valueOf(params.get("siteId"));
        String startDate = String.valueOf(params.get("startDate"));
        String endDate = String.valueOf(params.get("endDate"));
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

        return Result.success(sysDustRecStatisService.getPestSoilInfo(siteId, startDate, endDate));
    }

    /**
     * 单站气候信息分页
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
    @ApiOperation(value = "单站气候信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "站点ID", value = "siteId", required = true),
            @ApiImplicitParam(name = "开始日期", value = "startDate"),
            @ApiImplicitParam(name = "结束日期", value = "endDate"),
            @ApiImplicitParam(name = "是否是最近一周", value = "isNearlyWeek"),
            @ApiImplicitParam(name = "分页大小", value = "pageSize"),
            @ApiImplicitParam(name = "页码", value = "pageNumber")
    })
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
            return Result.error(ResultStatusCode.PARAM_IS_INVALID);
        }
    }

    /**
     * 单站气候信息
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    @ApiOperation(value = "单站气候统计信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "站点ID", value = "siteId", required = true),
            @ApiImplicitParam(name = "开始日期", value = "startDate"),
            @ApiImplicitParam(name = "结束日期", value = "endDate")
    })
    @PostMapping("stat/site/meteInfo")
    public Result meteorologicalStatInfo(@RequestBody Map<String, Object> params) {
        if (!params.containsKey("siteId") || !params.containsKey("startDate") || !params.containsKey("endDate")) {
            return Result.error(ResultStatusCode.PARAM_NOT_COMPLETE);
        }
        String siteId = String.valueOf(params.get("siteId"));
        String startDate = String.valueOf(params.get("startDate"));
        String endDate = String.valueOf(params.get("endDate"));
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

        return Result.success(sensorRecStatisService.getPestMeteInfo(siteId, startDate, endDate));
    }

    @PostMapping("statis")
    public Result statis() {
        dataStatisSchedule.manual();
        return Result.success();
    }
}