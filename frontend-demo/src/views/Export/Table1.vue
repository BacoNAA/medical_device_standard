<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">能力建设信息网站项目表（表1）</h2>
      <p class="page-description">导出质量管理所需的项目清单表</p>
    </div>
    
    <!-- 筛选条件 -->
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <el-form :model="filterForm" inline>
        <el-form-item label="选择标准">
          <el-select v-model="filterForm.standardId" placeholder="请选择标准" style="width: 300px;">
            <el-option 
              v-for="std in standards" 
              :key="std.id" 
              :label="`${std.standardNo} ${std.standardName}`"
              :value="std.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="项目名称">
          <el-input v-model="filterForm.itemName" placeholder="筛选项目名称" clearable style="width: 200px;" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="loadData">
            <el-icon><Refresh /></el-icon> 刷新数据
          </el-button>
          <el-button type="success" @click="exportExcel">
            <el-icon><Download /></el-icon> 导出Excel
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 预览表格 -->
    <el-card shadow="hover">
      <template #header>
        <span style="font-weight: bold;">数据预览</span>
      </template>
      
      <el-table :data="tableData" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="itemName" label="项目名称" width="200" />
        <el-table-column prop="standardNo" label="标准编号" width="180" />
        <el-table-column prop="clauseNo" label="项目条款" width="120" align="center" />
      </el-table>
      
      <div style="margin-top: 15px; text-align: right; color: #999;">
        共 {{ tableData.length }} 条记录
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'

// Mock标准数据
const mockStandards = [
  {
    id: 1,
    standardNo: 'GB 15810-2019',
    standardName: '一次性使用无菌注射器',
    items: [
      { itemId: 1, itemName: '外观', clauseNo: '5.1' },
      { itemId: 2, itemName: '残留容量', clauseNo: '5.7.1' },
      { itemId: 3, itemName: '器身密合性', clauseNo: '5.7.2' },
      { itemId: 4, itemName: '密封性', clauseNo: '5.3' }
    ]
  },
  {
    id: 2,
    standardNo: 'YY 0469-2023',
    standardName: '医用外科口罩',
    items: [
      { itemId: 5, itemName: '外观', clauseNo: '5.1' },
      { itemId: 6, itemName: '细菌过滤效率', clauseNo: '5.2' }
    ]
  }
]

const filterForm = ref({
  standardId: null,
  itemName: ''
})

const standards = computed(() => mockStandards)

const tableData = computed(() => {
  if (!filterForm.value.standardId) return []
  
  const standard = mockStandards.find(s => s.id === filterForm.value.standardId)
  if (!standard) return []
  
  let items = standard.items.map(item => ({
    itemName: item.itemName,
    standardNo: standard.standardNo,
    clauseNo: item.clauseNo
  }))
  
  if (filterForm.value.itemName) {
    items = items.filter(item => item.itemName.includes(filterForm.value.itemName))
  }
  
  return items
})

const loadData = () => {
  ElMessage.success('数据已刷新')
}

const exportExcel = () => {
  if (tableData.value.length === 0) {
    ElMessage.warning('没有数据可导出')
    return
  }
  
  ElMessage.success(`已导出 ${tableData.value.length} 条记录（Demo）`)
}
</script>

