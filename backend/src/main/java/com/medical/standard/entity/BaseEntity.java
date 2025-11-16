package com.medical.standard.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基础实体类
 * 包含所有表的公共字段（严格按照设计文档 v2.0 - PascalCase命名规范）
 * 注意：不包含主键字段，因为每个表的主键名称不同（如UserID、ItemID、StandardNo等）
 */
@Data
public class BaseEntity implements Serializable {

    /**
     * 创建人ID
     */
    @TableField(value = "CreatedBy", fill = FieldFill.INSERT)
    private Integer CreatedBy;

    /**
     * 创建时间
     */
    @TableField(value = "CreatedTime", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime CreatedTime;

    /**
     * 更新人ID
     */
    @TableField(value = "UpdatedBy", fill = FieldFill.INSERT_UPDATE)
    private Integer UpdatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "UpdatedTime", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime UpdatedTime;
}

