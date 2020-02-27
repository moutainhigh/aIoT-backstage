package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysSensorRecStatisEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysSensorRecStatisMapper extends BaseMapper<SysSensorRecStatisEntity> {

    /**
     * 批量插入
     *
     * @param data
     * @return
     */
    int batchInsert(@Param("data") List<SysSensorRecStatisEntity> data);

    /**
     * 通过站点ID查询每时平均值计数
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    int countAllBySiteId(Map<String, Object> params);

    /**
     * 通过站点ID查询每时平均值
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageIndex 页码
     * @param pageSize  分页大小
     * @return
     */
    List<SysSensorRecStatisEntity> findAllBySiteId(Map<String, Object> params);

    /**
     * 通过站点ID查询每天平均值计数
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    int countAllBySiteIdDaily(Map<String, Object> params);

    /**
     * 通过站点ID查询每天平均值
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageIndex 页码
     * @param pageSize  分页大小
     * @return
     */
    List<SysSensorRecStatisEntity> findAllBySiteIdDaily(Map<String, Object> params);
}
