package com.medical.standard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 标准信息表实体类
 * 严格按照设计文档 v2.0 - PascalCase命名规范
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("Standard")
public class Standard extends BaseEntity {

    /**
     * 标准号（主键，如GB 15810-2019）
     */
    @TableId(value = "StandardNo", type = IdType.INPUT)
    private String StandardNo;

    /**
     * 标准名称（中文）
     */
    @TableField("StandardName")
    private String StandardName;

    /**
     * 标准名称（英文）
     */
    @TableField("StandardNameEN")
    private String StandardNameEN;

    /**
     * ISO对应标准
     */
    @TableField("ISOReference")
    private String ISOReference;

    /**
     * 发布日期
     */
    @TableField("PublishDate")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate PublishDate;

    /**
     * 实施日期
     */
    @TableField("EffectiveDate")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate EffectiveDate;

    /**
     * 范围
     */
    @TableField("Scope")
    private String Scope;

    /**
     * 规范性引用文件
     */
    @TableField("`References`")
    private String References;

    /**
     * 前言
     */
    @TableField("Foreword")
    private String Foreword;

    /**
     * 引言
     */
    @TableField("Introduction")
    private String Introduction;
}

