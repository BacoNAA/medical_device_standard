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
 * 设备信息表实体类
 * 严格按照设计文档 v2.0 - PascalCase命名规范
 */
@Data
@TableName("Equipment")
public class Equipment implements Serializable {

    /**
     * 设备编码（主键，如A001）
     */
    @TableId(value = "EquipmentCode", type = IdType.INPUT)
    private String EquipmentCode;

    /**
     * 设备名称
     */
    @TableField("EquipmentName")
    private String EquipmentName;

    /**
     * 型号规格
     */
    @TableField("Model")
    private String Model;

    /**
     * 设备描述
     */
    @TableField("Description")
    private String Description;

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

