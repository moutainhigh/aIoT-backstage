package com.aiot.aiotbackstage.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Description TODO
 * @Author xiaowenhui
 * @CreateTime 2020/2/24 14:30
 */
public interface IPestAdoptionService {
    void importWatchExcel(MultipartFile xlsFile);
}
