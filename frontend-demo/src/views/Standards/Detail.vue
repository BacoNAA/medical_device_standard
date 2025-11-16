<template>
  <div class="page-container" v-if="standard">
    <!-- 页面头部 -->
    <div class="page-header">
      <el-page-header @back="$router.back()">
        <template #content>
          <h2 class="page-title">{{ standard.standardNo }} {{ standard.standardName }}</h2>
        </template>
      </el-page-header>
    </div>
    
    <!-- 标准基本信息 -->
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <template #header>
        <span style="font-weight: bold;">基本信息</span>
      </template>
      
      <el-descriptions :column="2" border>
        <el-descriptions-item label="标准编号">{{ standard.standardNo }}</el-descriptions-item>
        <el-descriptions-item label="标准名称">{{ standard.standardName }}</el-descriptions-item>
        <el-descriptions-item label="英文名称" :span="2">{{ standard.standardNameEN }}</el-descriptions-item>
        <el-descriptions-item label="发布日期">{{ standard.publishDate }}</el-descriptions-item>
        <el-descriptions-item label="实施日期">{{ standard.effectiveDate }}</el-descriptions-item>
        <el-descriptions-item label="适用范围" :span="2">
          <div style="white-space: pre-wrap;">{{ standard.scope }}</div>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
    
    <!-- 标签页 -->
    <el-card shadow="hover">
      <el-tabs v-model="activeTab">
        <!-- 检验项目 -->
        <el-tab-pane label="检验项目" name="items">
          <el-table :data="standard.items" border stripe>
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column prop="clauseNo" label="条款号" width="100" align="center" />
            <el-table-column prop="itemName" label="项目名称" width="150" />
            <el-table-column prop="requirement" label="项目要求" min-width="300" show-overflow-tooltip />
            <el-table-column label="方法类型" width="120" align="center">
              <template #default="{ row }">
                <el-tag v-if="row.methodType === 1" type="info">无独立方法</el-tag>
                <el-tag v-else-if="row.methodType === 2" type="warning">引用附录</el-tag>
                <el-tag v-else-if="row.methodType === 3" type="success">完整方法</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="使用设备" width="150">
              <template #default="{ row }">
                <el-tag 
                  v-for="eq in row.equipment" 
                  :key="eq" 
                  size="small" 
                  style="margin: 2px;"
                >
                  {{ standardsStore.getEquipmentName(eq) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100" align="center">
              <template #default="{ row }">
                <el-button type="primary" link @click="viewItemDetail(row)">详情</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        
        <!-- 附录内容 -->
        <el-tab-pane label="附录内容" name="appendix">
          <el-empty v-if="!standard.appendixes || standard.appendixes.length === 0" description="暂无附录" />
          <div v-else>
            <el-collapse>
              <el-collapse-item 
                v-for="app in standard.appendixes" 
                :key="app.appendixId"
                :title="`附录${app.appendixCode} - ${app.appendixTitle}`"
              >
                <div style="white-space: pre-wrap; padding: 10px; background: #f5f7fa; border-radius: 4px;">
                  {{ app.content }}
                </div>
              </el-collapse-item>
            </el-collapse>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
    
    <!-- 项目详情对话框 -->
    <el-dialog v-model="itemDialogVisible" title="项目详情" width="70%">
      <el-descriptions :column="1" border v-if="currentItem">
        <el-descriptions-item label="条款号">{{ currentItem.clauseNo }}</el-descriptions-item>
        <el-descriptions-item label="项目名称">{{ currentItem.itemName }}</el-descriptions-item>
        <el-descriptions-item label="项目要求">
          <div style="white-space: pre-wrap;">{{ currentItem.requirement }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="方法引用" v-if="currentItem.methodRef">
          {{ currentItem.methodRef }}
        </el-descriptions-item>
        <el-descriptions-item label="项目方法" v-if="currentItem.methodContent">
          <div style="white-space: pre-wrap;">{{ currentItem.methodContent }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="使用设备">
          <el-tag 
            v-for="eq in currentItem.equipment" 
            :key="eq" 
            style="margin-right: 8px;"
          >
            {{ standardsStore.getEquipmentName(eq) }}
          </el-tag>
          <span v-if="!currentItem.equipment || currentItem.equipment.length === 0">无</span>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
  
  <div v-else class="page-container">
    <el-empty description="标准不存在" />
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { useStandardsStore } from '@/stores/standards'

const route = useRoute()
const standardsStore = useStandardsStore()

const activeTab = ref('items')
const itemDialogVisible = ref(false)
const currentItem = ref(null)

const standard = computed(() => {
  return standardsStore.getStandardById(route.params.id)
})

const viewItemDetail = (item) => {
  currentItem.value = item
  itemDialogVisible.value = true
}
</script>

