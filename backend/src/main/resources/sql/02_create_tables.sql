-- =====================================================
-- 医疗器械标准数据库 - 数据表创建脚本
-- 严格按照设计文档 v2.0 实现（PascalCase命名）
-- =====================================================

USE medical_device_standards;

-- =====================================================
-- 1. 用户表 (User)
-- =====================================================
CREATE TABLE User (
    UserID INT AUTO_INCREMENT COMMENT '用户ID',
    Username VARCHAR(50) NOT NULL COMMENT '用户名（格式决定角色）',
    Password VARCHAR(200) NOT NULL COMMENT '密码（bcrypt加密）',
    RealName VARCHAR(50) NOT NULL COMMENT '真实姓名',
    Phone VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
    Email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    Department VARCHAR(100) DEFAULT NULL COMMENT '所属部门',
    RoleType TINYINT NOT NULL COMMENT '角色类型：1-数据输入员，2-系统管理员，3-业务人员，4-质量管理，5-设备管理，6-实验室人员',
    CreatedBy INT NOT NULL COMMENT '创建人ID',
    CreatedTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdatedBy INT DEFAULT NULL COMMENT '更新人ID',
    UpdatedTime DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    LastLoginTime DATETIME DEFAULT NULL COMMENT '最后登录时间',
    PRIMARY KEY (UserID),
    UNIQUE KEY UK_User_Username (Username),
    INDEX IDX_User_RoleType (RoleType)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =====================================================
-- 2. 标准信息表 (Standard)
-- =====================================================
CREATE TABLE Standard (
    StandardNo VARCHAR(50) NOT NULL COMMENT '标准号（主键，如GB 15810-2019）',
    StandardName VARCHAR(200) NOT NULL COMMENT '标准名称（中文）',
    StandardNameEN VARCHAR(200) DEFAULT NULL COMMENT '标准名称（英文）',
    ISOReference VARCHAR(100) DEFAULT NULL COMMENT 'ISO对应标准',
    PublishDate DATE DEFAULT NULL COMMENT '发布日期',
    EffectiveDate DATE DEFAULT NULL COMMENT '实施日期',
    Scope TEXT DEFAULT NULL COMMENT '范围',
    `References` TEXT DEFAULT NULL COMMENT '规范性引用文件',
    Foreword TEXT DEFAULT NULL COMMENT '前言',
    Introduction TEXT DEFAULT NULL COMMENT '引言',
    CreatedBy INT NOT NULL COMMENT '创建人ID',
    CreatedTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdatedBy INT DEFAULT NULL COMMENT '更新人ID',
    UpdatedTime DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (StandardNo),
    INDEX IDX_Standard_PublishDate (PublishDate)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标准信息表';

-- =====================================================
-- 3. 检验项目表 (RequirementItem)
-- =====================================================
CREATE TABLE RequirementItem (
    ItemID INT AUTO_INCREMENT COMMENT '项目ID',
    StandardNo VARCHAR(50) NOT NULL COMMENT '标准号（外键）',
    ClauseNo VARCHAR(20) NOT NULL COMMENT '条款号',
    ItemName VARCHAR(200) NOT NULL COMMENT '项目名称',
    Requirement TEXT NOT NULL COMMENT '要求内容',
    MethodRef VARCHAR(100) DEFAULT NULL COMMENT '方法引用',
    MethodContent TEXT DEFAULT NULL COMMENT '方法内容',
    MethodType TINYINT NOT NULL DEFAULT 1 COMMENT '方法类型：1-无独立方法，2-引用附录，3-完整方法',
    Notes TEXT DEFAULT NULL COMMENT '备注',
    SortOrder INT NOT NULL DEFAULT 0 COMMENT '排序号',
    CreatedTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdatedTime DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (ItemID),
    INDEX IDX_RequirementItem_StandardNo (StandardNo),
    INDEX IDX_RequirementItem_ClauseNo (ClauseNo),
    CONSTRAINT FK_RequirementItem_Standard FOREIGN KEY (StandardNo) REFERENCES Standard(StandardNo) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='检验项目表';

-- =====================================================
-- 4. 术语定义表 (TermDefinition)
-- =====================================================
CREATE TABLE TermDefinition (
    TermID INT AUTO_INCREMENT COMMENT '术语ID',
    StandardNo VARCHAR(50) NOT NULL COMMENT '标准号（外键）',
    TermCode VARCHAR(20) NOT NULL COMMENT '术语编号',
    TermName VARCHAR(200) NOT NULL COMMENT '术语名称（中文）',
    TermNameEN VARCHAR(200) DEFAULT NULL COMMENT '术语名称（英文）',
    Definition TEXT NOT NULL COMMENT '定义',
    Example TEXT DEFAULT NULL COMMENT '示例',
    SortOrder INT NOT NULL DEFAULT 0 COMMENT '排序号',
    CreatedTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (TermID),
    INDEX IDX_TermDefinition_StandardNo (StandardNo),
    CONSTRAINT FK_TermDefinition_Standard FOREIGN KEY (StandardNo) REFERENCES Standard(StandardNo) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='术语定义表';

-- =====================================================
-- 5. 条款信息表 (Clause)
-- =====================================================
CREATE TABLE Clause (
    StandardNo VARCHAR(50) NOT NULL COMMENT '标准号（外键）',
    ClauseNo VARCHAR(20) NOT NULL COMMENT '条款号',
    ClauseTitle VARCHAR(200) NOT NULL COMMENT '条款标题',
    ParentClauseNo VARCHAR(20) DEFAULT NULL COMMENT '父条款号',
    Level TINYINT NOT NULL COMMENT '层级（1-一级，2-二级，3-三级）',
    Content TEXT DEFAULT NULL COMMENT '条款内容',
    SortOrder INT NOT NULL DEFAULT 0 COMMENT '排序号',
    CreatedTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (StandardNo, ClauseNo),
    INDEX IDX_Clause_ParentClauseNo (ParentClauseNo),
    CONSTRAINT FK_Clause_Standard FOREIGN KEY (StandardNo) REFERENCES Standard(StandardNo) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='条款信息表';

-- =====================================================
-- 6. 检验方法表 (Method)
-- =====================================================
CREATE TABLE Method (
    MethodID INT AUTO_INCREMENT COMMENT '方法ID',
    StandardNo VARCHAR(50) NOT NULL COMMENT '标准号（外键）',
    MethodCode VARCHAR(50) NOT NULL COMMENT '方法编号',
    MethodName VARCHAR(200) NOT NULL COMMENT '方法名称',
    MethodContent TEXT NOT NULL COMMENT '方法内容',
    AppendixRef VARCHAR(50) DEFAULT NULL COMMENT '附录引用',
    SortOrder INT NOT NULL DEFAULT 0 COMMENT '排序号',
    CreatedTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdatedTime DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (MethodID),
    INDEX IDX_Method_StandardNo (StandardNo),
    CONSTRAINT FK_Method_Standard FOREIGN KEY (StandardNo) REFERENCES Standard(StandardNo) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='检验方法表';

-- =====================================================
-- 7. 附录表 (Appendix)
-- =====================================================
CREATE TABLE Appendix (
    AppendixID INT AUTO_INCREMENT COMMENT '附录ID',
    StandardNo VARCHAR(50) NOT NULL COMMENT '标准号（外键）',
    AppendixCode VARCHAR(20) NOT NULL COMMENT '附录编号（如A、B、C）',
    AppendixTitle VARCHAR(200) NOT NULL COMMENT '附录标题',
    AppendixType VARCHAR(20) NOT NULL COMMENT '附录类型（规范性/资料性）',
    Content TEXT NOT NULL COMMENT '附录内容',
    SortOrder INT NOT NULL DEFAULT 0 COMMENT '排序号',
    CreatedTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdatedTime DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (AppendixID),
    INDEX IDX_Appendix_StandardNo (StandardNo),
    CONSTRAINT FK_Appendix_Standard FOREIGN KEY (StandardNo) REFERENCES Standard(StandardNo) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='附录表';

-- =====================================================
-- 8. 设备信息表 (Equipment)
-- =====================================================
CREATE TABLE Equipment (
    EquipmentCode CHAR(4) NOT NULL COMMENT '设备编码（主键，如A001）',
    EquipmentName VARCHAR(200) NOT NULL COMMENT '设备名称',
    Model VARCHAR(100) DEFAULT NULL COMMENT '型号规格',
    Description TEXT DEFAULT NULL COMMENT '设备描述',
    CreatedBy INT NOT NULL COMMENT '创建人ID',
    CreatedTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (EquipmentCode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备信息表';

-- =====================================================
-- 9. 项目设备关联表 (ItemEquipment)
-- =====================================================
CREATE TABLE ItemEquipment (
    RelationID INT AUTO_INCREMENT COMMENT '关联ID',
    ItemID INT NOT NULL COMMENT '项目ID（外键）',
    EquipmentCode CHAR(4) NOT NULL COMMENT '设备编码（外键）',
    SpecificationRequirement VARCHAR(500) DEFAULT NULL COMMENT '规格要求',
    CreatedTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (RelationID),
    INDEX IDX_ItemEquipment_ItemID (ItemID),
    INDEX IDX_ItemEquipment_EquipmentCode (EquipmentCode),
    CONSTRAINT FK_ItemEquipment_Item FOREIGN KEY (ItemID) REFERENCES RequirementItem(ItemID) ON DELETE CASCADE,
    CONSTRAINT FK_ItemEquipment_Equipment FOREIGN KEY (EquipmentCode) REFERENCES Equipment(EquipmentCode) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目设备关联表';

-- =====================================================
-- 10. 设备关键词表 (EquipmentKeyword)
-- =====================================================
CREATE TABLE EquipmentKeyword (
    KeywordID INT AUTO_INCREMENT COMMENT '关键词ID',
    EquipmentCode CHAR(4) NOT NULL COMMENT '设备编码（外键）',
    Keyword VARCHAR(100) NOT NULL COMMENT '关键词',
    CreatedBy INT NOT NULL COMMENT '创建人ID',
    CreatedTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (KeywordID),
    INDEX IDX_EquipmentKeyword_EquipmentCode (EquipmentCode),
    INDEX IDX_EquipmentKeyword_Keyword (Keyword),
    CONSTRAINT FK_EquipmentKeyword_Equipment FOREIGN KEY (EquipmentCode) REFERENCES Equipment(EquipmentCode) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备关键词表';

-- =====================================================
-- 11. 错误报告表 (ErrorReport)
-- =====================================================
CREATE TABLE ErrorReport (
    ReportID INT AUTO_INCREMENT COMMENT '报告ID',
    ReportNo VARCHAR(50) NOT NULL COMMENT '报告编号（唯一）',
    StandardNo VARCHAR(50) NOT NULL COMMENT '标准号（外键）',
    ClauseNo VARCHAR(20) DEFAULT NULL COMMENT '条款号',
    ErrorDescription TEXT NOT NULL COMMENT '错误描述',
    Evidence TEXT DEFAULT NULL COMMENT '证据材料',
    ReporterID INT NOT NULL COMMENT '报告人ID（外键）',
    ReportTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报告时间',
    Status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-待处理，2-处理中，3-已完成，4-已驳回',
    HandlerID INT DEFAULT NULL COMMENT '处理人ID（外键）',
    HandleTime DATETIME DEFAULT NULL COMMENT '处理时间',
    HandleRemark TEXT DEFAULT NULL COMMENT '处理备注',
    PRIMARY KEY (ReportID),
    UNIQUE KEY UK_ErrorReport_ReportNo (ReportNo),
    INDEX IDX_ErrorReport_StandardNo (StandardNo),
    INDEX IDX_ErrorReport_Status (Status),
    CONSTRAINT FK_ErrorReport_Standard FOREIGN KEY (StandardNo) REFERENCES Standard(StandardNo) ON DELETE CASCADE,
    CONSTRAINT FK_ErrorReport_Reporter FOREIGN KEY (ReporterID) REFERENCES User(UserID),
    CONSTRAINT FK_ErrorReport_Handler FOREIGN KEY (HandlerID) REFERENCES User(UserID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='错误报告表';

-- =====================================================
-- 12. 自定义输出模板表 (ExportTemplate)
-- =====================================================
CREATE TABLE ExportTemplate (
    TemplateID INT AUTO_INCREMENT COMMENT '模板ID',
    TemplateName VARCHAR(100) NOT NULL COMMENT '模板名称',
    Description VARCHAR(500) DEFAULT NULL COMMENT '模板描述',
    IsPublic TINYINT NOT NULL DEFAULT 0 COMMENT '是否公开：0-私有，1-公开',
    CreatedBy INT NOT NULL COMMENT '创建人ID（外键）',
    CreatedTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdatedTime DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (TemplateID),
    INDEX IDX_ExportTemplate_CreatedBy (CreatedBy),
    CONSTRAINT FK_ExportTemplate_User FOREIGN KEY (CreatedBy) REFERENCES User(UserID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='自定义输出模板表';

-- =====================================================
-- 13. 模板字段配置表 (TemplateField)
-- =====================================================
CREATE TABLE TemplateField (
    FieldID INT AUTO_INCREMENT COMMENT '字段ID',
    TemplateID INT NOT NULL COMMENT '模板ID（外键）',
    FieldName VARCHAR(100) NOT NULL COMMENT '字段名称',
    FieldLabel VARCHAR(100) NOT NULL COMMENT '字段标签',
    FieldType VARCHAR(50) NOT NULL COMMENT '字段类型',
    IsRequired TINYINT NOT NULL DEFAULT 0 COMMENT '是否必填：0-否，1-是',
    SortOrder INT NOT NULL DEFAULT 0 COMMENT '排序号',
    CreatedTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (FieldID),
    INDEX IDX_TemplateField_TemplateID (TemplateID),
    CONSTRAINT FK_TemplateField_Template FOREIGN KEY (TemplateID) REFERENCES ExportTemplate(TemplateID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模板字段配置表';

-- =====================================================
-- 14. 产品技术要求表 (PTRDocument)
-- =====================================================
CREATE TABLE PTRDocument (
    DocumentID INT AUTO_INCREMENT COMMENT '文档ID',
    DocumentNo VARCHAR(50) NOT NULL COMMENT '文档编号（唯一）',
    ProductName VARCHAR(200) NOT NULL COMMENT '产品名称',
    Model VARCHAR(100) DEFAULT NULL COMMENT '型号规格',
    Content TEXT NOT NULL COMMENT '文档内容（富文本）',
    Status TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-草稿，2-审核中，3-已发布',
    CreatedBy INT NOT NULL COMMENT '创建人ID（外键）',
    CreatedTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UpdatedBy INT DEFAULT NULL COMMENT '更新人ID（外键）',
    UpdatedTime DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (DocumentID),
    UNIQUE KEY UK_PTRDocument_DocumentNo (DocumentNo),
    INDEX IDX_PTRDocument_Status (Status),
    CONSTRAINT FK_PTRDocument_Creator FOREIGN KEY (CreatedBy) REFERENCES User(UserID),
    CONSTRAINT FK_PTRDocument_Updater FOREIGN KEY (UpdatedBy) REFERENCES User(UserID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品技术要求表';

-- =====================================================
-- 15. 操作日志表 (OperationLog)
-- =====================================================
CREATE TABLE OperationLog (
    LogID BIGINT AUTO_INCREMENT COMMENT '日志ID',
    UserID INT NOT NULL COMMENT '用户ID（外键）',
    OperationType VARCHAR(50) NOT NULL COMMENT '操作类型',
    OperationModule VARCHAR(50) NOT NULL COMMENT '操作模块',
    OperationContent VARCHAR(500) NOT NULL COMMENT '操作内容',
    IPAddress VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
    OperationTime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (LogID),
    INDEX IDX_OperationLog_UserID (UserID),
    INDEX IDX_OperationLog_OperationTime (OperationTime),
    CONSTRAINT FK_OperationLog_User FOREIGN KEY (UserID) REFERENCES User(UserID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

