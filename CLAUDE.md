# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a project management repository for a **Medical Device Standards Database and Application System** (医疗器械标准数据库与应用系统). The project follows the waterfall model with comprehensive documentation and a Vue 3 frontend demo.

**Project Structure:**
- `project_document/` - All project management documents (requirements, WBS, staffing plans, Gantt charts)
- `frontend-demo/` - Vue 3 + Element Plus frontend demonstration
- Root directory - Contains PDF versions of key deliverables and demo resources

## Key Commands

### Frontend Demo Development

**Prerequisites:**
- Node.js 16+
- npm or yarn

**Install dependencies:**
```bash
cd frontend-demo
npm install
```

**Start development server:**
```bash
cd frontend-demo
npm run dev
# Server runs on http://localhost:3000
```

**Alternative (Windows):**
```bash
cd frontend-demo
启动开发服务器.bat
```

**Build for production:**
```bash
cd frontend-demo
npm run build
```

**Preview production build:**
```bash
cd frontend-demo
npm run preview
```

### Project Documentation Generation

**Generate Gantt chart (requires Python 3 + openpyxl):**
```bash
cd project_document
python create_gantt_chart.py
# Generates 项目甘特图 v1.0.xlsx
```

**Version-specific Gantt charts:**
```bash
cd project_document
python "create_gantt_chart 1104.py"  # For 1104 progress version
python "create_gantt_chart 1109.py"  # For 1109 progress version
```

## Architecture & Design

### Project Management Approach

**Development Model:** Waterfall with 5 phases
1. **Requirements Analysis (需求分析阶段)** - Requirements gathering, SRS documentation
2. **System Design (系统设计阶段)** - Architecture, database, and UI/UX design
3. **Implementation (实现编码阶段)** - 6 major modules development
4. **Testing & Verification (测试验证阶段)** - Unit, integration, system, UAT testing
5. **Deployment & Maintenance (部署与维护阶段)** - Deployment and ongoing support

**Team Structure (10 members):**
- 1 Project Manager (PM) - 郑鹏哲
- 1 UI/UX Designer - 陆治维
- 3 Frontend Developers - 陈浩, 王欣妍, 朱俊豪
- 3 Backend Developers - 朱家培, 王砺成, 孙峻宇
- 2 QA/Testing - 陈仕安, 谢江伦

### System Architecture

**Technology Stack:**
- **Frontend:** Vue 3 (Composition API), Element Plus, Vue Router, Pinia
- **Backend:** (Planned) B/S architecture with REST API
- **Database:** (Planned) Relational database for structured medical standards data
- **Build Tool:** Vite

**Core Functional Modules (6 modules):**
1. **User Management (用户管理)** - Authentication, role-based access (6 roles)
2. **Standards Data Management (标准数据管理)** - Browse, search, import medical device standards
3. **Equipment Management (设备管理)** - Equipment catalog with keyword-based recognition
4. **Error Reporting (错误报告)** - Standards error tracking and correction workflow
5. **Data Export (数据导出)** - 3 predefined table formats + custom export templates
6. **PTR Editor (产品技术要求编辑)** - Rich text editor with intelligent content suggestion

### Frontend Demo Architecture

**Directory Structure:**
```
frontend-demo/src/
├── assets/          # Global styles (style.css)
├── layout/          # Main application layout (top nav + sidebar)
├── router/          # Vue Router configuration
├── stores/          # Pinia state management (user, standards)
├── mock/            # Mock data for demonstration
└── views/           # Page components organized by module
    ├── Standards/   # Standards list and detail pages
    ├── Import/      # Standards import workflow
    ├── Equipment/   # Equipment management
    ├── Export/      # Table1/2/3 + custom export
    ├── PTREditor/   # Product Technical Requirements editor
    ├── ErrorReports/# Error tracking system
    └── System/      # Users, Logs, Settings (admin functions)
```

**Role-Based Access Control:**
The system uses username format to automatically determine roles:
- `dataadmin` - Data Entry Personnel (数据输入员)
- `sysadmin` - System Administrator (系统管理员)
- `yw###` - Business Personnel (业务人员)
- `zl###` - Quality Management (质量管理人员)
- `sb###` - Equipment Management (设备管理人员)
- `sy###` - Lab Personnel (实验室人员)

