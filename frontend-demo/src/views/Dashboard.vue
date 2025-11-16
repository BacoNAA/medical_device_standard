<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">工作台</h2>
      <p class="page-description">欢迎使用医疗器械标准内容信息化数据库及运用系统</p>
    </div>
    
    <!-- 统计卡片 -->
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="display: flex; align-items: center; justify-content: space-between;">
            <div>
              <div style="color: #909399; font-size: 14px; margin-bottom: 8px;">标准总数</div>
              <div style="font-size: 28px; font-weight: bold; color: #409eff;">{{ standardCount }}</div>
            </div>
            <el-icon :size="48" color="#409eff"><Document /></el-icon>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="display: flex; align-items: center; justify-content: space-between;">
            <div>
              <div style="color: #909399; font-size: 14px; margin-bottom: 8px;">检验项目</div>
              <div style="font-size: 28px; font-weight: bold; color: #67c23a;">{{ itemCount }}</div>
            </div>
            <el-icon :size="48" color="#67c23a"><List /></el-icon>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="display: flex; align-items: center; justify-content: space-between;">
            <div>
              <div style="color: #909399; font-size: 14px; margin-bottom: 8px;">设备数量</div>
              <div style="font-size: 28px; font-weight: bold; color: #e6a23c;">{{ equipmentCount }}</div>
            </div>
            <el-icon :size="48" color="#e6a23c"><Tools /></el-icon>
          </div>
        </el-card>
      </el-col>
      
      <el-col :span="6">
        <el-card shadow="hover">
          <div style="display: flex; align-items: center; justify-content: space-between;">
            <div>
              <div style="color: #909399; font-size: 14px; margin-bottom: 8px;">当前角色</div>
              <div style="font-size: 18px; font-weight: bold; color: #f56c6c; margin-top: 5px;">
                {{ userStore.roleName }}
              </div>
            </div>
            <el-icon :size="48" color="#f56c6c"><User /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 快捷操作 -->
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <template #header>
        <span style="font-weight: bold;">快捷操作</span>
      </template>
      
      <el-space wrap :size="15">
        <el-button type="primary" @click="$router.push('/standards')">
          <el-icon><Document /></el-icon> 浏览标准
        </el-button>
        
        <el-button type="success" @click="$router.push('/import')" v-if="userStore.roleCode === 'dataadmin'">
          <el-icon><Upload /></el-icon> 导入标准
        </el-button>
        
        <el-button type="warning" @click="$router.push('/ptr-editor')" v-if="userStore.roleCode === 'yw'">
          <el-icon><EditPen /></el-icon> 编辑产品技术要求
        </el-button>
        
        <el-button type="info" @click="$router.push('/export/custom')">
          <el-icon><Download /></el-icon> 导出数据
        </el-button>
        
        <el-button @click="$router.push('/equipment')" v-if="['dataadmin', 'sb'].includes(userStore.roleCode)">
          <el-icon><Tools /></el-icon> 设备管理
        </el-button>
      </el-space>
    </el-card>
    
    <!-- 最近操作 -->
    <el-card shadow="hover">
      <template #header>
        <span style="font-weight: bold;">最近浏览的标准</span>
      </template>
      
      <el-table :data="recentStandards" stripe>
        <el-table-column prop="standardNo" label="标准编号" width="180" />
        <el-table-column prop="standardName" label="标准名称" />
        <el-table-column prop="itemCount" label="项目数" width="100" align="center" />
        <el-table-column label="操作" width="120" align="center">
          <template #default="{ row }">
            <el-button type="primary" link @click="viewStandard(row.id)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

// Mock数据
const mockStandards = [
  { id: 1, standardNo: 'GB 15810-2019', standardName: '一次性使用无菌注射器', itemCount: 15 },
  { id: 2, standardNo: 'YY 0469-2023', standardName: '医用外科口罩', itemCount: 8 },
  { id: 3, standardNo: 'GB 18457-2015', standardName: '制药机械（设备）', itemCount: 12 }
]

const mockEquipments = [
  { id: 1, equipmentCode: 'D001', equipmentName: '电子天平' },
  { id: 2, equipmentCode: 'D002', equipmentName: '游标卡尺' }
]

const router = useRouter()
const userStore = useUserStore()

const standardCount = computed(() => mockStandards.length)
const itemCount = computed(() => {
  return mockStandards.reduce((sum, s) => sum + (s.itemCount || 0), 0)
})
const equipmentCount = computed(() => mockEquipments.length)

const recentStandards = computed(() => mockStandards.slice(0, 5))

const viewStandard = (id) => {
  router.push(`/standards/${id}`)
}
</script>

