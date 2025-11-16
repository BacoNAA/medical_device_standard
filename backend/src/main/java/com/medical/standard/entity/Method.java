package com.medical.standard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 检验方法表实体类
 * 严格按照设计文档 v2.0 - PascalCase命名规范
 */
@Data
@TableName("Method")
public class Method implements Serializable {

    /**
     * 方法ID（主键）
     */
    @TableId(value = "MethodID", type = IdType.AUTO)
    private Integer MethodID;

    /**
     * 标准号（外键）
     */
    @TableField("StandardNo")
    private String StandardNo;

    /**
     * 方法编号
     */
    @TableField("MethodCode")
    private String MethodCode;

    /**
     * 方法名称
     */
    @TableField("MethodName")
    private String MethodName;

    /**
     * 方法内容
     */
    @TableField("MethodContent")
    private String MethodContent;

    /**
     * 附录引用
     */
    @TableField("AppendixRef")
    private String AppendixRef;

    /**
     * 排序号
     */
    @TableField("SortOrder")
    private Integer SortOrder;

    /**
     * 创建时间
     */
    @TableField(value = "CreatedTime", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime CreatedTime;

    /**
     * 更新时间
     */
    @TableField(value = "UpdatedTime", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime UpdatedTime;
}

