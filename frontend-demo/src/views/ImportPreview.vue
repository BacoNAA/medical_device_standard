<template>
  <div class="import-preview-container">
    <!-- 头部导航 -->
    <div class="header">
      <div class="header-content">
        <h1>医疗器械标准数据库系统 - 导入预览演示</h1>
        <el-button-group>
          <el-button @click="$router.push('/standard-list')">标准列表</el-button>
          <el-button @click="$router.push('/equipment-manage')">设备管理</el-button>
        </el-button-group>
      </div>
    </div>

    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 步骤指示器 -->
      <el-steps :active="currentStep" finish-status="success" align-center class="steps">
        <el-step title="上传文件" description="选择Excel文件" />
        <el-step title="系统自动处理" description="智能识别方法和设备" />
        <el-step title="预览确认" description="审核并补充信息" />
        <el-step title="完成导入" description="保存到数据库" />
      </el-steps>

      <!-- 文件信息卡片 -->
      <el-card class="file-info-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span><el-icon><Document /></el-icon> 文件信息</span>
          </div>
        </template>
        <el-descriptions :column="3" border>
          <el-descriptions-item label="文件名">{{ previewData.fileName }}</el-descriptions-item>
          <el-descriptions-item label="上传时间">{{ previewData.uploadTime }}</el-descriptions-item>
          <el-descriptions-item label="标准编号">
            <el-tag type="primary">{{ previewData.standardInfo.standardNo }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="标准名称" :span="3">
            {{ previewData.standardInfo.standardName }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 处理统计 -->
      <el-card class="statistics-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span><el-icon><PieChart /></el-icon> 自动处理结果统计</span>
          </div>
        </template>
        <div class="statistics-content">
          <div class="stat-item success">
            <div class="stat-icon">
              <el-icon><CircleCheck /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ previewData.statistics.success }}</div>
              <div class="stat-label">自动处理成功</div>
            </div>
          </div>
          <div class="stat-item warning">
            <div class="stat-icon">
              <el-icon><Warning /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ previewData.statistics.warning }}</div>
              <div class="stat-label">新设备需确认</div>
            </div>
          </div>
          <div class="stat-item error">
            <div class="stat-icon">
              <el-icon><CircleClose /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ previewData.statistics.error }}</div>
              <div class="stat-label">需人工补充</div>
            </div>
          </div>
          <div class="stat-item total">
            <div class="stat-icon">
              <el-icon><Files /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ previewData.statistics.total }}</div>
              <div class="stat-label">检验项目总数</div>
            </div>
          </div>
        </div>
      </el-card>

      <!-- 检验项目预览表格 -->
      <el-card class="preview-table-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span><el-icon><List /></el-icon> 检验项目预览（带状态标注）</span>
            <div class="filter-buttons">
              <el-radio-group v-model="filterStatus" size="small">
                <el-radio-button label="all">全部</el-radio-button>
                <el-radio-button label="success">成功</el-radio-button>
                <el-radio-button label="warning">待确认</el-radio-button>
                <el-radio-button label="error">需补充</el-radio-button>
              </el-radio-group>
            </div>
          </div>
        </template>

        <el-table 
          :data="filteredItems" 
          stripe
          border
          :row-class-name="getRowClassName"
          style="width: 100%"
          max-height="600"
        >
          <el-table-column type="index" label="序号" width="60" />
          <el-table-column prop="clauseNo" label="项目条款" width="100" />
          <el-table-column prop="itemName" label="项目名称" width="150" />
          <el-table-column prop="requirement" label="项目要求" show-overflow-tooltip min-width="200" />
          <el-table-column label="方法类型" width="100">
            <template #default="{ row }">
              <el-tag v-if="row.methodType === 1" size="small">无独立方法</el-tag>
              <el-tag v-else-if="row.methodType === 2" size="small" type="warning">引用附录</el-tag>
              <el-tag v-else size="small" type="success">完整方法</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="methodRef" label="方法引用" width="100" show-overflow-tooltip />
          <el-table-column label="使用设备" min-width="180">
            <template #default="{ row }">
              <div v-if="row.equipments.length > 0">
                <el-tag 
                  v-for="(eq, index) in row.equipments" 
                  :key="index" 
                  size="small"
                  style="margin: 2px"
                >
                  {{ eq.name }}（{{ eq.specification }}）
                </el-tag>
              </div>
              <el-text v-else type="info" size="small">无</el-text>
            </template>
          </el-table-column>
          <el-table-column label="处理状态" width="120" fixed="right">
            <template #default="{ row }">
              <el-tag v-if="row.status === 'success'" type="success">
                <el-icon><CircleCheck /></el-icon> 成功
              </el-tag>
              <el-tag v-else-if="row.status === 'warning'" type="warning">
                <el-icon><Warning /></el-icon> 待确认
              </el-tag>
              <el-tag v-else type="danger">
                <el-icon><CircleClose /></el-icon> 需补充
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100" fixed="right">
            <template #default="{ row }">
              <el-button v-if="row.status !== 'success'" size="small" type="primary" link>
                编辑
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 新设备确认 -->
      <el-card v-if="previewData.newEquipments.length > 0" class="new-equipment-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span><el-icon><Warning /></el-icon> 新设备需要确认（{{ previewData.newEquipments.length }}个）</span>
          </div>
        </template>
        <el-table :data="previewData.newEquipments" border>
          <el-table-column label="识别到的关键词" prop="keywordFound" width="150" />
          <el-table-column label="建议设备名称" prop="suggestedName" width="180" />
          <el-table-column label="建议编号" prop="suggestedCode" width="100" />
          <el-table-column label="规格要求" prop="specification" min-width="150" />
          <el-table-column label="关联项目" prop="relatedItems" min-width="150">
            <template #default="{ row }">
              <el-tag v-for="(item, index) in row.relatedItems" :key="index" size="small" style="margin: 2px">
                {{ item }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default>
              <el-button type="success" size="small">确认添加</el-button>
              <el-button type="warning" size="small">修改</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 需补充信息的项目 -->
      <el-card v-if="previewData.incompleteItems.length > 0" class="incomplete-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span><el-icon><CircleClose /></el-icon> 需要人工补充的项目（{{ previewData.incompleteItems.length }}个）</span>
          </div>
        </template>
        <el-table :data="previewData.incompleteItems" border>
          <el-table-column label="项目条款" prop="clauseNo" width="100" />
          <el-table-column label="项目名称" prop="itemName" width="150" />
          <el-table-column label="缺失信息" prop="missingInfo" min-width="150" />
          <el-table-column label="建议" prop="suggestion" min-width="200" />
          <el-table-column label="操作" width="120" fixed="right">
            <template #default>
              <el-button type="primary" size="small">补充信息</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button size="large" @click="currentStep = 0">取消导入</el-button>
        <el-button size="large" type="warning">保存草稿</el-button>
        <el-button 
          size="large" 
          type="primary"
          @click="confirmImport"
          :disabled="previewData.statistics.error > 0"
        >
          确认导入
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { mockImportPreview } from '../mock/standardData'

const router = useRouter()
const currentStep = ref(2) // 当前在预览确认步骤
const filterStatus = ref('all')
const previewData = ref(mockImportPreview)

// 过滤后的项目列表
const filteredItems = computed(() => {
  if (filterStatus.value === 'all') {
    return previewData.value.items
  }
  return previewData.value.items.filter(item => item.status === filterStatus.value)
})

// 获取行的类名（用于着色）
const getRowClassName = ({ row }) => {
  return `row-${row.status}`
}

// 确认导入
const confirmImport = () => {
  currentStep.value = 3
  ElMessage.success('导入成功！正在跳转到标准详情页面...')
  setTimeout(() => {
    router.push('/standard-detail/1')
  }, 1500)
}
</script>

<style scoped>
.import-preview-container {
  min-height: 100vh;
  background: linear-gradient(to bottom, #f5f7fa 0%, #ffffff 100%);
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px 0;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-content h1 {
  font-size: 24px;
  font-weight: 500;
  margin: 0;
}

.main-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 30px 20px;
}

.steps {
  margin-bottom: 30px;
  background: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}

.file-info-card,
.statistics-card,
.preview-table-card,
.new-equipment-card,
.incomplete-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 16px;
  font-weight: 500;
}

.card-header span {
  display: flex;
  align-items: center;
  gap: 8px;
}

.statistics-content {
  display: flex;
  justify-content: space-around;
  gap: 20px;
}

.stat-item {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
  border-radius: 8px;
  background: #f5f7fa;
}

.stat-item.success {
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
}

.stat-item.warning {
  background: linear-gradient(135deg, #fff3e0 0%, #ffe0b2 100%);
}

.stat-item.error {
  background: linear-gradient(135deg, #ffebee 0%, #ffcdd2 100%);
}

.stat-item.total {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
}

.stat-icon {
  font-size: 40px;
}

.stat-item.success .stat-icon {
  color: #4caf50;
}

.stat-item.warning .stat-icon {
  color: #ff9800;
}

.stat-item.error .stat-icon {
  color: #f44336;
}

.stat-item.total .stat-icon {
  color: #2196f3;
}

.stat-info {
  display: flex;
  flex-direction: column;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  line-height: 1;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

/* 表格行颜色 */
:deep(.row-success) {
  background-color: #f1f8f4 !important;
}

:deep(.row-warning) {
  background-color: #fff8f0 !important;
}

:deep(.row-error) {
  background-color: #fff1f0 !important;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 20px;
  margin-top: 30px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}

.filter-buttons {
  display: flex;
  gap: 10px;
}
</style>

