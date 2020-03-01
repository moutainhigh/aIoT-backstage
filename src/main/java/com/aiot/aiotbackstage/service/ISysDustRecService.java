package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysDustRecEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ISysDustRecService extends IService<SysDustRecEntity> {

    List<SysDustRecEntity> getStatByTime(String siteId, String time);
}