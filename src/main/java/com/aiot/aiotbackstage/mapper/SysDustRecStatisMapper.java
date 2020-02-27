package com.aiot.aiotbackstage.mapper;

import com.aiot.aiotbackstage.model.entity.SysDustRecStatisEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SysDustRecStatisMapper extends BaseMapper<SysDustRecStatisEntity> {

    /**
     * 批量插入
     *
     * @param data
     * @return
     */
    int batchInsert(@Param("data") List<SysDustRecStatisEntity> data);

    /**
     * 根据站点ID查询
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    int countAllBySiteId(Map<String, Object> params);

    /**
     * 根据站点ID查询
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageIndex 页码
     * @param pageSize  分页大小
     * @return
     */
    List<SysDustRecStatisEntity> findAllBySiteId(Map<String, Object> params);

    /**
     * 根据站点ID查询每天平均值计数
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    int countAllDaily(Map<String, Object> params);

    /**
     * 根据站点ID查询每天平均值
     *
     * @param siteId    站点ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param pageIndex 页码
     * @param pageSize  分页大小
     * @return
     */
    List<SysDustRecStatisEntity> findAllDaily(Map<String, Object> params);
}
