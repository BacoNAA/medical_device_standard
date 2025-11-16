<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">错误报告</h2>
      <p class="page-description">提交和查看标准内容错误报告</p>
    </div>
    
    <!-- 操作按钮 -->
    <div class="table-actions">
      <el-button type="primary" @click="showSubmitDialog" v-if="userStore.roleCode !== 'sysadmin'">
        <el-icon><Plus /></el-icon> 提交错误报告
      </el-button>
      
      <el-radio-group v-model="statusFilter" @change="handleFilterChange">
        <el-radio-button label="all">全部</el-radio-button>
        <el-radio-button label="pending">待处理</el-radio-button>
        <el-radio-button label="confirmed">已确认</el-radio-button>
        <el-radio-button label="resolved">已修正</el-radio-button>
        <el-radio-button label="rejected">已驳回</el-radio-button>
      </el-radio-group>
    </div>
    
    <!-- 报告列表 -->
    <el-table :data="filteredReports" border stripe>
      <el-table-column prop="reportNo" label="报告编号" width="150" align="center" />
      <el-table-column prop="standardNo" label="标准编号" width="150" />
      <el-table-column prop="clauseNo" label="项目条款" width="100" align="center" />
      <el-table-column prop="description" label="错误描述" min-width="250" show-overflow-tooltip />
      <el-table-column prop="reporter" label="报告人" width="100" align="center" />
      <el-table-column prop="reportTime" label="报告时间" width="160" />
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="viewDetail(row)">查看</el-button>
          <el-button 
            type="success" 
            link 
            @click="handleReport(row)" 
            v-if="userStore.roleCode === 'dataadmin' && row.status === 0"
          >
            处理
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 提交报告对话框 -->
    <el-dialog v-model="submitDialogVisible" title="提交错误报告" width="600px">
      <el-form :model="reportForm" label-width="100px">
        <el-form-item label="标准编号" required>
          <el-select v-model="reportForm.standardId" placeholder="请选择标准" style="width: 100%;">
            <el-option 
              v-for="std in standards" 
              :key="std.id" 
              :label="`${std.standardNo} ${std.standardName}`"
              :value="std.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="项目条款">
          <el-input v-model="reportForm.clauseNo" placeholder="如：5.7.1（可选）" />
        </el-form-item>
        
        <el-form-item label="错误描述" required>
          <el-input 
            v-model="reportForm.description" 
            type="textarea" 
            :rows="4"
            placeholder="请详细描述发现的错误内容"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="submitDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitReport">提交</el-button>
      </template>
    </el-dialog>
    
    <!-- 报告详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="错误报告详情" width="700px">
      <el-descriptions :column="1" border v-if="currentReport">
        <el-descriptions-item label="报告编号">{{ currentReport.reportNo }}</el-descriptions-item>
        <el-descriptions-item label="标准编号">{{ currentReport.standardNo }}</el-descriptions-item>
        <el-descriptions-item label="项目条款">{{ currentReport.clauseNo || '无' }}</el-descriptions-item>
        <el-descriptions-item label="错误描述">
          <div style="white-space: pre-wrap;">{{ currentReport.description }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="报告人">{{ currentReport.reporter }}</el-descriptions-item>
        <el-descriptions-item label="报告时间">{{ currentReport.reportTime }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(currentReport.status)">
            {{ getStatusText(currentReport.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="处理备注" v-if="currentReport.handleRemark">
          <div style="white-space: pre-wrap;">{{ currentReport.handleRemark }}</div>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
    
    <!-- 处理报告对话框 -->
    <el-dialog v-model="handleDialogVisible" title="处理错误报告" width="500px">
      <el-form label-width="100px">
        <el-form-item label="处理方式">
          <el-radio-group v-model="handleAction">
            <el-radio label="resolve">修正错误</el-radio>
            <el-radio label="reject">驳回</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="处理备注">
          <el-input type="textarea" :rows="3" v-model="handleRemark" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="handleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmHandle">确认处理</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

// Mock标准数据
const mockStandards = [
  { id: 1, standardNo: 'GB 15810-2019', standardName: '一次性使用无菌注射器' },
  { id: 2, standardNo: 'YY 0469-2023', standardName: '医用外科口罩' }
]

const userStore = useUserStore()

const statusFilter = ref('all')
const submitDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const handleDialogVisible = ref(false)
const currentReport = ref(null)
const handleAction = ref('resolve')
const handleRemark = ref('')

const reportForm = ref({
  standardId: null,
  clauseNo: '',
  description: ''
})

const reports = ref([
  {
    id: 1,
    reportNo: 'ER202510210001',
    standardNo: 'GB 15810-2019',
    clauseNo: '5.7.1',
    description: '残留容量的要求描述有误，应为"不超过0.1mL"',
    reporter: 'yw001',
    reportTime: '2025-10-21 10:30:00',
    status: 0
  },
  {
    id: 2,
    reportNo: 'ER202510200002',
    standardNo: 'YY 0469-2023',
    clauseNo: '5.2',
    description: '细菌过滤效率测试方法引用错误',
    reporter: 'sy001',
    reportTime: '2025-10-20 15:20:00',
    status: 2,
    handleRemark: '已修正，感谢反馈'
  }
])

const standards = computed(() => mockStandards)

const filteredReports = computed(() => {
  if (statusFilter.value === 'all') return reports.value
  
  const statusMap = {
    pending: 0,
    confirmed: 1,
    resolved: 2,
    rejected: 3
  }
  
  return reports.value.filter(r => r.status === statusMap[statusFilter.value])
})

const getStatusType = (status) => {
  const types = ['warning', 'info', 'success', 'danger']
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = ['待处理', '已确认', '已修正', '已驳回']
  return texts[status] || '未知'
}

const handleFilterChange = () => {
  // 筛选变化
}

const showSubmitDialog = () => {
  reportForm.value = {
    standardId: null,
    clauseNo: '',
    description: ''
  }
  submitDialogVisible.value = true
}

const submitReport = () => {
  if (!reportForm.value.standardId || !reportForm.value.description) {
    ElMessage.warning('请填写必填项')
    return
  }
  
  const standard = standardsStore.getStandardById(reportForm.value.standardId)
  
  reports.value.unshift({
    id: reports.value.length + 1,
    reportNo: `ER${Date.now()}`,
    standardNo: standard.standardNo,
    clauseNo: reportForm.value.clauseNo,
    description: reportForm.value.description,
    reporter: userStore.username,
    reportTime: new Date().toLocaleString(),
    status: 0
  })
  
  submitDialogVisible.value = false
  ElMessage.success('错误报告已提交')
}

const viewDetail = (report) => {
  currentReport.value = report
  detailDialogVisible.value = true
}

const handleReport = (report) => {
  currentReport.value = report
  handleAction.value = 'resolve'
  handleRemark.value = ''
  handleDialogVisible.value = true
}

const confirmHandle = () => {
  if (currentReport.value) {
    currentReport.value.status = handleAction.value === 'resolve' ? 2 : 3
    currentReport.value.handleRemark = handleRemark.value
    
    ElMessage.success('处理完成')
    handleDialogVisible.value = false
  }
}
</script>

