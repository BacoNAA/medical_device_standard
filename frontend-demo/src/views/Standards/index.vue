<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">标准浏览</h2>
      <p class="page-description">查询和浏览医疗器械标准内容</p>
    </div>
    
    <!-- 查询表单 -->
    <div class="query-form">
      <el-form :model="queryForm" inline>
        <el-form-item label="标准编号">
          <el-input v-model="queryForm.standardNo" placeholder="请输入标准编号" clearable style="width: 200px;" />
        </el-form-item>
        
        <el-form-item label="标准名称">
          <el-input v-model="queryForm.standardName" placeholder="请输入标准名称" clearable style="width: 200px;" />
        </el-form-item>
        
        <el-form-item label="关键词">
          <el-input v-model="queryForm.keyword" placeholder="请输入关键词" clearable style="width: 200px;" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon> 查询
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon> 重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 标准列表 -->
    <el-table 
      :data="filteredStandards" 
      stripe 
      border
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="standardNo" label="标准编号" width="180" />
      <el-table-column prop="standardName" label="标准名称" min-width="250" show-overflow-tooltip />
      <el-table-column prop="publishDate" label="发布日期" width="120" align="center" />
      <el-table-column prop="effectiveDate" label="实施日期" width="120" align="center" />
      <el-table-column prop="itemCount" label="项目数" width="100" align="center">
        <template #default="{ row }">
          <el-tag type="info">{{ row.itemCount }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="viewDetail(row.id)">
            <el-icon><View /></el-icon> 查看
          </el-button>
          <el-button type="danger" link @click="reportError(row)" v-if="userStore.roleCode !== 'sysadmin'">
            <el-icon><Warning /></el-icon> 报告错误
          </el-button>
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
        :total="filteredStandards.length"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useStandardsStore } from '@/stores/standards'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const standardsStore = useStandardsStore()
const userStore = useUserStore()

const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)

const queryForm = ref({
  standardNo: '',
  standardName: '',
  keyword: ''
})

// 过滤标准列表
const filteredStandards = computed(() => {
  let standards = standardsStore.standards
  
  if (queryForm.value.standardNo) {
    standards = standards.filter(s => 
      s.standardNo.toLowerCase().includes(queryForm.value.standardNo.toLowerCase())
    )
  }
  
  if (queryForm.value.standardName) {
    standards = standards.filter(s => 
      s.standardName.toLowerCase().includes(queryForm.value.standardName.toLowerCase())
    )
  }
  
  if (queryForm.value.keyword) {
    standards = standards.filter(s => 
      s.standardNo.toLowerCase().includes(queryForm.value.keyword.toLowerCase()) ||
      s.standardName.toLowerCase().includes(queryForm.value.keyword.toLowerCase())
    )
  }
  
  return standards
})

// 查询
const handleSearch = () => {
  loading.value = true
  setTimeout(() => {
    loading.value = false
    ElMessage.success('查询完成')
  }, 500)
}

// 重置
const handleReset = () => {
  queryForm.value = {
    standardNo: '',
    standardName: '',
    keyword: ''
  }
  handleSearch()
}

// 查看详情
const viewDetail = (id) => {
  router.push(`/standards/${id}`)
}

// 报告错误
const reportError = (row) => {
  ElMessage.info(`报告错误功能（Demo）：${row.standardNo}`)
}
</script>

