package com.aiot.aiotbackstage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "sys_warn_info")
public class SysWarnInfoEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 站点id
     */
    @TableField(value = "site_id")
    private Integer siteId;

    /**
     * 站点id
     */
    @TableField(value = "site_name")
    private String siteName;

    /**
     * 是否审核通过（0-未审核，1-通过，2-不通过）
     */
    @TableField(value = "is_examine")
    private Integer isExamine;

    /**
     * 是否关闭（1-关闭，2-未关闭）
     */
    @TableField(value = "is_closed")
    private Integer isClosed;

    /**
     * 预警时间
     */
    @TableField(value = "time")
    private Date time;

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