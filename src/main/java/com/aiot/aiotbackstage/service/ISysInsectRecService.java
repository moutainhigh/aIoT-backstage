package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.dto.YunFeiData;
import com.aiot.aiotbackstage.model.entity.SysInsectRecEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author Avernus
 */
public interface ISysInsectRecService extends IService<SysInsectRecEntity> {

    /**
     * 云飞上传图片识别结果入库
     * @param data
     */
    void save(YunFeiData data);

    Map<String, List<String>> getStatByTime(String time);
}
