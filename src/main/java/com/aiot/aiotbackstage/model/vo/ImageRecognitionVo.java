package com.aiot.aiotbackstage.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 相似照片返回参数
 * @Author xiaowenhui
 * @CreateTime 2020/1/15 16:24
 */
@Data
public class ImageRecognitionVo {

    /**
     * 参考地址
     */
    private String referenceUrl;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String desc;

    /**
     * 相似度
     */
    private BigDecimal probability;

    /**
     * 详情地址
     */
    private String detailUrl;
}
