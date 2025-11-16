<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">产品技术要求编辑</h2>
      <p class="page-description">编制医疗器械产品技术要求文档，支持智能联想和模板插入</p>
    </div>
    
    <!-- 工具栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="showNewDialog">
        <el-icon><Plus /></el-icon> 新建文档
      </el-button>
      <el-button @click="backToList" v-if="currentDoc">
        <el-icon><ArrowLeft /></el-icon> 返回列表
      </el-button>
      <el-button @click="saveDocument" :disabled="!currentDoc" type="success">
        <el-icon><Document /></el-icon> 保存
      </el-button>
      <el-button @click="exportWord" :disabled="!currentDoc">
        <el-icon><Download /></el-icon> 导出Word
      </el-button>
      <el-button @click="generateChapter3" :disabled="!currentDoc" v-if="currentChapter.includes('2. 性能指标')">
        <el-icon><MagicStick /></el-icon> 生成第3章框架
      </el-button>
    </div>
    
    <!-- 文档列表 -->
    <el-card shadow="hover" v-if="!currentDoc">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span style="font-weight: bold;">我的文档</span>
          <el-tag type="info">共 {{ documents.length }} 份文档</el-tag>
        </div>
      </template>
      
      <el-table :data="documents" border stripe>
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="productName" label="产品名称" min-width="200" />
        <el-table-column prop="modelSpec" label="型号规格" min-width="180" show-overflow-tooltip />
        <el-table-column prop="version" label="版本" width="100" align="center" />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'warning'">
              {{ row.status === 1 ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updatedTime" label="更新时间" width="180" />
        <el-table-column label="操作" width="250" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="editDocument(row)">
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
            <el-button type="success" link @click="previewDocument(row)">
              <el-icon><View /></el-icon> 预览
            </el-button>
            <el-button type="info" link @click="exportWord">
              <el-icon><Download /></el-icon> 导出
            </el-button>
            <el-popconfirm title="确定删除该文档吗？" @confirm="deleteDocument(row)">
              <template #reference>
                <el-button type="danger" link>
                  <el-icon><Delete /></el-icon> 删除
                </el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 编辑器 -->
    <div v-else class="editor-container">
      <!-- 左侧章节树 -->
      <div class="editor-sidebar">
        <h4 style="margin-bottom: 15px;">文档章节</h4>
        <el-tree
          :data="chapters"
          :props="{ label: 'title' }"
          @node-click="handleNodeClick"
          highlight-current
        />
      </div>
      
      <!-- 中间编辑区 -->
      <div class="editor-main">
        <div style="border-bottom: 1px solid #e4e7ed; padding-bottom: 10px; margin-bottom: 15px; display: flex; justify-content: space-between; align-items: center;">
          <h3 style="margin: 0;">{{ currentChapter }}</h3>
          <el-tag type="success" size="small" v-if="autoSaved">
            <el-icon><SuccessFilled /></el-icon> 已自动保存
          </el-tag>
        </div>
        
        <!-- 产品基本信息 -->
        <div v-if="currentChapter.includes('1. 产品')" class="info-section">
          <el-form label-width="140px">
            <el-form-item label="产品名称">
              <el-input v-model="currentDoc.productName" placeholder="请输入产品名称" />
            </el-form-item>
            <el-form-item label="产品型号/规格">
              <el-input 
                v-model="currentDoc.modelSpec" 
                type="textarea" 
                :rows="6"
                placeholder="请输入产品型号/规格及其划分说明"
              />
            </el-form-item>
          </el-form>
        </div>
        
        <!-- 富文本编辑器 -->
        <div v-else>
          <el-input
            v-model="editorContent"
            type="textarea"
            :rows="22"
            placeholder="在此输入内容...&#10;&#10;提示：&#10;- 第2章（性能指标）：使用右侧「联想查找」快速插入标准要求&#10;- 第3章（检验方法）：完成第2章后可自动生成框架&#10;- 支持使用右侧「模板插入」快速输入标准语句"
          />
        </div>
        
        <div style="margin-top: 15px; display: flex; justify-content: space-between; align-items: center;">
          <div style="color: #999; font-size: 12px;">
            <el-icon><InfoFilled /></el-icon>
            自动保存时间：{{ lastSaveTime }}
          </div>
          <div style="color: #999; font-size: 12px;">
            字数统计：{{ editorContent.length }} 字
          </div>
        </div>
      </div>
      
      <!-- 右侧智能辅助 -->
      <div class="editor-assistant">
        <h4 style="margin-bottom: 15px;">智能辅助</h4>
        
        <el-tabs v-model="assistTab">
          <el-tab-pane label="模板插入" name="template">
            <el-button size="small" style="width: 100%; margin-bottom: 8px;" @click="insertTemplate(1)">
              试验结果应符合XX的要求
            </el-button>
            <el-button size="small" style="width: 100%; margin-bottom: 8px;" @click="insertTemplate(2)">
              按XX方法试验
            </el-button>
            <el-button size="small" style="width: 100%; margin-bottom: 8px;" @click="insertTemplate(3)">
              应符合YY/T XXXX标准
            </el-button>
          </el-tab-pane>
          
          <el-tab-pane label="联想查找" name="search">
            <el-input
              v-model="searchKeyword"
              placeholder="输入项目名称关键词"
              size="small"
              style="margin-bottom: 10px;"
              clearable
            >
              <template #prefix>
                <el-icon><Search /></el-icon>
              </template>
            </el-input>
            
            <el-alert
              v-if="!searchKeyword"
              title="输入项目名称关键词进行搜索"
              type="info"
              :closable="false"
              style="margin-bottom: 10px;"
            />
            
            <el-alert
              v-else-if="searchResults.length === 0"
              title="未找到相关项目"
              type="warning"
              :closable="false"
              style="margin-bottom: 10px;"
            />
            
            <div style="max-height: 450px; overflow-y: auto;">
              <div 
                v-for="item in searchResults" 
                :key="item.id"
                class="search-result-item"
              >
                <div style="font-weight: bold; margin-bottom: 5px; color: #409eff;">
                  {{ item.itemName }}
                </div>
                <div style="font-size: 12px; color: #999; margin-bottom: 5px;">
                  {{ item.standardNo }} {{ item.clauseNo }}
                </div>
                <div style="font-size: 12px; color: #666; margin-bottom: 8px; line-height: 1.5;">
                  {{ item.requirement.substring(0, 60) }}{{ item.requirement.length > 60 ? '...' : '' }}
                </div>
                <el-button-group size="small" style="width: 100%;">
                  <el-button style="flex: 1;" @click="insertRequirement(item)">
                    <el-icon><DocumentAdd /></el-icon> 插入要求
                  </el-button>
                  <el-button style="flex: 1;" @click="insertMethod(item)">
                    <el-icon><Memo /></el-icon> 插入方法
                  </el-button>
                </el-button-group>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
    
    <!-- 新建文档对话框 -->
    <el-dialog v-model="newDialogVisible" title="新建产品技术要求" width="500px">
      <el-form :model="newDocForm" label-width="120px">
        <el-form-item label="产品名称" required>
          <el-input v-model="newDocForm.productName" placeholder="请输入产品名称" />
        </el-form-item>
        <el-form-item label="产品型号规格" required>
          <el-input 
            v-model="newDocForm.modelSpec" 
            type="textarea"
            :rows="3"
            placeholder="请输入产品型号/规格及其划分说明"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="newDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="createDocument">创建</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

// Mock标准数据（用于联想查找）
const mockStandardItems = [
  {
    itemId: 1,
    standardNo: 'GB 15810-2019',
    clauseNo: '5.1',
    itemName: '外观',
    requirement: '注射器应清洁，无肉眼可见污染物。',
    methodContent: '目视检查'
  },
  {
    itemId: 2,
    standardNo: 'GB 15810-2019',
    clauseNo: '5.7.1',
    itemName: '残留容量',
    requirement: '按附录 B 中 B.2 试验，在活塞被完全推入后，在圆柱型部分、圆锥接头及任何附加装置内的残留容量应符合表 1 的规定。',
    methodContent: 'B.2 残留容量试验：将注射器安装在试验装置上，以稳定的速度推动活塞至底部，测量残留液体体积。'
  },
  {
    itemId: 3,
    standardNo: 'GB 15810-2019',
    clauseNo: '5.7.2',
    itemName: '器身密合性',
    requirement: '按附录 B 中 B.3 试验，注射器的器身密合性应符合表 2 的规定。',
    methodContent: 'B.3 器身密合性试验：将注射器固定在试验装置上，施加规定的压力，观察活塞滑动情况。'
  },
  {
    itemId: 4,
    standardNo: 'YY 0469-2023',
    clauseNo: '5.1',
    itemName: '外观',
    requirement: '口罩外观应整洁、形状完好，表面不得有破洞、污渍。',
    methodContent: '目视检查'
  },
  {
    itemId: 5,
    standardNo: 'YY 0469-2023',
    clauseNo: '5.2',
    itemName: '细菌过滤效率',
    requirement: '口罩的细菌过滤效率(BFE)应不小于95%。',
    methodContent: '将口罩样品固定在测试装置上，通入含菌气溶胶，采集过滤前后的空气样本，培养计数后计算过滤效率。测试流量为(28±2)L/min。'
  },
  {
    itemId: 6,
    standardNo: 'GB 15810-2019',
    clauseNo: '5.3',
    itemName: '密封性',
    requirement: '按附录 B 中 B.5 试验，注射器应无泄漏。',
    methodContent: 'B.5 密封性试验：将注射器浸入水中，施加压力，观察是否有气泡产生。'
  }
]

const currentDoc = ref(null)
const currentChapter = ref('1. 产品型号/规格及其划分说明')
const editorContent = ref('')
const assistTab = ref('template')
const searchKeyword = ref('')
const newDialogVisible = ref(false)
const autoSaved = ref(false)
const lastSaveTime = ref('未保存')

const newDocForm = ref({
  productName: '',
  modelSpec: ''
})

const documents = ref([
  {
    id: 1,
    productName: '一次性使用无菌注射器',
    modelSpec: '1ml、2ml、5ml、10ml、20ml、50ml',
    version: 'V1.0',
    status: 0,
    updatedTime: '2025-10-20 14:30:00'
  },
  {
    id: 2,
    productName: '医用外科口罩',
    modelSpec: '平面耳挂式、绑带式',
    version: 'V2.1',
    status: 1,
    updatedTime: '2025-10-18 10:15:00'
  }
])

// 自动保存功能
let autoSaveTimer = null
watch(() => editorContent.value, () => {
  autoSaved.value = false
  if (autoSaveTimer) clearTimeout(autoSaveTimer)
  autoSaveTimer = setTimeout(() => {
    autoSaved.value = true
    lastSaveTime.value = new Date().toLocaleTimeString()
  }, 2000)
})

const chapters = ref([
  { title: '1. 产品型号/规格及其划分说明' },
  { title: '2. 性能指标' },
  { title: '3. 检验方法' },
  { title: '4. 术语' },
  { title: '5. 附录' }
])

const searchResults = computed(() => {
  if (!searchKeyword.value) return []
  
  // 从Mock数据中搜索
  const results = mockStandardItems.filter(item => 
    item.itemName.includes(searchKeyword.value)
  )
  
  return results.slice(0, 5)
})

const handleNodeClick = (data) => {
  currentChapter.value = data.title
}

const showNewDialog = () => {
  newDocForm.value = { productName: '', modelSpec: '' }
  newDialogVisible.value = true
}

const createDocument = () => {
  if (!newDocForm.value.productName) {
    ElMessage.warning('请输入产品名称')
    return
  }
  
  currentDoc.value = {
    productName: newDocForm.value.productName,
    modelSpec: newDocForm.value.modelSpec,
    version: 'V1.0'
  }
  
  editorContent.value = newDocForm.value.modelSpec
  newDialogVisible.value = false
  ElMessage.success('文档创建成功')
}

const editDocument = (doc) => {
  currentDoc.value = doc
  editorContent.value = '这里是文档内容...'
}

const saveDocument = () => {
  ElMessage.success('文档已保存')
}

const exportWord = () => {
  ElMessage.success('Word文档已导出（Demo）')
}

const versionManage = () => {
  ElMessage.info('版本管理功能（Demo）')
}

const deleteDocument = (doc) => {
  ElMessage.info('删除文档功能（Demo）')
}

const insertTemplate = (type) => {
  const templates = {
    1: '试验结果应符合XX的要求。',
    2: '按XX方法试验。',
    3: '应符合YY/T XXXX标准的规定。'
  }
  editorContent.value += templates[type]
  ElMessage.success('模板已插入')
}

const insertRequirement = (item) => {
  const text = `\n${item.itemName}：${item.requirement}\n（引用自 ${item.standardNo} ${item.clauseNo}）\n`
  editorContent.value += text
  ElMessage.success('项目要求已插入，并自动记录引用来源')
}

const insertMethod = (item) => {
  const text = `\n${item.itemName}：${item.methodContent || '按附录方法试验'}\n（引用自 ${item.standardNo} ${item.clauseNo}）\n`
  editorContent.value += text
  ElMessage.success('检验方法已插入，并自动记录引用来源')
}

const generateChapter3 = () => {
  ElMessage.success('第3章框架已生成（Demo功能）')
}

const backToList = () => {
  currentDoc.value = null
  editorContent.value = ''
  currentChapter.value = '1. 产品型号/规格及其划分说明'
}

const previewDocument = (doc) => {
  ElMessage.info(`预览文档：${doc.productName}（Demo功能）`)
}
</script>

<style scoped>
.page-container {
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  min-height: 100vh;
}

.page-header {
  background: white;
  padding: 24px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.page-title {
  margin: 0 0 8px 0;
  color: #303133;
  font-size: 24px;
}

.page-description {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.toolbar {
  background: white;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  gap: 10px;
}

.editor-container {
  display: flex;
  gap: 20px;
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  min-height: 700px;
}

.editor-sidebar {
  width: 20%;
  border-right: 1px solid #e4e7ed;
  padding-right: 15px;
}

.editor-main {
  flex: 1;
  padding: 0 15px;
}

.editor-assistant {
  width: 25%;
  border-left: 1px solid #e4e7ed;
  padding-left: 15px;
}

.search-result-item {
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  padding: 12px;
  margin-bottom: 12px;
  transition: all 0.3s;
  background: #fafafa;
}

.search-result-item:hover {
  border-color: #409eff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
  background: white;
}

.info-section {
  background: #f9f9f9;
  padding: 20px;
  border-radius: 6px;
}
</style>
