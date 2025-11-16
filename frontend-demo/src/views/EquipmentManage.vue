<template>
  <div class="equipment-manage-container">
    <!-- 头部导航 -->
    <div class="header">
      <div class="header-content">
        <h1>设备管理</h1>
        <el-button-group>
          <el-button @click="$router.push('/import-preview')">导入预览</el-button>
          <el-button @click="$router.push('/standard-list')">标准列表</el-button>
        </el-button-group>
      </div>
    </div>

    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 统计卡片 -->
      <div class="statistics-row">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="color: #409eff;">
              <el-icon><Tools /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ equipmentList.length }}</div>
              <div class="stat-label">设备总数</div>
            </div>
          </div>
        </el-card>
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="color: #67c23a;">
              <el-icon><Link /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ getTotalRelations() }}</div>
              <div class="stat-label">项目关联数</div>
            </div>
          </div>
        </el-card>
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="color: #e6a23c;">
              <el-icon><Grid /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ getTotalSpecs() }}</div>
              <div class="stat-label">规格类型数</div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 设备列表 -->
      <el-card class="list-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span><el-icon><List /></el-icon> 设备信息列表</span>
            <el-button type="primary">
              <el-icon><Plus /></el-icon> 新增设备
            </el-button>
          </div>
        </template>

        <el-table 
          :data="equipmentList" 
          stripe
          border
          style="width: 100%"
        >
          <el-table-column type="expand">
            <template #default="{ row }">
              <div class="expand-content">
                <div class="expand-section">
                  <div class="section-title">
                    <el-icon><InfoFilled /></el-icon> 设备详细信息
                  </div>
                  <el-descriptions :column="2" border>
                    <el-descriptions-item label="设备编号">
                      <el-tag type="primary">{{ row.code }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="设备名称">{{ row.name }}</el-descriptions-item>
                    <el-descriptions-item label="通用描述" :span="2">{{ row.model }}</el-descriptions-item>
                    <el-descriptions-item label="用途说明" :span="2">{{ row.description }}</el-descriptions-item>
                    <el-descriptions-item label="关联项目数">
                      <el-tag type="success">{{ row.relatedProjects }}</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="规格类型数">
                      <el-tag type="warning">{{ row.specifications.length }}</el-tag>
                    </el-descriptions-item>
                  </el-descriptions>
                </div>

                <div class="expand-section">
                  <div class="section-title">
                    <el-icon><Grid /></el-icon> 关联规格要求
                  </div>
                  <div class="spec-list">
                    <el-tag 
                      v-for="(spec, index) in row.specifications" 
                      :key="index"
                      type="info"
                      effect="plain"
                      size="large"
                      style="margin: 5px"
                    >
                      {{ spec }}
                    </el-tag>
                  </div>
                </div>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="设备编号" prop="code" width="120">
            <template #default="{ row }">
              <el-tag type="primary" size="large">{{ row.code }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="设备名称" prop="name" min-width="200" />
          <el-table-column label="通用描述" prop="model" min-width="150" />
          <el-table-column label="用途说明" prop="description" min-width="180" show-overflow-tooltip />
          <el-table-column label="关联项目数" prop="relatedProjects" width="120" align="center">
            <template #default="{ row }">
              <el-tag type="success" round>{{ row.relatedProjects }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="规格类型" width="100" align="center">
            <template #default="{ row }">
              <el-tag type="warning" round>{{ row.specifications.length }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default>
              <el-button type="primary" size="small">
                <el-icon><Edit /></el-icon> 编辑
              </el-button>
              <el-button type="success" size="small">
                <el-icon><Link /></el-icon> 关联
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 设备关键词库 -->
      <el-card class="keyword-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span><el-icon><Key /></el-icon> 设备关键词库（用于智能识别）</span>
            <el-button type="primary" size="small">
              <el-icon><Plus /></el-icon> 添加关键词
            </el-button>
          </div>
        </template>
        
        <el-alert 
          title="设备关键词用于标准导入时自动识别设备。系统会在项目方法内容中搜索这些关键词来自动关联设备。" 
          type="info" 
          :closable="false"
          style="margin-bottom: 15px"
        />

        <el-table :data="equipmentList.slice(0, 3)" border>
          <el-table-column label="设备名称" prop="name" width="200" />
          <el-table-column label="识别关键词" min-width="400">
            <template #default="{ row }">
              <el-tag 
                v-for="(keyword, index) in getKeywords(row.name)" 
                :key="index"
                closable
                style="margin: 3px"
              >
                {{ keyword }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default>
              <el-button type="primary" size="small" link>
                <el-icon><Edit /></el-icon> 编辑关键词
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { mockEquipments } from '../mock/standardData'

const equipmentList = ref(mockEquipments)

const getTotalRelations = () => {
  return equipmentList.value.reduce((sum, eq) => sum + eq.relatedProjects, 0)
}

const getTotalSpecs = () => {
  return equipmentList.value.reduce((sum, eq) => sum + eq.specifications.length, 0)
}

const getKeywords = (name) => {
  // 模拟关键词数据
  const keywordMap = {
    '电子天平': ['电子天平', '天平', '分析天平', '精密天平'],
    '注射器器身密合性测试仪': ['器身密合性', '密合性测试仪', '密合性试验'],
    '活塞推动力测试仪': ['活塞推动力', '推动力测试', '推力测试仪']
  }
  return keywordMap[name] || [name]
}
</script>

<style scoped>
.equipment-manage-container {
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

.statistics-row {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  background: linear-gradient(135deg, #fff 0%, #f8f9fa 100%);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stat-icon {
  font-size: 48px;
}

.stat-value {
  font-size: 36px;
  font-weight: bold;
  line-height: 1;
  margin-bottom: 5px;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.list-card,
.keyword-card {
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

.expand-content {
  padding: 20px;
  background: #fafafa;
}

.expand-section {
  margin-bottom: 20px;
}

.expand-section:last-child {
  margin-bottom: 0;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 600;
  color: #409eff;
  margin-bottom: 15px;
  padding-bottom: 8px;
  border-bottom: 2px solid #e4e7ed;
}

.spec-list {
  padding-left: 24px;
}
</style>

