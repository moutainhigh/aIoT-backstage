package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Avernus
 */
@Data
@TableName("sys_insect_rec")
@Accessors(chain = true)
public class SysInsectRecEntity {

    @TableId(value = "id", type = IdType.AUTO)
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

    @TableField(exist = false)
    private String siteName;

    @TableField(exist = false)
    private List<Map<String,Object>> maps;

    private Date time;
}