**State Management Pattern:**
- User authentication state stored in `stores/user.js` with localStorage persistence
- Standards and equipment data in `stores/standards.js`
- Mock data provides 2 complete standards (GB 15810-2019, YY 0469-2023) with 42 inspection items

**Key Design Patterns:**
- **Semi-automated Import:** Excel/Word import with color-coded validation (green=success, yellow=needs confirmation, red=needs manual input)
- **Equipment Catalog Design:** One record per equipment type; specific specifications stored in item-equipment associations
- **Method Type Classification:** 3 types - (1) embedded in requirements, (2) references appendix, (3) complete method content
- **Intelligent PTR Editing:** Template insertion + keyword-based content suggestion from standards database

## Important Conventions

### Document Versioning
- All project documents use semantic versioning (v1.0, v2.0, etc.)
- Latest versions: Requirements v4.0, WBS v3.0, Staffing Plan v3.0
- Update history tracked in document footer tables

### Code Style (Frontend)
- **Vue 3 Composition API:** Use `<script setup>` syntax exclusively
- **Component Naming:** PascalCase for files (e.g., `StandardDetail.vue`)
- **State Management:** Use `ref` and `computed` from Vue, Pinia for shared state
- **UI Components:** Element Plus component library for consistent design
- **Colors:** Professional blue theme (#4472C4 primary, status colors for success/warning/error)

### Data Structures
- **Standards:** Must include standard number with year (e.g., "GB 15810-2019")
- **Equipment Codes:** Format `[A-Z]\d{3}` (e.g., A001, B002)
- **Method Types:** 1 (no independent method), 2 (references appendix), 3 (complete method)
- **Import Status:** 'success', 'warning', 'error' for validation results

### File Naming
- Project documents: Chinese names with version suffix (e.g., `需求规格说明书-医疗器械标准数据库与应用系统_v4.0.md`)
- Code files: English names following Vue/JavaScript conventions
- Generated artifacts: Include date/version in filename (e.g., `项目甘特图_1109进度.xlsx`)

## Testing Approach

The project follows a comprehensive testing strategy:
1. **Module Testing** - Individual feature testing per module
2. **Integration Testing** - Frontend-backend API integration, module interface testing
3. **System Testing** - End-to-end business flow testing
4. **Performance Testing** - Response time, concurrency, data accuracy
5. **UAT** - Client acceptance testing with real scenarios

## Important Notes

### Frontend Demo Limitations
- **Pure frontend only** - No actual backend, uses mock data
- **Data persistence** - Only login state persists (localStorage), other data is in-memory
- **File operations** - Export/import functions are simulated, don't generate real files
- **Page refresh** - Resets all data except login state

### Communication Channels (Team Context)
- **Daily sync:** QQ group, 3-7 days interval
- **Weekly meetings:** Progress review, task planning
- **Code repository:** Gitee/GitHub for version control
- **Documentation:** Tencent Docs/Yuque for collaboration
- **Design tools:** Figma/墨刀 for UI prototypes

### Client Information
- **Client:** 秦黎 (名茜生物)
- **Supervisor:** 余立功
- **Target users:** Medical device testing labs, quality management personnel

## Working with This Repository

**When updating requirements documentation:**
1. Update the markdown file in `project_document/`
2. Maintain version history table at document end
3. Generate PDF if needed for formal delivery
4. Update corresponding WBS/staffing docs if scope changes

**When modifying frontend demo:**
1. All Vue components should use Composition API (`<script setup>`)
2. Keep mock data in `src/mock/standardData.js` synchronized with UI changes
3. Test with different role accounts to verify permission controls
4. Maintain responsive design for different screen sizes

**When generating project plans:**
1. Python scripts in `project_document/` generate Excel Gantt charts
2. Ensure openpyxl is installed: `pip install openpyxl`
3. Scripts reference WBS v3.0 structure with 133 work packages
4. Update date ranges in script if project timeline changes

**Browser compatibility:**
- Chrome 90+
- Edge 90+
- Firefox 88+

