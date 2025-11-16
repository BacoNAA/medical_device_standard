<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-title">
        <h1>医疗器械标准内容信息化数据库及运用系统</h1>
        <p>Medical Device Standard Database & Application System</p>
      </div>
      
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef">
        <el-form-item prop="username">
          <el-input 
            v-model="loginForm.username" 
            placeholder="请输入用户名"
            size="large"
            prefix-icon="User"
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            type="password"
            placeholder="请输入密码"
            size="large"
            prefix-icon="Lock"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <el-checkbox v-model="loginForm.remember">记住密码</el-checkbox>
        </el-form-item>
        
        <el-form-item>
          <el-button 
            type="primary" 
            size="large" 
            style="width: 100%"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
      
      <el-divider>快速登录（Demo）</el-divider>
      
      <div class="quick-login">
        <el-button size="small" @click="quickLogin('dataadmin')">数据输入员</el-button>
        <el-button size="small" @click="quickLogin('sysadmin')">系统管理员</el-button>
        <el-button size="small" @click="quickLogin('yw001')">业务人员</el-button>
        <el-button size="small" @click="quickLogin('zl001')">质量管理</el-button>
        <el-button size="small" @click="quickLogin('sb001')">设备管理</el-button>
        <el-button size="small" @click="quickLogin('sy001')">实验室</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref()
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: '',
  remember: false
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

// 登录处理
const handleLogin = async () => {
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      
      // 模拟登录延迟
      setTimeout(() => {
        userStore.login({
          username: loginForm.username,
          realName: loginForm.username
        })
        
        ElMessage.success('登录成功！')
        router.push('/')
        loading.value = false
      }, 500)
    }
  })
}

// 快速登录
const quickLogin = (username) => {
  loginForm.username = username
  loginForm.password = '123456'
  handleLogin()
}
</script>

<style scoped>
.quick-login {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  justify-content: center;
}
</style>

