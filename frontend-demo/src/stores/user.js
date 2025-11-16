import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const username = ref(localStorage.getItem('username') || '')
  const realName = ref(localStorage.getItem('realName') || '')
  const roleType = ref(parseInt(localStorage.getItem('roleType')) || 0)
  
  // 角色映射
  const roleMap = {
    1: { name: '数据输入员', code: 'dataadmin' },
    2: { name: '系统管理员', code: 'sysadmin' },
    3: { name: '业务人员', code: 'yw' },
    4: { name: '质量管理人员', code: 'zl' },
    5: { name: '设备管理人员', code: 'sb' },
    6: { name: '实验室人员', code: 'sy' }
  }
  
  // 根据用户名识别角色
  const identifyRole = (username) => {
    if (username === 'dataadmin') return 1
    if (username === 'sysadmin') return 2
    if (username.startsWith('yw')) return 3
    if (username.startsWith('zl')) return 4
    if (username.startsWith('sb')) return 5
    if (username.startsWith('sy')) return 6
    return 0
  }
  
  const roleName = computed(() => roleMap[roleType.value]?.name || '未知角色')
  const roleCode = computed(() => roleMap[roleType.value]?.code || '')
  
  // 登录
  const login = (userInfo) => {
    token.value = 'demo-token-' + Date.now()
    username.value = userInfo.username
    realName.value = userInfo.realName || userInfo.username
    roleType.value = identifyRole(userInfo.username)
    
    localStorage.setItem('token', token.value)
    localStorage.setItem('username', username.value)
    localStorage.setItem('realName', realName.value)
    localStorage.setItem('roleType', roleType.value)
  }
  
  // 登出
  const logout = () => {
    token.value = ''
    username.value = ''
    realName.value = ''
    roleType.value = 0
    
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    localStorage.removeItem('realName')
    localStorage.removeItem('roleType')
  }
  
  // 检查权限
  const hasPermission = (roles) => {
    if (!roles || roles.length === 0 || roles.includes('all')) return true
    return roles.includes(roleCode.value)
  }
  
  return {
    token,
    username,
    realName,
    roleType,
    roleName,
    roleCode,
    login,
    logout,
    hasPermission
  }
})

