# VSCode 数据库连接指南

## 📋 目录

1. [安装MySQL扩展](#1-安装mysql扩展)
2. [连接数据库](#2-连接数据库)
3. [执行SQL脚本](#3-执行sql脚本)
4. [常用操作](#4-常用操作)
5. [故障排查](#5-故障排查)

---

## 1. 安装MySQL扩展

**推荐扩展：MySQL**

**扩展名称：** MySQL
**发布者：** cweijan
**扩展ID：** `cweijan.vscode-mysql-client2`

**安装步骤：**
1. 打开VSCode
2. 点击左侧扩展图标（或按 `Ctrl+Shift+X`）
3. 搜索 `MySQL cweijan`
4. 点击"安装"按钮

**特点：**
- ✅ 支持MySQL、PostgreSQL、SQL Server等多种数据库
- ✅ 可视化数据库管理
- ✅ SQL语法高亮和自动补全
- ✅ 支持导入/导出数据
- ✅ 支持ER图生成

---

## 2. 连接数据库

### 步骤1：打开数据库面板

安装扩展后，左侧会出现数据库图标（圆柱形），点击打开数据库面板。

### 步骤2：创建新连接

1. 点击数据库面板顶部的 `+` 号（或"New Connection"）
2. 选择 `MySQL`
3. 填写连接信息：

```
连接名称：医疗器械标准数据库（本地）
主机地址：localhost
端口：3306
用户名：root
密码：[您的MySQL密码]
数据库：medical_standard_db（可选，连接后再选择）
```

### 步骤3：测试连接

1. 点击"Test Connection"测试连接
2. 如果成功，点击"Connect"保存连接
3. 连接成功后，左侧会显示数据库列表

---

## 3. 执行SQL脚本

### 步骤1：创建数据库

1. 在VSCode中打开 `backend/src/main/resources/sql/01_create_database.sql`
2. 右键点击编辑器，选择 `Run MySQL Query`（或按 `Ctrl+Enter`）
3. 选择MySQL连接
4. 等待执行完成

### 步骤2：创建数据表

1. 打开 `backend/src/main/resources/sql/02_create_tables.sql`
2. 右键点击编辑器，选择 `Run MySQL Query`
3. 等待执行完成

### 步骤3：插入初始数据

1. 打开 `backend/src/main/resources/sql/03_insert_init_data.sql`
2. 右键点击编辑器，选择 `Run MySQL Query`
3. 等待执行完成

---

## 4. 常用操作

### 4.1 查看表结构

1. 在数据库面板中展开 `medical_standard_db`
2. 展开 `Tables` 节点
3. 右键点击表名，选择 `Show Table Info`

### 4.2 查看表数据

1. 右键点击表名
2. 选择 `Show Table Data`
3. 可以直接编辑数据（双击单元格）

### 4.3 执行SQL查询

1. 在数据库面板中右键点击数据库
2. 选择 `New Query`
3. 输入SQL语句
4. 按 `Ctrl+Enter` 执行

### 4.4 导出数据

1. 右键点击表名
2. 选择 `Export Data`
3. 选择导出格式（SQL、CSV、JSON等）

### 4.5 生成ER图

1. 右键点击数据库名
2. 选择 `Generate ER Diagram`
3. 查看数据库关系图

---

## 5. 故障排查

### 问题1：无法连接到MySQL

**可能原因：**
- MySQL服务未启动
- 端口号错误（默认3306）
- 用户名或密码错误
- 防火墙阻止连接

**解决方法：**
```bash
# Windows - 检查MySQL服务状态
net start MySQL80

# 如果未启动，启动服务
net start MySQL80

# 测试连接
mysql -u root -p
```

### 问题2：执行SQL脚本报错

**常见错误：**
- `Access denied` - 权限不足
- `Unknown database` - 数据库不存在
- `Syntax error` - SQL语法错误

**解决方法：**
1. 确保使用root用户或有足够权限的用户
2. 先执行 `01_create_database.sql` 创建数据库
3. 检查SQL语法是否正确

### 问题3：中文乱码

**解决方法：**
```sql
-- 检查数据库字符集
SHOW VARIABLES LIKE 'character%';

-- 如果不是utf8mb4，修改配置文件 my.ini（Windows）或 my.cnf（Linux）
[mysqld]
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci

[client]
default-character-set=utf8mb4
```

### 问题4：SOURCE命令不支持

**解决方法：**
- 使用MySQL命令行执行
- 或者分别执行3个SQL脚本

---

## 📌 快速参考

### 默认账号信息

| 用户名 | 密码 | 角色 |
|--------|------|------|
| sysadmin | 123456 | 系统管理员 |
| dataadmin | 123456 | 数据输入员 |
| yw001 | 123456 | 业务人员 |
| zl001 | 123456 | 质量管理人员 |
| sb001 | 123456 | 设备管理人员 |
| sy001 | 123456 | 实验室人员 |

### 数据库信息

- **数据库名：** medical_standard_db
- **字符集：** utf8mb4
- **排序规则：** utf8mb4_unicode_ci
- **表数量：** 8个核心表

### SQL脚本位置

```
backend/src/main/resources/sql/
├── 00_init_all.sql          # 一键初始化（包含所有步骤）
├── 01_create_database.sql   # 创建数据库
├── 02_create_tables.sql     # 创建数据表
└── 03_insert_init_data.sql  # 插入初始数据
```

---

## ✅ 验证安装

执行以下SQL验证数据库是否正确初始化：

```sql
-- 1. 检查数据库
SHOW DATABASES LIKE 'medical_standard_db';

-- 2. 检查表
USE medical_standard_db;
SHOW TABLES;

-- 3. 检查数据
SELECT COUNT(*) AS '用户数' FROM `user`;
SELECT COUNT(*) AS '标准数' FROM `standard`;
SELECT COUNT(*) AS '检验项目数' FROM `requirement_item`;
SELECT COUNT(*) AS '设备数' FROM `equipment`;

-- 4. 测试登录
SELECT * FROM `user` WHERE username = 'sysadmin';
```

**预期结果：**
- 用户数：6
- 标准数：2
- 检验项目数：8
- 设备数：9

---

**创建时间：** 2025-11-14  
**版本：** v1.0

