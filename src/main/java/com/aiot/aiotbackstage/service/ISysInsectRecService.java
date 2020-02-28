package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.dto.YunFeiData;
import com.aiot.aiotbackstage.model.entity.SysInsectRecEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Avernus
 */
public interface ISysInsectRecService extends IService<SysInsectRecEntity> {

    /**
     * 云飞上传图片识别结果入库
     * @param data
     */
    void save(YunFeiData data);
}
