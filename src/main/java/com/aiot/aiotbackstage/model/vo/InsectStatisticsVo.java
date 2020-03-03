package com.aiot.aiotbackstage.model.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/3/3 15:32
 */
@Data
public class InsectStatisticsVo {

    private Integer total;

    private List<Map<String,Object>> list;
}
