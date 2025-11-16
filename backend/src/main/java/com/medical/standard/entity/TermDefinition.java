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
 * 术语定义表实体类
 * 严格按照设计文档 v2.0 - PascalCase命名规范
 */
@Data
@TableName("TermDefinition")
public class TermDefinition implements Serializable {

    /**
     * 术语ID（主键）
     */
    @TableId(value = "TermID", type = IdType.AUTO)
    private Integer TermID;

    /**
     * 标准号（外键）
     */
    @TableField("StandardNo")
    private String StandardNo;

    /**
     * 术语编号
     */
    @TableField("TermCode")
    private String TermCode;

    /**
     * 术语名称（中文）
     */
    @TableField("TermName")
    private String TermName;

    /**
     * 术语名称（英文）
     */
    @TableField("TermNameEN")
    private String TermNameEN;

    /**
     * 定义
     */
    @TableField("Definition")
    private String Definition;

    /**
     * 示例
     */
    @TableField("Example")
    private String Example;

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

