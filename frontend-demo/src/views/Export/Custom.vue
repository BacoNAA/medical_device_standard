<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">自定义导出</h2>
      <p class="page-description">创建个性化的导出模板，灵活配置字段和格式</p>
    </div>
    
    <el-row :gutter="20">
      <!-- 左侧：模板列表 -->
      <el-col :span="8">
        <el-card shadow="hover">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span style="font-weight: bold;">我的模板</span>
              <el-button type="primary" size="small" @click="showCreateDialog">
                <el-icon><Plus /></el-icon> 新建
              </el-button>
            </div>
          </template>
          
          <el-menu :default-active="activeTemplate" @select="selectTemplate">
            <el-menu-item index="preset1">
              <el-icon><Document /></el-icon>
              <span>能力建设信息表</span>
            </el-menu-item>
            <el-menu-item index="preset2">
              <el-icon><Document /></el-icon>
              <span>认可检验能力表</span>
            </el-menu-item>
            <el-menu-item index="preset3">
              <el-icon><Document /></el-icon>
              <span>操作规程表</span>
            </el-menu-item>
            <el-divider />
            <el-menu-item index="custom1">
              <el-icon><Star /></el-icon>
              <span>我的自定义模板1</span>
            </el-menu-item>
          </el-menu>
        </el-card>
      </el-col>
      
      <!-- 右侧：导出配置 -->
      <el-col :span="16">
        <el-card shadow="hover" style="margin-bottom: 20px;">
          <template #header>
            <span style="font-weight: bold;">导出配置</span>
          </template>
          
          <el-form :model="exportForm" label-width="100px">
            <el-form-item label="选择标准">
              <el-select v-model="exportForm.standardId" placeholder="请选择标准" style="width: 100%;">
                <el-option 
                  v-for="std in standards" 
                  :key="std.id" 
                  :label="`${std.standardNo} ${std.standardName}`"
                  :value="std.id"
                />
              </el-select>
            </el-form-item>
            
            <el-form-item label="筛选条件">
              <el-input v-model="exportForm.keyword" placeholder="关键词筛选（可选）" />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="preview">
                <el-icon><View /></el-icon> 预览数据
              </el-button>
              <el-button type="success" @click="exportData">
                <el-icon><Download /></el-icon> 导出Excel
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
        
        <!-- 预览表格 -->
        <el-card shadow="hover" v-if="previewVisible">
          <template #header>
            <span style="font-weight: bold;">数据预览</span>
          </template>
          
          <el-table :data="previewData" border stripe max-height="400">
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column prop="standardNo" label="标准编号" width="150" />
            <el-table-column prop="itemName" label="项目名称" width="150" />
            <el-table-column prop="clauseNo" label="条款号" width="100" align="center" />
          </el-table>
          
          <div style="margin-top: 15px; text-align: right; color: #999;">
            共 {{ previewData.length }} 条记录
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 创建模板对话框 -->
    <el-dialog v-model="createDialogVisible" title="创建自定义模板" width="600px">
      <el-form :model="templateForm" label-width="100px">
        <el-form-item label="模板名称" required>
          <el-input v-model="templateForm.name" placeholder="请输入模板名称" />
        </el-form-item>
        
        <el-form-item label="模板说明">
          <el-input v-model="templateForm.description" type="textarea" :rows="2" />
        </el-form-item>
        
        <el-form-item label="选择字段">
          <el-checkbox-group v-model="templateForm.fields">
            <el-checkbox label="standardNo">标准编号</el-checkbox>
            <el-checkbox label="standardName">标准名称</el-checkbox>
            <el-checkbox label="clauseNo">项目条款</el-checkbox>
            <el-checkbox label="itemName">项目名称</el-checkbox>
            <el-checkbox label="requirement">项目要求</el-checkbox>
            <el-checkbox label="method">项目方法</el-checkbox>
            <el-checkbox label="equipment">使用设备</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        
        <el-form-item label="是否公开">
          <el-switch v-model="templateForm.isPublic" />
          <span style="margin-left: 10px; color: #999; font-size: 12px;">公开后其他用户可使用</span>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="createTemplate">创建</el-button>
      </template>
    </el-dialog>
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

const activeTemplate = ref('preset1')
const previewVisible = ref(false)
const createDialogVisible = ref(false)

const exportForm = ref({
  standardId: null,
  keyword: ''
})

const templateForm = ref({
  name: '',
  description: '',
  fields: ['standardNo', 'itemName', 'clauseNo'],
  isPublic: false
})

const standards = computed(() => mockStandards)

const previewData = computed(() => {
  if (!exportForm.value.standardId) return []
  
  const standard = mockStandards.find(s => s.id === exportForm.value.standardId)
  if (!standard) return []
  
  return standard.items.map(item => ({
    standardNo: standard.standardNo,
    itemName: item.itemName,
    clauseNo: item.clauseNo
  }))
})

const selectTemplate = (index) => {
  activeTemplate.value = index
  ElMessage.info(`已选择模板：${index}`)
}

const showCreateDialog = () => {
  templateForm.value = {
    name: '',
    description: '',
    fields: ['standardNo', 'itemName', 'clauseNo'],
    isPublic: false
  }
  createDialogVisible.value = true
}

const createTemplate = () => {
  if (!templateForm.value.name) {
    ElMessage.warning('请输入模板名称')
    return
  }
  
  ElMessage.success('模板创建成功')
  createDialogVisible.value = false
}

const preview = () => {
  if (!exportForm.value.standardId) {
    ElMessage.warning('请选择标准')
    return
  }
  
  previewVisible.value = true
  ElMessage.success('数据预览已生成')
}

const exportData = () => {
  if (previewData.value.length === 0) {
    ElMessage.warning('没有数据可导出')
    return
  }
  
  ElMessage.success(`已导出 ${previewData.value.length} 条记录（Demo）`)
}
</script>

