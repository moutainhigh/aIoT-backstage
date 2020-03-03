package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "sys_warn_rule")
public class SysWarnRuleEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 预警对象
     */
    @TableField(value = "early_type")
    private String earlyType;

    /**
     * 预警名称
     */
    @TableField(value = "early_name")
    private String earlyName;

    /**
     * 土壤深度
     */
    @TableField(value = "early_depth")
    private String earlyDepth;


    /**
     * 预警阈值最大值
     */
    @TableField(value = "early_max")
    private String earlyMax;

    /**
     * 预警阈值最小值
     */
    @TableField(value = "early_min")
    private String earlyMin;

    /**
     * 防治措施
     */
    @TableField(value = "measures")
    private String measures;


    /**
     * 预警程度
     */
    @TableField(value = "early_degree")
    private String earlyDegree;

    /**
     * 预警内容
     */
    @TableField(value = "early_content")
    private String earlyContent;

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