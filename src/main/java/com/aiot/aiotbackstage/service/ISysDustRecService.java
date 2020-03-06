package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysDustRecEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface ISysDustRecService extends IService<SysDustRecEntity> {

    Map<Integer, Map<String, Object>> getStatByTime(String time);

    Map<String, Object> current(Integer siteId);
}