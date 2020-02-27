package com.aiot.aiotbackstage.service.impl;

import com.aiot.aiotbackstage.mapper.SysInsectDeviceMapper;
import com.aiot.aiotbackstage.mapper.SysInsectRecMapper;
import com.aiot.aiotbackstage.model.dto.YunFeiData;
import com.aiot.aiotbackstage.model.entity.SysInsectDeviceEntity;
import com.aiot.aiotbackstage.model.entity.SysInsectRecEntity;
import com.aiot.aiotbackstage.service.ISysInsectRecService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Avernus
 */
@Service
public class SysInsectRecServiceImpl extends ServiceImpl<SysInsectRecMapper, SysInsectRecEntity> implements ISysInsectRecService {

    @Resource
    private SysInsectDeviceMapper sysInsectDeviceMapper;

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
    }
}