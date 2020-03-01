package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysDustRecEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface ISysDustRecService extends IService<SysDustRecEntity> {

    Map<Integer, Object> getStatByTime(String siteId, String time);
}