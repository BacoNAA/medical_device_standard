package com.medical.standard.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户表实体类
 * 严格按照设计文档 v2.0 - PascalCase命名规范
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("User")
public class User extends BaseEntity {

    /**
     * 用户ID（主键）
     */
    @TableId(value = "UserID", type = IdType.AUTO)
    private Integer UserID;

    /**
     * 用户名（格式决定角色）
     */
    @TableField("Username")
    private String Username;

    /**
     * 密码（bcrypt加密）
     */
    @TableField("Password")
    private String Password;

    /**
     * 真实姓名
     */
    @TableField("RealName")
    private String RealName;

    /**
     * 联系电话
     */
    @TableField("Phone")
    private String Phone;

    /**
     * 邮箱
     */
    @TableField("Email")
    private String Email;

    /**
     * 所属部门
     */
    @TableField("Department")
    private String Department;

    /**
     * 角色类型：1-数据输入员，2-系统管理员，3-业务人员，4-质量管理，5-设备管理，6-实验室人员
     */
    @TableField("RoleType")
    private Integer RoleType;

    /**
     * 最后登录时间
     */
    @TableField("LastLoginTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime LastLoginTime;
}

