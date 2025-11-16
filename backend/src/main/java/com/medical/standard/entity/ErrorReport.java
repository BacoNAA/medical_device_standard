package com.medical.standard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 错误报告表实体类
 * 严格按照设计文档 v2.0 - PascalCase命名规范
 */
@Data
@TableName("ErrorReport")
public class ErrorReport implements Serializable {

    /**
     * 报告ID（主键）
     */
    @TableId(value = "ReportID", type = IdType.AUTO)
    private Integer ReportID;

    /**
     * 报告编号（唯一）
     */
    @TableField("ReportNo")
    private String ReportNo;

    /**
     * 标准号（外键）
     */
    @TableField("StandardNo")
    private String StandardNo;

    /**
     * 条款号
     */
    @TableField("ClauseNo")
    private String ClauseNo;

    /**
     * 错误描述
     */
    @TableField("ErrorDescription")
    private String ErrorDescription;

    /**
     * 证据材料
     */
    @TableField("Evidence")
    private String Evidence;

    /**
     * 报告人ID（外键）
     */
    @TableField("ReporterID")
    private Integer ReporterID;

    /**
     * 报告时间
     */
    @TableField("ReportTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime ReportTime;

    /**
     * 状态：1-待处理，2-处理中，3-已完成，4-已驳回
     */
    @TableField("Status")
    private Integer Status;

    /**
     * 处理人ID（外键）
     */
    @TableField("HandlerID")
    private Integer HandlerID;

    /**
     * 处理时间
     */
    @TableField("HandleTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime HandleTime;

    /**
     * 处理备注
     */
    @TableField("HandleRemark")
    private String HandleRemark;
}

