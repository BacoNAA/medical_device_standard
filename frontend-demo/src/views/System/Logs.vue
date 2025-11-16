<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">操作日志</h2>
      <p class="page-description">查询系统操作日志记录</p>
    </div>
    
    <!-- 查询条件 -->
    <div class="query-form">
      <el-form :model="queryForm" inline>
        <el-form-item label="操作时间">
          <el-date-picker
            v-model="queryForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 300px;"
          />
        </el-form-item>
        
        <el-form-item label="操作人">
          <el-input v-model="queryForm.operator" placeholder="请输入操作人" clearable style="width: 150px;" />
        </el-form-item>
        
        <el-form-item label="操作类型">
          <el-select v-model="queryForm.operationType" placeholder="请选择" clearable style="width: 150px;">
            <el-option label="用户登录" value="login" />
            <el-option label="数据导入" value="import" />
            <el-option label="数据修改" value="edit" />
            <el-option label="数据导出" value="export" />
            <el-option label="文档创建" value="create" />
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon> 查询
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon> 重置
          </el-button>
          <el-button @click="exportLogs">
            <el-icon><Download /></el-icon> 导出
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 日志列表 -->
    <el-table :data="logs" border stripe>
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="operationTime" label="操作时间" width="180" />
      <el-table-column prop="operator" label="操作人" width="120" align="center" />
      <el-table-column prop="operationType" label="操作类型" width="120" align="center">
        <template #default="{ row }">
          <el-tag :type="getOperationType(row.operationType).type">
            {{ getOperationType(row.operationType).text }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="operationTarget" label="操作对象" width="200" show-overflow-tooltip />
      <el-table-column prop="operationDetail" label="操作详情" min-width="250" show-overflow-tooltip />
      <el-table-column prop="ipAddress" label="IP地址" width="130" align="center" />
      <el-table-column label="操作" width="100" align="center">
        <template #default="{ row }">
          <el-button type="primary" link @click="viewDetail(row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 分页 -->
    <div style="margin-top: 20px; text-align: right;">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="100"
      />
    </div>
    
    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="操作日志详情" width="600px">
      <el-descriptions :column="1" border v-if="currentLog">
        <el-descriptions-item label="操作时间">{{ currentLog.operationTime }}</el-descriptions-item>
        <el-descriptions-item label="操作人">{{ currentLog.operator }}</el-descriptions-item>
        <el-descriptions-item label="操作类型">{{ getOperationType(currentLog.operationType).text }}</el-descriptions-item>
        <el-descriptions-item label="操作对象">{{ currentLog.operationTarget }}</el-descriptions-item>
        <el-descriptions-item label="操作详情">
          <div style="white-space: pre-wrap;">{{ currentLog.operationDetail }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="IP地址">{{ currentLog.ipAddress }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const queryForm = ref({
  dateRange: null,
  operator: '',
  operationType: ''
})

const currentPage = ref(1)
const pageSize = ref(10)
const detailVisible = ref(false)
const currentLog = ref(null)

const logs = ref([
  {
    id: 1,
    operationTime: '2025-10-21 10:30:15',
    operator: 'dataadmin',
    operationType: 'import',
    operationTarget: 'GB 15810-2019',
    operationDetail: '导入标准数据，共24个检验项目',
    ipAddress: '192.168.1.100'
  },
  {
    id: 2,
    operationTime: '2025-10-21 09:15:22',
    operator: 'yw001',
    operationType: 'export',
    operationTarget: '产品技术要求_一次性使用无菌注射器',
    operationDetail: '导出产品技术要求Word文档',
    ipAddress: '192.168.1.105'
  },
  {
    id: 3,
    operationTime: '2025-10-21 08:45:10',
    operator: 'sysadmin',
    operationType: 'login',
    operationTarget: '系统登录',
    operationDetail: '用户登录系统',
    ipAddress: '192.168.1.88'
  },
  {
    id: 4,
    operationTime: '2025-10-20 16:20:33',
    operator: 'dataadmin',
    operationType: 'edit',
    operationTarget: 'GB 15810-2019 条款5.7.1',
    operationDetail: '修正残留容量要求描述',
    ipAddress: '192.168.1.100'
  }
])

const getOperationType = (type) => {
  const types = {
    login: { text: '用户登录', type: 'success' },
    import: { text: '数据导入', type: 'primary' },
    edit: { text: '数据修改', type: 'warning' },
    export: { text: '数据导出', type: 'info' },
    create: { text: '文档创建', type: '' }
  }
  return types[type] || { text: type, type: 'info' }
}

const handleSearch = () => {
  ElMessage.success('查询完成')
}

const handleReset = () => {
  queryForm.value = {
    dateRange: null,
    operator: '',
    operationType: ''
  }
}

const exportLogs = () => {
  ElMessage.success('日志已导出（Demo）')
}

const viewDetail = (log) => {
  currentLog.value = log
  detailVisible.value = true
}
</script>

