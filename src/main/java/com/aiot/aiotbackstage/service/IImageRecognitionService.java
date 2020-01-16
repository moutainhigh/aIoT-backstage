package com.aiot.aiotbackstage.service;

import com.aiot.aiotbackstage.model.vo.ImageRecognitionVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description 形色实现
 * @Author xiaowenhui
 * @CreateTime 2020/1/7 17:28
 */
public interface IImageRecognitionService {
    List<ImageRecognitionVo> fileUpload(MultipartFile multipartFile, Long userId);
}
