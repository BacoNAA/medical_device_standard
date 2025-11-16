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
 * 自定义输出模板表实体类
 * 严格按照设计文档 v2.0 - PascalCase命名规范
 */
@Data
@TableName("ExportTemplate")
public class ExportTemplate implements Serializable {

    /**
     * 模板ID（主键）
     */
    @TableId(value = "TemplateID", type = IdType.AUTO)
    private Integer TemplateID;

    /**
     * 模板名称
     */
    @TableField("TemplateName")
    private String TemplateName;

    /**
     * 模板描述
     */
    @TableField("Description")
    private String Description;

    /**
     * 是否公开：0-私有，1-公开
     */
    @TableField("IsPublic")
    private Integer IsPublic;

    /**
     * 创建人ID（外键）
     */
    @TableField("CreatedBy")
    private Integer CreatedBy;

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

