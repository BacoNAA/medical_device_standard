-- =====================================================
-- 医疗器械标准数据库 - 初始数据插入脚本
-- 严格按照设计文档 v2.0 实现
-- =====================================================

USE medical_device_standards;

-- =====================================================
-- 1. 插入系统管理员账号（初始账号）
-- =====================================================
-- 系统管理员（sysadmin）- 密码：123456（bcrypt加密）
INSERT INTO User (Username, Password, RealName, Phone, Email, Department, RoleType, CreatedBy) VALUES
('sysadmin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHCYfOOe0Bq0q5YtRCJ7CO', '系统管理员', '13800000000', 'admin@example.com', '信息技术部', 2, 1);

-- 数据输入员（dataadmin）- 密码：123456
INSERT INTO User (Username, Password, RealName, Phone, Email, Department, RoleType, CreatedBy) VALUES
('dataadmin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHCYfOOe0Bq0q5YtRCJ7CO', '数据录入员', '13800000001', 'data@example.com', '数据管理部', 1, 1);

-- 业务人员（yw001）- 密码：123456
INSERT INTO User (Username, Password, RealName, Phone, Email, Department, RoleType, CreatedBy) VALUES
('yw001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHCYfOOe0Bq0q5YtRCJ7CO', '张业务', '13800000002', 'yw001@example.com', '业务部', 3, 1);

-- 质量管理人员（zl001）- 密码：123456
INSERT INTO User (Username, Password, RealName, Phone, Email, Department, RoleType, CreatedBy) VALUES
('zl001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHCYfOOe0Bq0q5YtRCJ7CO', '李质量', '13800000003', 'zl001@example.com', '质量管理部', 4, 1);

-- 设备管理人员（sb001）- 密码：123456
INSERT INTO User (Username, Password, RealName, Phone, Email, Department, RoleType, CreatedBy) VALUES
('sb001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHCYfOOe0Bq0q5YtRCJ7CO', '王设备', '13800000004', 'sb001@example.com', '设备管理部', 5, 1);

-- 实验室人员（sy001）- 密码：123456
INSERT INTO User (Username, Password, RealName, Phone, Email, Department, RoleType, CreatedBy) VALUES
('sy001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHCYfOOe0Bq0q5YtRCJ7CO', '赵实验', '13800000005', 'sy001@example.com', '实验室', 6, 1);

-- =====================================================
-- 2. 插入测试标准数据（GB 15810-2019）
-- =====================================================
INSERT INTO Standard (StandardNo, StandardName, StandardNameEN, ISOReference, PublishDate, EffectiveDate, Scope, CreatedBy) VALUES
('GB 15810-2019', '一次性使用无菌注射器', 'Sterile hypodermic syringes for single use', 'ISO 7886-1:2017', '2019-08-30', '2020-09-01', '本标准规定了一次性使用无菌注射器的要求、试验方法、标志、包装和贮存。本标准适用于一次性使用无菌注射器。', 1);

-- =====================================================
-- 3. 插入测试检验项目数据
-- =====================================================
-- 外观检查
INSERT INTO RequirementItem (StandardNo, ClauseNo, ItemName, Requirement, MethodType, SortOrder) VALUES
('GB 15810-2019', '5.1', '外观', '注射器应清洁，无可见异物。', 1, 1);

-- 标称容量
INSERT INTO RequirementItem (StandardNo, ClauseNo, ItemName, Requirement, MethodRef, MethodType, SortOrder) VALUES
('GB 15810-2019', '5.2', '标称容量', '注射器的标称容量应符合表1的规定。', '见6.2', 2, 2);

-- 残留容量
INSERT INTO RequirementItem (StandardNo, ClauseNo, ItemName, Requirement, MethodRef, MethodType, SortOrder) VALUES
('GB 15810-2019', '5.3', '残留容量', '残留容量应不大于表2的规定值。', '见6.3', 2, 3);

-- 滑动性能
INSERT INTO RequirementItem (StandardNo, ClauseNo, ItemName, Requirement, MethodRef, MethodType, SortOrder) VALUES
('GB 15810-2019', '5.4', '滑动性能', '滑动性能应符合表3的规定。', '见6.4', 2, 4);

-- =====================================================
-- 4. 插入测试设备数据
-- =====================================================
-- 游标卡尺
INSERT INTO Equipment (EquipmentCode, EquipmentName, Model, Description, CreatedBy) VALUES
('A001', '游标卡尺', '0-150mm', '用于测量注射器外形尺寸', 1);

-- 电子天平
INSERT INTO Equipment (EquipmentCode, EquipmentName, Model, Description, CreatedBy) VALUES
('A002', '电子天平', '0.1mg', '用于称量残留容量', 1);

-- 拉力试验机
INSERT INTO Equipment (EquipmentCode, EquipmentName, Model, Description, CreatedBy) VALUES
('A003', '拉力试验机', '0-500N', '用于测试滑动性能', 1);

-- =====================================================
-- 5. 插入设备关键词
-- =====================================================
INSERT INTO EquipmentKeyword (EquipmentCode, Keyword, CreatedBy) VALUES
('A001', '卡尺', 1);

INSERT INTO EquipmentKeyword (EquipmentCode, Keyword, CreatedBy) VALUES
('A001', '游标', 1);

INSERT INTO EquipmentKeyword (EquipmentCode, Keyword, CreatedBy) VALUES
('A002', '天平', 1);

INSERT INTO EquipmentKeyword (EquipmentCode, Keyword, CreatedBy) VALUES
('A002', '称量', 1);

INSERT INTO EquipmentKeyword (EquipmentCode, Keyword, CreatedBy) VALUES
('A003', '拉力', 1);

INSERT INTO EquipmentKeyword (EquipmentCode, Keyword, CreatedBy) VALUES
('A003', '试验机', 1);

