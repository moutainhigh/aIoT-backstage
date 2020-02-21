package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.SysDustRecMapper;
import com.aiot.aiotbackstage.mapper.SysInsectRecMapper;
import com.aiot.aiotbackstage.model.entity.SysInsectRecEntity;
import com.aiot.aiotbackstage.model.vo.SysDustRecVo;
import com.aiot.aiotbackstage.service.ISysDustRecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.http.impl.cookie.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Avernus
 */
@Service
public class SysDustRecServiceImpl extends ServiceImpl<SysInsectRecMapper, SysInsectRecEntity> implements ISysDustRecService {

    @Autowired
    private SysDustRecMapper sysDustRecMapper;

    @Autowired
    private SysInsectRecMapper sysInsectRecMapper;

    /**
     * 最大虫害土壤信息
     *
     * @param siteId    站点ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    @Override
    public List<SysDustRecVo> getBiggestPestSoilInfo(String siteId, long startTime, long endTime) {
        List<SysDustRecVo> stat = getAllDaySoilInfo(siteId, startTime, endTime);

        Map<String, Object> params = new HashMap<>();
        params.put("startTime", DateUtils.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss"));
        params.put("endTime", DateUtils.formatDate(new Date(endTime), "yyyy-MM-dd HH:mm:ss"));
        List<Map<String, Object>> pestStatByDay = sysInsectRecMapper.findPestStatByDay(params);

        // 获取虫害最大日期
        Optional<Map<String, Object>> max = pestStatByDay.stream().max((Comparator.comparing(o -> ((Double) o.get("insect_num")))));
        if (max.isPresent()) {
            String biggestPestDate = (String) max.get().get("date");
            List<SysDustRecVo> result = new ArrayList<>();
            for (SysDustRecVo item : stat) {
                if (item.getDate().equals(biggestPestDate)) {
                    result.add(item);
                }
            }
            return result;
        }
        return null;
    }

    /**
     * 最小虫害土壤信息
     *
     * @param siteId    站点ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    @Override
    public List<SysDustRecVo> getMinimumPestSoilInfo(String siteId, long startTime, long endTime) {
        List<SysDustRecVo> stat = getAllDaySoilInfo(siteId, startTime, endTime);

        // 获取每天虫害数量统计
        Map<String, Object> params = new HashMap<>();
        params.put("startTime", DateUtils.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss"));
        params.put("endTime", DateUtils.formatDate(new Date(endTime), "yyyy-MM-dd HH:mm:ss"));
        List<Map<String, Object>> pestStatByDay = sysInsectRecMapper.findPestStatByDay(params);

        // 获取虫害最小日期
        Optional<Map<String, Object>> min = pestStatByDay.stream().min((Comparator.comparing(o -> ((Double) o.get("insect_num")))));
        if (min.isPresent()) {
            String minimumPestDate = (String) min.get().get("date");
            List<SysDustRecVo> result = new ArrayList<>();
            for (SysDustRecVo item : stat) {
                if (item.getDate().equals(minimumPestDate)) {
                    result.add(item);
                }
            }
            return result;
        }
        return null;
    }

    /**
     * 获取每天土壤信息平均值
     *
     * @param siteId
     * @param startTime
     * @param endTime
     * @return
     */
    private List<SysDustRecVo> getAllDaySoilInfo(String siteId, long startTime, long endTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startTime", DateUtils.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss"));
        params.put("endTime", DateUtils.formatDate(new Date(endTime), "yyyy-MM-dd HH:mm:ss"));
        return sysDustRecMapper.findAllDepthAverageByDay(params);
    }
}
