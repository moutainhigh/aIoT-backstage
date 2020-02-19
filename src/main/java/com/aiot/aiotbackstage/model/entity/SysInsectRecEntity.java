package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * @author Avernus
 */
@TableName("sys_insect_rec")
public class SysInsectRecEntity {

    private Integer id;

    private Integer deviceId;
    /**
     * 原始图片网络地址
     */
    private String image;
    /**
     * 识别结果图片网络地址
     */
    private String resultImage;
    /**
     * 结果字符串，用"种类1,种类1数目#种类2,种类2数目…"表示，如"10,1#12,2"，表示种类10的数目是1,种类12的数目是2
     * 种类参照昆虫对照表
     */
    private String result;

    private Date time;
}
