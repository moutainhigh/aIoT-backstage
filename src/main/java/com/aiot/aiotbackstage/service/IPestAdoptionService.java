package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.entity.SysPestAdoptionEntity;
import com.aiot.aiotbackstage.model.param.PageParam;
import com.aiot.aiotbackstage.model.vo.PageResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/24 14:30
 */
public interface IPestAdoptionService {
    void importWatchExcel(MultipartFile xlsFile);

    PageResult<SysPestAdoptionEntity> pestAdoption(PageParam pageParam);
}
