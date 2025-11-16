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
 * 产品技术要求表实体类
 * 严格按照设计文档 v2.0 - PascalCase命名规范
 */
@Data
@TableName("PTRDocument")
public class PTRDocument implements Serializable {

    /**
     * 文档ID（主键）
     */
    @TableId(value = "DocumentID", type = IdType.AUTO)
    private Integer DocumentID;

    /**
     * 文档编号（唯一）
     */
    @TableField("DocumentNo")
    private String DocumentNo;

    /**
     * 产品名称
     */
    @TableField("ProductName")
    private String ProductName;

    /**
     * 型号规格
     */
    @TableField("Model")
    private String Model;

    /**
     * 文档内容（富文本）
     */
    @TableField("Content")
    private String Content;

    /**
     * 状态：1-草稿，2-审核中，3-已发布
     */
    @TableField("Status")
    private Integer Status;

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
     * 更新人ID（外键）
     */
    @TableField("UpdatedBy")
    private Integer UpdatedBy;

    /**
     * 更新时间
     */
    @TableField(value = "UpdatedTime", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime UpdatedTime;
}

