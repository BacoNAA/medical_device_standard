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
 * 检验项目表实体类
 * 严格按照设计文档 v2.0 - PascalCase命名规范
 * 注意：此表不继承BaseEntity，因为没有CreatedBy和UpdatedBy字段
 */
@Data
@TableName("RequirementItem")
public class RequirementItem implements Serializable {

    /**
     * 项目ID（主键）
     */
    @TableId(value = "ItemID", type = IdType.AUTO)
    private Integer ItemID;

    /**
     * 标准号（外键）
     */
    @TableField("StandardNo")
    private String StandardNo;

    /**
     * 条款号
     */
    @TableField("ClauseNo")
    private String ClauseNo;

    /**
     * 项目名称
     */
    @TableField("ItemName")
    private String ItemName;

    /**
     * 要求内容
     */
    @TableField("Requirement")
    private String Requirement;

    /**
     * 方法引用
     */
    @TableField("MethodRef")
    private String MethodRef;

    /**
     * 方法内容
     */
    @TableField("MethodContent")
    private String MethodContent;

    /**
     * 方法类型：1-无独立方法，2-引用附录，3-完整方法
     */
    @TableField("MethodType")
    private Integer MethodType;

    /**
     * 备注
     */
    @TableField("Notes")
    private String Notes;

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

