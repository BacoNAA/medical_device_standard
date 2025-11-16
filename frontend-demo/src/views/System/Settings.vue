<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">系统配置</h2>
      <p class="page-description">配置系统运行参数</p>
    </div>
    
    <el-card shadow="hover">
      <el-form :model="settingsForm" label-width="180px" style="max-width: 800px;">
        <el-divider content-position="left">安全设置</el-divider>
        
        <el-form-item label="会话超时时间（分钟）">
          <el-input-number v-model="settingsForm.sessionTimeout" :min="5" :max="120" />
          <span style="margin-left: 10px; color: #999;">用户无操作自动退出登录的时间</span>
        </el-form-item>
        
        <el-form-item label="密码最小长度">
          <el-input-number v-model="settingsForm.passwordMinLength" :min="6" :max="20" />
          <span style="margin-left: 10px; color: #999;">用户密码的最小位数要求</span>
        </el-form-item>
        
        <el-divider content-position="left">文件设置</el-divider>
        
        <el-form-item label="文件上传大小限制（MB）">
          <el-input-number v-model="settingsForm.maxFileSize" :min="1" :max="100" />
          <span style="margin-left: 10px; color: #999;">单个文件上传的最大尺寸</span>
        </el-form-item>
        
        <el-form-item label="导出文件保留天数">
          <el-input-number v-model="settingsForm.exportFileRetention" :min="1" :max="30" />
          <span style="margin-left: 10px; color: #999;">导出文件在服务器保留的时间，过期自动清理</span>
        </el-form-item>
        
        <el-divider content-position="left">编辑器设置</el-divider>
        
        <el-form-item label="自动保存间隔（分钟）">
          <el-input-number v-model="settingsForm.autoSaveInterval" :min="1" :max="10" />
          <span style="margin-left: 10px; color: #999;">产品技术要求编辑器自动保存时间间隔</span>
        </el-form-item>
        
        <el-form-item label="联想查找结果数量">
          <el-input-number v-model="settingsForm.searchResultLimit" :min="3" :max="20" />
          <span style="margin-left: 10px; color: #999;">智能联想查找时显示的结果条数</span>
        </el-form-item>
        
        <el-divider content-position="left">系统维护</el-divider>
        
        <el-form-item label="数据库备份">
          <el-button type="primary" @click="backupDatabase">
            <el-icon><Download /></el-icon> 立即备份
          </el-button>
          <span style="margin-left: 10px; color: #999;">最后备份时间：2025-10-21 02:00:00</span>
        </el-form-item>
        
        <el-form-item label="自动备份">
          <el-switch v-model="settingsForm.autoBackup" />
          <span style="margin-left: 10px; color: #999;">每天凌晨2点自动备份数据库</span>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="saveSettings">
            <el-icon><Select /></el-icon> 保存设置
          </el-button>
          <el-button @click="resetSettings">
            <el-icon><Refresh /></el-icon> 恢复默认
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const settingsForm = ref({
  sessionTimeout: 30,
  passwordMinLength: 8,
  maxFileSize: 5,
  exportFileRetention: 7,
  autoSaveInterval: 2,
  searchResultLimit: 5,
  autoBackup: true
})

const saveSettings = () => {
  ElMessage.success('设置已保存')
}

const resetSettings = () => {
  settingsForm.value = {
    sessionTimeout: 30,
    passwordMinLength: 8,
    maxFileSize: 5,
    exportFileRetention: 7,
    autoSaveInterval: 2,
    searchResultLimit: 5,
    autoBackup: true
  }
  ElMessage.info('已恢复默认设置')
}

const backupDatabase = () => {
  ElMessage.success('数据库备份已启动（Demo）')
}
</script>

