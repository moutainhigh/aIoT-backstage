package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysInsectRecStatisEntity;
import com.aiot.aiotbackstage.model.vo.SysInsectRecStatisVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysInsectRecStatisMapper extends BaseMapper<SysInsectRecStatisEntity> {

    /**
     * 批量插入
     *
     * @param data
     * @return
     */
    int batchInsert(@Param("data") List<SysInsectRecStatisEntity> data);

    /**
     * 通过站点ID分组查询
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    List<Map<String, Object>> findAllGroupBySiteId(Map<String, Object> params);

    /**
     * 通过站点ID查询计数
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    int countAllBySiteId(Map<String, Object> params);

    /**
     * 通过站点ID查询
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageSize  分页大小
     * @param pageIndex 页码
     * @return
     */
    List<Map<String, Object>> findAllBySiteId(Map<String, Object> params);

    /**
     * 查询虫害最严重/最轻日期
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    List<Map<String, Object>> findMaxOrMinPestDate(Map<String, Object> params);

    /**
     * 查询虫害
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    List<Map<String, Object>> findAllGroupByInsectId(Map<String, Object> params);

    /**
     * 查询虫害
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    List<Map<String, Object>> findAllGroupByDate(Map<String, Object> params);

    /**
     * 查询每月害虫统计
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> findAllGroupByDateMonth(Map<String, Object> params);
}
