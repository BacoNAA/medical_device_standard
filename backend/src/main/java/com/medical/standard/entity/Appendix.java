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
 * 附录表实体类
 * 严格按照设计文档 v2.0 - PascalCase命名规范
 */
@Data
@TableName("Appendix")
public class Appendix implements Serializable {

    /**
     * 附录ID（主键）
     */
    @TableId(value = "AppendixID", type = IdType.AUTO)
    private Integer AppendixID;

    /**
     * 标准号（外键）
     */
    @TableField("StandardNo")
    private String StandardNo;

    /**
     * 附录编号（如A、B、C）
     */
    @TableField("AppendixCode")
    private String AppendixCode;

    /**
     * 附录标题
     */
    @TableField("AppendixTitle")
    private String AppendixTitle;

    /**
     * 附录类型（规范性/资料性）
     */
    @TableField("AppendixType")
    private String AppendixType;

    /**
     * 附录内容
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

    /**
     * 更新时间
     */
    @TableField(value = "UpdatedTime", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime UpdatedTime;
}

