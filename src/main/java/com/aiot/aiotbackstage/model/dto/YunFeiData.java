package com.aiot.aiotbackstage.model.dto;

import lombok.Data;

/**
 * @author Avernus
 * 对接云飞（虫情测报灯厂商）接口
 */
@Data
public class YunFeiData {

    /**
     * 原始图片网络地址
     */
    private String Image;
    /**
     * 识别结果图片网络地址
     */
    private String Result_image;
    /**
     * 结果字符串，用"种类 1,种类 1 数目#种类 2,种类 2 数目…"表示，如"10,1#12,2"，表示种类 10 的数目是 1，种类 12 的数目是 2
     * 种类参照昆虫对照表
     */
    private String Result;
    /**
     * 设备序列号
     */
    private String imei;
}
