<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">用户管理</h2>
      <p class="page-description">管理系统用户账号和权限</p>
    </div>
    
    <!-- 工具栏 -->
    <div class="table-actions">
      <el-button type="primary" @click="showAddDialog">
        <el-icon><Plus /></el-icon> 创建用户
      </el-button>
      
      <el-select v-model="roleFilter" placeholder="筛选角色" clearable style="width: 200px;">
        <el-option label="数据输入员" value="1" />
        <el-option label="系统管理员" value="2" />
        <el-option label="业务人员" value="3" />
        <el-option label="质量管理人员" value="4" />
        <el-option label="设备管理人员" value="5" />
        <el-option label="实验室人员" value="6" />
      </el-select>
    </div>
    
    <!-- 用户列表 -->
    <el-table :data="filteredUsers" border stripe>
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="username" label="用户名" width="150" />
      <el-table-column prop="realName" label="姓名" width="120" />
      <el-table-column label="角色" width="150" align="center">
        <template #default="{ row }">
          <el-tag :type="getRoleType(row.roleType)">
            {{ getRoleName(row.roleType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="联系电话" width="130" />
      <el-table-column prop="email" label="邮箱" width="180" show-overflow-tooltip />
      <el-table-column prop="department" label="所属部门" width="120" />
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="lastLoginTime" label="最后登录" width="160" />
      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="editUser(row)">编辑</el-button>
          <el-button type="warning" link @click="resetPassword(row)">重置密码</el-button>
          <el-button 
            :type="row.status === 1 ? 'danger' : 'success'" 
            link 
            @click="toggleStatus(row)"
          >
            {{ row.status === 1 ? '停用' : '启用' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 新增/编辑用户对话框 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑用户' : '创建用户'" width="600px">
      <el-form :model="userForm" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input 
            v-model="userForm.username" 
            placeholder="根据角色格式输入，如：yw001"
            :disabled="isEdit"
          />
          <div style="font-size: 12px; color: #999; margin-top: 5px;">
            数据输入员: dataadmin | 系统管理员: sysadmin<br>
            业务人员: yw### | 质量管理: zl### | 设备管理: sb### | 实验室: sy###
          </div>
        </el-form-item>
        
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="userForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        
        <el-form-item label="初始密码" v-if="!isEdit">
          <el-input v-model="userForm.password" type="password" placeholder="留空则默认为123456" />
        </el-form-item>
        
        <el-form-item label="联系电话">
          <el-input v-model="userForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        
        <el-form-item label="邮箱">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        
        <el-form-item label="所属部门">
          <el-input v-model="userForm.department" placeholder="请输入所属部门" />
        </el-form-item>
        
        <el-form-item label="状态">
          <el-radio-group v-model="userForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveUser">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()
const roleFilter = ref('')

const userForm = ref({
  username: '',
  realName: '',
  password: '',
  phone: '',
  email: '',
  department: '',
  status: 1
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ]
}

const users = ref([
  {
    id: 1,
    username: 'dataadmin',
    realName: '张三',
    roleType: 1,
    phone: '13800138001',
    email: 'dataadmin@example.com',
    department: '数据管理部',
    status: 1,
    lastLoginTime: '2025-10-21 09:00:00'
  },
  {
    id: 2,
    username: 'sysadmin',
    realName: '李四',
    roleType: 2,
    phone: '13800138002',
    email: 'sysadmin@example.com',
    department: '信息技术部',
    status: 1,
    lastLoginTime: '2025-10-21 08:30:00'
  },
  {
    id: 3,
    username: 'yw001',
    realName: '王五',
    roleType: 3,
    phone: '13800138003',
    email: 'wangwu@example.com',
    department: '业务部',
    status: 1,
    lastLoginTime: '2025-10-20 16:20:00'
  }
])

const filteredUsers = computed(() => {
  if (!roleFilter.value) return users.value
  return users.value.filter(u => u.roleType === parseInt(roleFilter.value))
})

const getRoleName = (roleType) => {
  const names = ['', '数据输入员', '系统管理员', '业务人员', '质量管理人员', '设备管理人员', '实验室人员']
  return names[roleType] || '未知'
}

const getRoleType = (roleType) => {
  const types = ['', 'danger', 'warning', 'primary', 'success', 'info', '']
  return types[roleType] || 'info'
}

const showAddDialog = () => {
  isEdit.value = false
  userForm.value = {
    username: '',
    realName: '',
    password: '',
    phone: '',
    email: '',
    department: '',
    status: 1
  }
  dialogVisible.value = true
}

const editUser = (user) => {
  isEdit.value = true
  userForm.value = { ...user }
  dialogVisible.value = true
}

const saveUser = async () => {
  await formRef.value.validate((valid) => {
    if (valid) {
      if (isEdit.value) {
        ElMessage.success('用户信息已更新')
      } else {
        ElMessage.success('用户已创建')
      }
      dialogVisible.value = false
    }
  })
}

const resetPassword = (user) => {
  ElMessageBox.confirm(`确定要重置用户 ${user.realName} 的密码吗？`, '提示', {
    type: 'warning'
  }).then(() => {
    ElMessage.success('密码已重置为：123456')
  }).catch(() => {})
}

const toggleStatus = (user) => {
  user.status = user.status === 1 ? 0 : 1
  ElMessage.success(user.status === 1 ? '已启用' : '已停用')
}
</script>

