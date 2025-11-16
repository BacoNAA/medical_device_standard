package com.medical.standard.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 条款信息表实体类
 * 严格按照设计文档 v2.0 - PascalCase命名规范
 * 注意：此表使用复合主键（StandardNo + ClauseNo）
 */
@Data
@TableName("Clause")
public class Clause implements Serializable {

    /**
     * 标准号（复合主键之一，外键）
     */
    @TableField("StandardNo")
    private String StandardNo;

    /**
     * 条款号（复合主键之一）
     */
    @TableField("ClauseNo")
    private String ClauseNo;

    /**
     * 条款标题
     */
    @TableField("ClauseTitle")
    private String ClauseTitle;

    /**
     * 父条款号
     */
    @TableField("ParentClauseNo")
    private String ParentClauseNo;

    /**
     * 层级（1-一级，2-二级，3-三级）
     */
    @TableField("Level")
    private Integer Level;

    /**
     * 条款内容
     */
    @TableField("Content")
    private String Content;

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
}

