package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.*;
import com.aiot.aiotbackstage.model.entity.SysDustRecStatisEntity;
import com.aiot.aiotbackstage.model.vo.PageResult;
import com.aiot.aiotbackstage.service.ISysDustRecStatisService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SysDustRecStatisServiceImpl extends ServiceImpl<SysDustRecStatisMapper, SysDustRecStatisEntity>
        implements ISysDustRecStatisService {

    @Autowired
    private SysDustRecStatisMapper sysDustRecStatisMapper;

    @Autowired
    private SysInsectRecStatisMapper sysInsectRecStatisMapper;

    /**
     * 获取每天土壤信息平均值
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageIndex 页码
     * @param pageSize  分页大小
     * @return
     */
    @Override
    public List<SysDustRecStatisEntity> getPestSoilInfo(String siteId, String startDate, String endDate, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);

        // 查询当天每小时平均值
        if (startDate.equals(endDate)) {
            int total = sysDustRecStatisMapper.countAllBySiteId(params);
            params.put("pageIndex", pageIndex);
            params.put("pageSize", pageSize);
            return sysDustRecStatisMapper.findAllBySiteId(params);
            // TODO
//            return PageResult
        }
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        return sysDustRecStatisMapper.findAllDaily(params);
    }

    /**
     * 最*虫害土壤信息
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param isMax     1：最大，0：最小
     * @param pageIndex 页码
     * @param pageSize  分页大小
     * @return
     */
    @Override
    public PageResult<SysDustRecStatisEntity> getMaxOrMinPestSoilInfo(String siteId, String startDate, String endDate, int isMax, int pageIndex, int pageSize) {
        Map<String, Object> params = new HashMap<>();
        params.put("siteId", siteId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("isMax", isMax);
        // 获取虫害最大日期
        List<Map<String, Object>> pestResult = sysInsectRecStatisMapper.findMaxOrMinPestDate(params);
        if (pestResult != null && pestResult.size() == 1) {
            String pestDate = String.valueOf(pestResult.get(0).get("date"));
            params.clear();
            params.put("siteId", siteId);
            params.put("startDate", pestDate);
            params.put("endDate", pestDate);
            int total =  sysDustRecStatisMapper.countAllDaily(params);
            params.put("pageIndex", pageIndex);
            params.put("pageSize", pageSize);
            List<SysDustRecStatisEntity> result = sysDustRecStatisMapper.findAllDaily(params);
            return PageResult.<SysDustRecStatisEntity>builder()
                    .total(total)
                    .pageData(result)
                    .pageSize(pageSize)
                    .pageNumber(pageIndex)
                    .build();
        }
        return null;
    }
}
