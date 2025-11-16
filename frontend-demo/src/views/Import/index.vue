<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">标准导入</h2>
      <p class="page-description">批量导入医疗器械标准数据（CSV/Excel格式）</p>
    </div>
    
    <!-- 导入步骤 -->
    <el-steps :active="currentStep" finish-status="success" align-center style="margin-bottom: 30px;">
      <el-step title="上传文件" icon="Upload" />
      <el-step title="系统解析" icon="Loading" />
      <el-step title="预览确认" icon="View" />
      <el-step title="完成导入" icon="CircleCheck" />
    </el-steps>
    
    <!-- 步骤1：上传文件 -->
    <el-card shadow="hover" v-show="currentStep === 0">
      <template #header>
        <span style="font-weight: bold;">第一步：上传标准文件</span>
      </template>
      
      <el-alert 
        title="文件要求说明" 
        type="info" 
        :closable="false"
        style="margin-bottom: 20px;"
      >
        <ul style="margin: 10px 0; padding-left: 20px;">
          <li>一个标准需要上传一组文件（10-15个）</li>
          <li>必需文件：标准信息.csv、检验要求.csv</li>
          <li>附录文件：附录A.csv、附录B.csv等（按标准实际包含的附录）</li>
          <li>可选文件：项目方法.csv、使用设备.csv（可减少人工审核工作）</li>
          <li>仅支持纯文本内容，不支持图片和图表</li>
        </ul>
      </el-alert>
      
      <el-upload
        drag
        multiple
        :auto-upload="false"
        :on-change="handleFileChange"
        :file-list="fileList"
        accept=".csv,.xlsx"
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">
          将文件拖到此处，或<em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            支持CSV和Excel格式，可同时上传多个文件
          </div>
        </template>
      </el-upload>
      
      <div style="margin-top: 20px; text-align: right;">
        <el-button type="primary" @click="nextStep" :disabled="fileList.length === 0">
          下一步：开始解析
        </el-button>
      </div>
    </el-card>
    
    <!-- 步骤2：系统解析 -->
    <el-card shadow="hover" v-show="currentStep === 1">
      <template #header>
        <span style="font-weight: bold;">第二步：系统自动解析</span>
      </template>
      
      <div style="text-align: center; padding: 60px 0;">
        <el-icon class="is-loading" :size="60" color="#409eff"><Loading /></el-icon>
        <div style="margin-top: 20px; font-size: 16px; color: #666;">
          正在解析文件，请稍候...
        </div>
        <div style="margin-top: 10px; color: #999;">
          系统正在自动识别项目方法和使用设备
        </div>
      </div>
    </el-card>
    
    <!-- 步骤3：预览确认 -->
    <el-card shadow="hover" v-show="currentStep === 2">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span style="font-weight: bold;">第三步：预览与确认</span>
          <div>
            <el-tag type="success">自动识别成功: 8</el-tag>
            <el-tag type="warning" style="margin-left: 10px;">需要确认: 2</el-tag>
            <el-tag type="danger" style="margin-left: 10px;">需要补充: 0</el-tag>
          </div>
        </div>
      </template>
      
      <el-alert 
        title="说明：绿色表示自动处理成功，黄色表示新设备需确认，红色表示需人工补充" 
        type="info" 
        :closable="false"
        style="margin-bottom: 20px;"
      />
      
      <el-table :data="previewData" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="clauseNo" label="条款号" width="100" align="center" />
        <el-table-column prop="itemName" label="项目名称" width="150" />
        <el-table-column prop="requirement" label="项目要求" min-width="250" show-overflow-tooltip />
        <el-table-column label="方法识别" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="row.methodStatus === 'success' ? 'success' : 'warning'">
              {{ row.methodStatus === 'success' ? '已识别' : '待确认' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="设备识别" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="row.equipmentStatus === 'success' ? 'success' : 'warning'">
              {{ row.equipmentStatus === 'success' ? '已识别' : '待确认' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="center">
          <template #default="{ row }">
            <el-button 
              type="primary" 
              link 
              v-if="row.methodStatus === 'warning' || row.equipmentStatus === 'warning'"
            >
              编辑
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div style="margin-top: 20px; text-align: right;">
        <el-button @click="prevStep">上一步</el-button>
        <el-button type="primary" @click="confirmImport">确认导入</el-button>
      </div>
    </el-card>
    
    <!-- 步骤4：完成 -->
    <el-card shadow="hover" v-show="currentStep === 3">
      <el-result
        icon="success"
        title="导入成功"
        sub-title="标准数据已成功导入系统"
      >
        <template #extra>
          <el-button type="primary" @click="$router.push('/standards')">查看标准列表</el-button>
          <el-button @click="resetImport">继续导入</el-button>
        </template>
      </el-result>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const currentStep = ref(0)
const fileList = ref([])

const previewData = ref([
  {
    clauseNo: '5.1',
    itemName: '外观',
    requirement: '注射器应清洁，无肉眼可见污染物。',
    methodStatus: 'success',
    equipmentStatus: 'success'
  },
  {
    clauseNo: '5.7.1',
    itemName: '残留容量',
    requirement: '按附录 B 中 B.2 试验，在活塞被完全推入后...',
    methodStatus: 'success',
    equipmentStatus: 'warning'
  },
  {
    clauseNo: '5.7.2',
    itemName: '器身密合性',
    requirement: '按附录 B 中 B.3 试验，注射器的器身密合性...',
    methodStatus: 'warning',
    equipmentStatus: 'success'
  }
])

const handleFileChange = (file, files) => {
  fileList.value = files
}

const nextStep = () => {
  if (currentStep.value === 0) {
    currentStep.value = 1
    // 模拟解析
    setTimeout(() => {
      currentStep.value = 2
      ElMessage.success('文件解析完成')
    }, 2000)
  }
}

const prevStep = () => {
  if (currentStep.value > 0) {
    currentStep.value--
  }
}

const confirmImport = () => {
  currentStep.value = 3
  ElMessage.success('导入成功')
}

const resetImport = () => {
  currentStep.value = 0
  fileList.value = []
}
</script>

