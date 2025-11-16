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
 * 设备关键词表实体类
 * 严格按照设计文档 v2.0 - PascalCase命名规范
 */
@Data
@TableName("EquipmentKeyword")
public class EquipmentKeyword implements Serializable {

    /**
     * 关键词ID（主键）
     */
    @TableId(value = "KeywordID", type = IdType.AUTO)
    private Integer KeywordID;

    /**
     * 设备编码（外键）
     */
    @TableField("EquipmentCode")
    private String EquipmentCode;

    /**
     * 关键词
     */
    @TableField("Keyword")
    private String Keyword;

    /**
     * 创建人ID
     */
    @TableField("CreatedBy")
    private Integer CreatedBy;

    /**
     * 创建时间
     */
    @TableField(value = "CreatedTime", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime CreatedTime;
}

