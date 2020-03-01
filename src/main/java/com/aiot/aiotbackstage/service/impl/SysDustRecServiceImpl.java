package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.SysDustRecMapper;
import com.aiot.aiotbackstage.model.entity.SysDustRecEntity;
import com.aiot.aiotbackstage.service.ISysDustRecService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDustRecServiceImpl extends ServiceImpl<SysDustRecMapper, SysDustRecEntity>
        implements ISysDustRecService {

    @Override
    public List<SysDustRecEntity> getStatByTime(String siteId, String time) {
        return baseMapper.findByTimeGroupByDepth(siteId, time);
//
//        Map<String, Object> map = new HashMap<>();
//        Map<String, Object> tempMap;
//        for (SysDustRecStatisEntity item : result) {
//            tempMap = new HashMap<>();
//            tempMap.put("")
//            map.put(String.valueOf(item.getDepth()), tempMap);
//        }
    }
}
