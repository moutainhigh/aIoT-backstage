package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "sys_pest_bank")
public class SysPestBankEntity {
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 害虫名称
     */
    @TableField(value = "pest_name")
    private String pestName;

    /**
     * 害虫类型
     */
    @TableField(value = "pest_type")
    private String pestType;

    /**
     * 害虫介绍
     */
    @TableField(value = "pest_introduce")
    private String pestIntroduce;

    /**
     * 害虫图片
     */
    @TableField(value = "pest_img")
    private String pestImg;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;
}