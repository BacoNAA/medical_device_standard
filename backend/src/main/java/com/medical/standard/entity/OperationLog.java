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
 * 操作日志表实体类
 * 严格按照设计文档 v2.0 - PascalCase命名规范
 */
@Data
@TableName("OperationLog")
public class OperationLog implements Serializable {

    /**
     * 日志ID（主键）
     */
    @TableId(value = "LogID", type = IdType.AUTO)
    private Long LogID;

    /**
     * 用户ID（外键）
     */
    @TableField("UserID")
    private Integer UserID;

    /**
     * 操作类型
     */
    @TableField("OperationType")
    private String OperationType;

    /**
     * 操作模块
     */
    @TableField("OperationModule")
    private String OperationModule;

    /**
     * 操作内容
     */
    @TableField("OperationContent")
    private String OperationContent;

    /**
     * IP地址
     */
    @TableField("IPAddress")
    private String IPAddress;

    /**
     * 操作时间
     */
    @TableField("OperationTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime OperationTime;
}

