package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.common.util.RedisUtils;
import com.aiot.aiotbackstage.mapper.SysInsectDeviceMapper;
import com.aiot.aiotbackstage.mapper.SysInsectInfoMapper;
import com.aiot.aiotbackstage.mapper.SysInsectRecMapper;
import com.aiot.aiotbackstage.mapper.SysSiteMapper;
import com.aiot.aiotbackstage.model.entity.SysInsectDeviceEntity;
import com.aiot.aiotbackstage.model.entity.SysInsectRecEntity;
import com.aiot.aiotbackstage.model.entity.SysSiteEntity;
import com.aiot.aiotbackstage.model.vo.InsectStatisticsVo;
import com.aiot.aiotbackstage.service.IBigScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/3/3 15:30
 */
@Service
public class BigScreenServiceImpl implements IBigScreenService {


    @Autowired
    private SysInsectRecMapper insectRecMapper;

    @Autowired
    private SysInsectDeviceMapper insectDeviceMapper;

    @Autowired
    private SysSiteMapper siteMapper;

    @Autowired
    private RedisUtils redisUtils;



    @Override
    public InsectStatisticsVo insectStatistics() {

        List<SysInsectDeviceEntity> sysInsectDeviceEntities = insectDeviceMapper.selectList(null);
        Map<Integer, List<SysInsectDeviceEntity>> collect = sysInsectDeviceEntities.stream()
                .collect(Collectors.groupingBy(SysInsectDeviceEntity::getSiteId, Collectors.toList()));
        Set<Integer> integers = collect.keySet();
        int sumTotal=0;
        List<Map<String,Object>> list=new ArrayList<>();
        for (Integer integer:integers
             ) {
            List<SysInsectRecEntity> sysInsectRecEntities = insectRecMapper.insectRecGisInfo(integer);
            SysInsectRecEntity sysInsectRecEntity = sysInsectRecEntities.get(0);
            String result = sysInsectRecEntity.getResult();
            String[] split = result.split("#");
            int total=0;
            for (String s:split) {
                String count = s.substring(s.indexOf(",")+1,s.length());
                total+= Integer.parseInt(count);
            }
            SysSiteEntity sysSiteEntity = siteMapper.selectById(integer);
            Map<String,Object> map=new HashMap<>();
            map.put("name",sysSiteEntity.getName());
            map.put("value,",total);
            list.add(map);
            sumTotal+=total;
        }
        InsectStatisticsVo statisticsVo=new InsectStatisticsVo();
        statisticsVo.setTotal(sumTotal);
        statisticsVo.setList(list);
        return statisticsVo;
    }

    @Override
    public Object dustRecStatistics() {
        return null;
    }
}
