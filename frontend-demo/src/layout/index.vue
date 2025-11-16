<template>
  <div class="layout-container">
    <!-- 顶部导航栏 -->
    <div class="layout-header">
      <div class="header-left">
        <el-icon class="header-logo" :size="28"><DataBoard /></el-icon>
        <span class="header-title">医疗器械标准数据库系统</span>
      </div>
      
      <div class="header-right">
        <div class="user-info">
          <el-icon><User /></el-icon>
          <span>{{ userStore.realName }}</span>
          <el-tag size="small" type="success" style="margin-left: 8px">
            {{ userStore.roleName }}
          </el-tag>
        </div>
        
        <el-dropdown @command="handleCommand">
          <el-button text style="color: white;">
            <el-icon><Setting /></el-icon>
          </el-button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <el-icon><User /></el-icon> 个人信息
              </el-dropdown-item>
              <el-dropdown-item command="password">
                <el-icon><Lock /></el-icon> 修改密码
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <el-icon><SwitchButton /></el-icon> 退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    
    <!-- 主体区域 -->
    <div class="layout-main">
      <!-- 左侧菜单 -->
      <div class="layout-sidebar">
        <el-menu
          :default-active="activeMenu"
          router
          :unique-opened="true"
        >
          <el-menu-item index="/dashboard">
            <el-icon><HomeFilled /></el-icon>
            <span>工作台</span>
          </el-menu-item>
          
          <!-- 标准数据管理 -->
          <el-sub-menu index="standards" v-if="showMenu(['all'])">
            <template #title>
              <el-icon><Document /></el-icon>
              <span>标准数据管理</span>
            </template>
            <el-menu-item index="/standards">标准浏览</el-menu-item>
            <el-menu-item index="/import" v-if="showMenu(['dataadmin'])">标准导入</el-menu-item>
          </el-sub-menu>
          
          <!-- 设备管理 -->
          <el-menu-item index="/equipment" v-if="showMenu(['dataadmin', 'sb'])">
            <el-icon><Tools /></el-icon>
            <span>设备管理</span>
          </el-menu-item>
          
          <!-- 产品技术要求编辑 -->
          <el-menu-item index="/ptr-editor" v-if="showMenu(['yw'])">
            <el-icon><EditPen /></el-icon>
            <span>产品技术要求编辑</span>
          </el-menu-item>
          
          <!-- 数据导出 -->
          <el-sub-menu index="export">
            <template #title>
              <el-icon><Download /></el-icon>
              <span>数据导出</span>
            </template>
            <el-menu-item index="/export/table1" v-if="showMenu(['zl'])">能力建设信息表</el-menu-item>
            <el-menu-item index="/export/table2" v-if="showMenu(['sb'])">认可检验能力表</el-menu-item>
            <el-menu-item index="/export/table3" v-if="showMenu(['sy'])">操作规程表</el-menu-item>
            <el-menu-item index="/export/custom" v-if="showMenu(['all'])">自定义导出</el-menu-item>
          </el-sub-menu>
          
          <!-- 错误报告 -->
          <el-menu-item index="/error-reports" v-if="showMenu(['all'])">
            <el-icon><Warning /></el-icon>
            <span>错误报告</span>
          </el-menu-item>
          
          <!-- 系统管理 -->
          <el-sub-menu index="system" v-if="showMenu(['sysadmin'])">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/system/users">用户管理</el-menu-item>
            <el-menu-item index="/system/logs">操作日志</el-menu-item>
            <el-menu-item index="/system/settings">系统配置</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
      
      <!-- 内容区域 -->
      <div class="layout-content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

// 检查菜单权限
const showMenu = (roles) => {
  if (roles.includes('all')) return true
  return roles.some(role => {
    if (role === 'dataadmin') return userStore.roleCode === 'dataadmin'
    if (role === 'sysadmin') return userStore.roleCode === 'sysadmin'
    if (role === 'yw') return userStore.roleCode === 'yw'
    if (role === 'zl') return userStore.roleCode === 'zl'
    if (role === 'sb') return userStore.roleCode === 'sb'
    if (role === 'sy') return userStore.roleCode === 'sy'
    return false
  })
}

// 处理下拉菜单命令
const handleCommand = (command) => {
  if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      type: 'warning'
    }).then(() => {
      userStore.logout()
      router.push('/login')
      ElMessage.success('已退出登录')
    }).catch(() => {})
  } else if (command === 'password') {
    ElMessage.info('修改密码功能（Demo）')
  } else if (command === 'profile') {
    ElMessage.info('个人信息功能（Demo）')
  }
}
</script>

