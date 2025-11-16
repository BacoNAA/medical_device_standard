<template>
  <div class="home-container">
    <div class="header">
      <div class="logo">
        <el-icon :size="32" color="#409eff"><DataAnalysis /></el-icon>
        <h1>医疗器械标准数据库系统</h1>
      </div>
      <div class="user-info">
        <el-tag type="success">演示模式</el-tag>
      </div>
    </div>
    
    <div class="main-content">
      <div class="sidebar">
        <el-menu
          :default-active="activeMenu"
          class="demo-menu"
          @select="handleMenuSelect"
        >
          <el-menu-item index="/import-preview">
            <el-icon><Upload /></el-icon>
            <span>标准导入预览</span>
          </el-menu-item>
          
          <el-menu-item index="/standard-list">
            <el-icon><List /></el-icon>
            <span>标准列表</span>
          </el-menu-item>
          
          <el-menu-item index="/equipment-manage">
            <el-icon><Operation /></el-icon>
            <span>设备管理</span>
          </el-menu-item>
          
          <el-sub-menu index="export">
            <template #title>
              <el-icon><Download /></el-icon>
              <span>数据导出</span>
            </template>
            <el-menu-item index="/export-table1">能力建设项目表</el-menu-item>
            <el-menu-item index="/export-table2">检验检测能力表</el-menu-item>
            <el-menu-item index="/export-table3">操作规程表</el-menu-item>
          </el-sub-menu>
          
          <el-menu-item index="/ptr-editor">
            <el-icon><EditPen /></el-icon>
            <span>产品技术要求</span>
          </el-menu-item>
        </el-menu>
      </div>
      
      <div class="content-area">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const activeMenu = ref('/import-preview')

watch(() => route.path, (newPath) => {
  activeMenu.value = newPath
}, { immediate: true })

const handleMenuSelect = (index) => {
  router.push(index)
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 16px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo h1 {
  font-size: 20px;
  font-weight: 600;
  margin: 0;
}

.main-content {
  display: flex;
  flex: 1;
  overflow: hidden;
}

.sidebar {
  width: 240px;
  background: #fff;
  box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
}

.demo-menu {
  height: 100%;
  border-right: none;
}

.content-area {
  flex: 1;
  overflow-y: auto;
  background: #f5f7fa;
}
</style>

