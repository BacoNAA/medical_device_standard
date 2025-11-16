# 数据库初始化脚本

严格按照设计文档 v2.0 实现，使用 PascalCase 命名规范。

## 脚本列表

| 脚本文件 | 说明 |
|---------|------|
| `01_create_database.sql` | 创建数据库 `medical_device_standards` |
| `02_create_tables.sql` | 创建15个数据表（PascalCase命名） |
| `03_insert_init_data.sql` | 插入初始数据（6个测试账号） |

## 执行方式

使用 VSCode MySQL 扩展按顺序执行：

1. 右键 `01_create_database.sql` → Run MySQL Query
2. 右键 `02_create_tables.sql` → Run MySQL Query
3. 右键 `03_insert_init_data.sql` → Run MySQL Query

## 数据库结构

**数据库名：** `medical_device_standards`  
**字符集：** `utf8mb4`  
**排序规则：** `utf8mb4_unicode_ci`  
**命名规范：** PascalCase（表名、字段名）

### 15个数据表

1. **User** - 用户表
2. **Standard** - 标准信息表
3. **RequirementItem** - 检验项目表
4. **TermDefinition** - 术语定义表
5. **Clause** - 条款信息表
6. **Method** - 检验方法表
7. **Appendix** - 附录表
8. **Equipment** - 设备信息表
9. **ItemEquipment** - 项目设备关联表
10. **EquipmentKeyword** - 设备关键词表
11. **ErrorReport** - 错误报告表
12. **ExportTemplate** - 自定义输出模板表
13. **TemplateField** - 模板字段配置表
14. **PTRDocument** - 产品技术要求表
15. **OperationLog** - 操作日志表

## 默认账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| sysadmin | 123456 | 系统管理员 |
| dataadmin | 123456 | 数据输入员 |
| yw001 | 123456 | 业务人员 |
| zl001 | 123456 | 质量管理人员 |
| sb001 | 123456 | 设备管理人员 |
| sy001 | 123456 | 实验室人员 |

## 验证初始化

```sql
USE medical_device_standards;

-- 查看所有表
SHOW TABLES;

-- 统计数据
SELECT 
    (SELECT COUNT(*) FROM User) AS '用户数',
    (SELECT COUNT(*) FROM Standard) AS '标准数',
    (SELECT COUNT(*) FROM RequirementItem) AS '检验项目数',
    (SELECT COUNT(*) FROM Equipment) AS '设备数';
```

预期结果：用户数=6，标准数=1，检验项目数=4，设备数=3

