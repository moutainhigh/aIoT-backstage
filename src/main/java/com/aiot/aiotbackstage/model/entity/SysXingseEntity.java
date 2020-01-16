package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "sys_xingse")
public class SysXingseEntity {
    /**
     * 形色ID
     */
    @TableId(value = "xingse_id", type = IdType.AUTO)
    private Long xingseId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 父级ID
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 参考地址
     */
    @TableField(value = "reference_url")
    private String referenceUrl;

    /**
     * 名称
     */
    @TableField(value = "img_name")
    private String imgName;

    /**
     * 描述
     */
    @TableField(value = "img_desc")
    private String imgDesc;

    /**
     * 相似度
     */
    @TableField(value = "probability")
    private BigDecimal probability;

    /**
     * 详情地址
     */
    @TableField(value = "detail_url")
    private String detailUrl;

    @TableField(value = "create_time")
    private Date createTime;

    @TableField(value = "update_time")
    private Date updateTime;
}