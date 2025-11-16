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
 * 模板字段配置表实体类
 * 严格按照设计文档 v2.0 - PascalCase命名规范
 */
@Data
@TableName("TemplateField")
public class TemplateField implements Serializable {

    /**
     * 字段ID（主键）
     */
    @TableId(value = "FieldID", type = IdType.AUTO)
    private Integer FieldID;

    /**
     * 模板ID（外键）
     */
    @TableField("TemplateID")
    private Integer TemplateID;

    /**
     * 字段名称
     */
    @TableField("FieldName")
    private String FieldName;

    /**
     * 字段标签
     */
    @TableField("FieldLabel")
    private String FieldLabel;

    /**
     * 字段类型
     */
    @TableField("FieldType")
    private String FieldType;

    /**
     * 是否必填：0-否，1-是
     */
    @TableField("IsRequired")
    private Integer IsRequired;

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

