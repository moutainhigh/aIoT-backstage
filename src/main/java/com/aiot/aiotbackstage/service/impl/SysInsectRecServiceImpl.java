package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.SysInsectDeviceMapper;
import com.aiot.aiotbackstage.mapper.SysInsectInfoMapper;
import com.aiot.aiotbackstage.mapper.SysInsectRecMapper;
import com.aiot.aiotbackstage.model.dto.YunFeiData;
import com.aiot.aiotbackstage.model.entity.SysInsectDeviceEntity;
import com.aiot.aiotbackstage.model.entity.SysInsectInfoEntity;
import com.aiot.aiotbackstage.model.entity.SysInsectRecEntity;
import com.aiot.aiotbackstage.service.IEarlyWarningService;
import com.aiot.aiotbackstage.service.ISysInsectRecService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author Avernus
 */
@Service
public class SysInsectRecServiceImpl extends ServiceImpl<SysInsectRecMapper, SysInsectRecEntity> implements ISysInsectRecService {

    @Resource
    private SysInsectDeviceMapper sysInsectDeviceMapper;
    @Resource
    private IEarlyWarningService earlyWarningService;
    @Autowired
    private SysInsectInfoMapper insectInfoMapper;

    @Override
    public void save(YunFeiData data) {
        SysInsectDeviceEntity device = sysInsectDeviceMapper.selectOne(new QueryWrapper<SysInsectDeviceEntity>().eq("imei", data.getImei()));
        SysInsectRecEntity rec = new SysInsectRecEntity()
                .setDeviceId(device.getId())
                .setImage(data.getImage())
                .setResult(data.getResult())
                .setResultImage(data.getResult_image())
                .setTime(new Date());
        this.baseMapper.insert(rec);

        try {
            List<Map<String,Object>> maps=new ArrayList<>();
            String result = rec.getResult();
            String[] split = result.split("#");
            Arrays.stream(split).forEach(s -> {
                String pestId = s.substring(0, s.indexOf(","));
                SysInsectInfoEntity sysInsectInfoEntity = insectInfoMapper.selectById(pestId);
                String count = s.substring(s.indexOf(",")+1,s.length());
                Map<String,Object> map=new HashMap<>();
                map.put("pestName",sysInsectInfoEntity.getName());
                map.put("count",count);
                maps.add(map);
            });
            maps.stream().forEach(stringObjectMap -> {
                earlyWarningService.earlyWarningReport("虫情",(String) stringObjectMap.get("pestName"),null,String.valueOf(stringObjectMap.get("count")),null);
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}