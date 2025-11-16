<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">设备管理</h2>
      <p class="page-description">管理实验室检验设备信息</p>
    </div>
    
    <!-- 工具栏 -->
    <div class="table-actions">
      <el-button type="primary" @click="showAddDialog">
        <el-icon><Plus /></el-icon> 新增设备
      </el-button>
      
      <el-input 
        v-model="searchKeyword" 
        placeholder="搜索设备名称或编号" 
        style="width: 300px;"
        clearable
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
    </div>
    
    <!-- 设备列表 -->
    <el-table :data="filteredEquipments" border stripe>
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column prop="equipmentCode" label="设备编号" width="120" align="center" />
      <el-table-column prop="equipmentName" label="设备名称" width="200" />
      <el-table-column prop="model" label="规格型号" width="180" />
      <el-table-column prop="description" label="用途说明" min-width="250" show-overflow-tooltip />
      <el-table-column label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" align="center" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" link @click="editEquipment(row)">
            <el-icon><Edit /></el-icon> 编辑
          </el-button>
          <el-button 
            :type="row.status === 1 ? 'warning' : 'success'" 
            link 
            @click="toggleStatus(row)"
          >
            {{ row.status === 1 ? '停用' : '启用' }}
          </el-button>
          <el-button type="danger" link @click="deleteEquipment(row)">
            <el-icon><Delete /></el-icon> 删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 新增/编辑对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="isEdit ? '编辑设备' : '新增设备'"
      width="600px"
    >
      <el-form :model="equipmentForm" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="设备编号" prop="equipmentCode">
          <el-input 
            v-model="equipmentForm.equipmentCode" 
            placeholder="格式：大写字母+3位数字，如A001"
            :disabled="isEdit"
          >
            <template #append>
              <el-button @click="suggestCode" v-if="!isEdit">建议编号</el-button>
            </template>
          </el-input>
        </el-form-item>
        
        <el-form-item label="设备名称" prop="equipmentName">
          <el-input v-model="equipmentForm.equipmentName" placeholder="请输入设备名称" />
        </el-form-item>
        
        <el-form-item label="规格型号">
          <el-input v-model="equipmentForm.model" placeholder="请输入规格型号（可选）" />
        </el-form-item>
        
        <el-form-item label="用途说明">
          <el-input 
            v-model="equipmentForm.description" 
            type="textarea" 
            :rows="3"
            placeholder="请输入用途说明（可选）"
          />
        </el-form-item>
        
        <el-form-item label="状态">
          <el-radio-group v-model="equipmentForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEquipment">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// Mock设备数据
const mockEquipments = ref([
  {
    id: 1,
    equipmentCode: 'D001',
    equipmentName: '电子天平',
    model: 'BSA224S',
    description: '精度0.0001g',
    status: 1
  },
  {
    id: 2,
    equipmentCode: 'D002',
    equipmentName: '游标卡尺',
    model: 'VC-150',
    description: '测量范围0-150mm',
    status: 1
  },
  {
    id: 3,
    equipmentCode: 'D003',
    equipmentName: '恒温水浴锅',
    model: 'HH-4',
    description: '温度范围室温-100℃',
    status: 1
  }
])

const searchKeyword = ref('')
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()

const equipmentForm = ref({
  equipmentCode: '',
  equipmentName: '',
  model: '',
  description: '',
  status: 1
})

const rules = {
  equipmentCode: [
    { required: true, message: '请输入设备编号', trigger: 'blur' },
    { pattern: /^[A-Z]\d{3}$/, message: '格式错误，应为大写字母+3位数字', trigger: 'blur' }
  ],
  equipmentName: [
    { required: true, message: '请输入设备名称', trigger: 'blur' }
  ]
}

const filteredEquipments = computed(() => {
  if (!searchKeyword.value) {
    return mockEquipments.value
  }
  return mockEquipments.value.filter(e =>
    e.equipmentCode.toLowerCase().includes(searchKeyword.value.toLowerCase()) ||
    e.equipmentName.toLowerCase().includes(searchKeyword.value.toLowerCase())
  )
})

const showAddDialog = () => {
  isEdit.value = false
  equipmentForm.value = {
    equipmentCode: '',
    equipmentName: '',
    model: '',
    description: '',
    status: 1
  }
  dialogVisible.value = true
}

const editEquipment = (row) => {
  isEdit.value = true
  equipmentForm.value = { ...row }
  dialogVisible.value = true
}

const suggestCode = () => {
  // 简单的编号建议逻辑
  const codes = mockEquipments.value.map(e => e.equipmentCode)
  let letter = 'A'
  let num = 1
  
  while (codes.includes(`${letter}${String(num).padStart(3, '0')}`)) {
    num++
    if (num > 999) {
      letter = String.fromCharCode(letter.charCodeAt(0) + 1)
      num = 1
    }
  }
  
  equipmentForm.value.equipmentCode = `${letter}${String(num).padStart(3, '0')}`
}

const saveEquipment = async () => {
  await formRef.value.validate((valid) => {
    if (valid) {
      if (isEdit.value) {
        const index = mockEquipments.value.findIndex(e => e.id === equipmentForm.value.id)
        if (index !== -1) {
          mockEquipments.value[index] = { ...equipmentForm.value }
        }
        ElMessage.success('设备信息已更新')
      } else {
        const newId = Math.max(...mockEquipments.value.map(e => e.id), 0) + 1
        mockEquipments.value.push({ ...equipmentForm.value, id: newId })
        ElMessage.success('设备已添加')
      }
      dialogVisible.value = false
    }
  })
}

const toggleStatus = (row) => {
  row.status = row.status === 1 ? 0 : 1
  ElMessage.success(row.status === 1 ? '已启用' : '已停用')
}

const deleteEquipment = (row) => {
  ElMessageBox.confirm('确定要删除该设备吗？', '提示', {
    type: 'warning'
  }).then(() => {
    const index = mockEquipments.value.findIndex(e => e.id === row.id)
    if (index > -1) {
      mockEquipments.value.splice(index, 1)
      ElMessage.success('删除成功')
    }
  }).catch(() => {})
}
</script>

