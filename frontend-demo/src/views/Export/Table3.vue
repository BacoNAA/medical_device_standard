<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">操作规程表（表3）</h2>
      <p class="page-description">导出实验室操作规程所需的完整项目信息表</p>
    </div>
    
    <!-- 筛选条件 -->
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <template #header>
        <span style="font-weight: bold;">筛选条件</span>
      </template>
      
      <el-form :model="filterForm" inline>
        <el-form-item label="选择标准">
          <el-select v-model="filterForm.standardId" placeholder="请选择标准" style="width: 350px;">
            <el-option 
              v-for="std in standards" 
              :key="std.id" 
              :label="`${std.standardNo} ${std.standardName}`"
              :value="std.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="项目名称">
          <el-input v-model="filterForm.itemName" placeholder="筛选项目名称" clearable style="width: 200px;" />
        </el-form-item>
        
        <el-form-item label="条款编号">
          <el-input v-model="filterForm.clauseNo" placeholder="如：5.7" clearable style="width: 150px;" />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="loadData">
            <el-icon><Refresh /></el-icon> 刷新数据
          </el-button>
          <el-button type="success" @click="exportExcel">
            <el-icon><Download /></el-icon> 导出Excel
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 说明卡片 -->
    <el-alert 
      title="导出说明"
      type="info"
      :closable="false"
      style="margin-bottom: 20px;"
    >
      <ul style="margin: 5px 0; padding-left: 20px;">
        <li>表格包含完整的检验项目信息：标准编号、标准名称、项目条款、项目名称、项目要求、使用设备、项目方法、注意事项</li>
        <li>使用设备格式：设备名称（规格要求），多个设备用分号分隔</li>
        <li>长文本内容将在Excel中自动换行显示</li>
      </ul>
    </el-alert>
    
    <!-- 预览表格 -->
    <el-card shadow="hover">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span style="font-weight: bold;">数据预览</span>
          <el-tag type="success">共 {{ tableData.length }} 条记录</el-tag>
        </div>
      </template>
      
      <el-table :data="tableData" border stripe max-height="550">
        <el-table-column type="index" label="序号" width="60" align="center" fixed />
        <el-table-column prop="standardNo" label="标准编号" width="160" fixed />
        <el-table-column prop="standardName" label="标准名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="clauseNo" label="项目条款" width="100" align="center" />
        <el-table-column prop="itemName" label="项目名称" width="150" />
        <el-table-column prop="requirement" label="项目要求" min-width="300" show-overflow-tooltip />
        <el-table-column label="使用设备" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <div v-if="row.equipments.length > 0">
              <el-tag 
                v-for="(eq, idx) in row.equipments" 
                :key="idx"
                size="small"
                type="success"
                style="margin-right: 5px; margin-bottom: 3px;"
              >
                {{ eq }}
              </el-tag>
            </div>
            <span v-else style="color: #999;">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="methodContent" label="项目方法" min-width="300" show-overflow-tooltip />
        <el-table-column label="注意事项" min-width="200" show-overflow-tooltip>
          <template #default="{ row }">
            <span :style="{ color: row.notes ? '#606266' : '#999' }">
              {{ row.notes || '无' }}
            </span>
          </template>
        </el-table-column>
      </el-table>
      
      <div style="margin-top: 15px; display: flex; justify-content: space-between; align-items: center;">
        <div style="color: #999; font-size: 12px;">
          <el-icon><InfoFilled /></el-icon>
          Excel导出时长文本将自动换行，列宽自动调整
        </div>
        <el-button type="text" @click="showPreviewTip">
          <el-icon><QuestionFilled /></el-icon> 查看导出格式说明
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// Mock标准数据
const mockStandards = [
  {
    id: 1,
    standardNo: 'GB 15810-2019',
    standardName: '一次性使用无菌注射器',
    items: [
      {
        itemId: 1,
        itemName: '外观',
        clauseNo: '5.1',
        requirement: '注射器应清洁，无肉眼可见污染物。',
        methodContent: '目视检查',
        equipments: [{ code: 'C001', name: '显微镜', spec: '放大倍数40倍' }],
        notes: ''
      },
      {
        itemId: 2,
        itemName: '残留容量',
        clauseNo: '5.7.1',
        requirement: '按附录 B 中 B.2 试验，残留容量应不大于标称容量的 4%。',
        methodContent: 'B.2 残留容量试验：将注射器活塞推至标称容量刻度处...',
        equipments: [{ code: 'A001', name: '电子天平', spec: '分度值0.1mg' }],
        notes: '关键性能指标'
      },
      {
        itemId: 3,
        itemName: '器身密合性',
        clauseNo: '5.7.2',
        requirement: '按附录 B 中 B.3 试验，注射器应无泄漏。',
        methodContent: 'B.3 器身密合性试验：将活塞推至标称容量刻度...',
        equipments: [{ code: 'B001', name: '注射器器身密合性测试仪', spec: '压力范围0-100kPa' }],
        notes: ''
      },
      {
        itemId: 4,
        itemName: '密封性',
        clauseNo: '5.3',
        requirement: '注射器应密封良好，无泄漏现象。',
        methodContent: '见附录',
        equipments: [
          { code: 'B001', name: '注射器器身密合性测试仪', spec: '压力范围0-100kPa' },
          { code: 'A002', name: '游标卡尺', spec: '精度0.02mm' }
        ],
        notes: ''
      }
    ]
  },
  {
    id: 2,
    standardNo: 'YY 0469-2023',
    standardName: '医用外科口罩',
    items: [
      {
        itemId: 5,
        itemName: '外观',
        clauseNo: '5.1',
        requirement: '口罩表面应清洁、无破损。',
        methodContent: '目视检查',
        equipments: [{ code: 'C001', name: '显微镜', spec: '放大倍数10倍' }],
        notes: ''
      },
      {
        itemId: 6,
        itemName: '细菌过滤效率',
        clauseNo: '5.2',
        requirement: 'BFE应≥95%。',
        methodContent: '按YY 0469标准附录进行测试',
        equipments: [{ code: 'B002', name: '高低温试验箱', spec: '温度范围-40~150℃' }],
        notes: '核心指标'
      }
    ]
  }
]

const filterForm = ref({
  standardId: null,
  itemName: '',
  clauseNo: ''
})

const standards = computed(() => mockStandards)

// 表格数据
const tableData = computed(() => {
  if (!filterForm.value.standardId) return []
  
  const standard = mockStandards.find(s => s.id === filterForm.value.standardId)
  if (!standard) return []
  
  let items = standard.items.map(item => {
    // 格式化设备显示："设备名称（规格要求）"
    const equipments = item.equipments ? item.equipments.map(eq => 
      eq.spec ? `${eq.name}（${eq.spec}）` : eq.name
    ) : []
    
    return {
      standardNo: standard.standardNo,
      standardName: standard.standardName,
      clauseNo: item.clauseNo,
      itemName: item.itemName,
      requirement: item.requirement,
      equipments: equipments,
      methodContent: item.methodContent || '见附录',
      notes: item.notes || ''
    }
  })
  
  // 按项目名称筛选
  if (filterForm.value.itemName) {
    items = items.filter(item => item.itemName.includes(filterForm.value.itemName))
  }
  
  // 按条款编号筛选
  if (filterForm.value.clauseNo) {
    items = items.filter(item => item.clauseNo.includes(filterForm.value.clauseNo))
  }
  
  return items
})

const loadData = () => {
  ElMessage.success('数据已刷新')
}

const exportExcel = () => {
  if (tableData.value.length === 0) {
    ElMessage.warning('没有数据可导出')
    return
  }
  
  ElMessage.success(`已导出 ${tableData.value.length} 条记录（Demo）`)
}

const showPreviewTip = () => {
  ElMessageBox.alert(
    `
    <div style="line-height: 1.8;">
      <h4 style="margin-top: 0;">表3：操作规程表 - Excel导出格式</h4>
      <p><strong>表头行：</strong>加粗、背景色填充</p>
      <p><strong>列宽：</strong>自动调整适应内容</p>
      <p><strong>长文本：</strong>自动换行显示</p>
      <p><strong>边框：</strong>所有单元格添加边框</p>
      <p><strong>对齐方式：</strong>表头居中，数据左对齐</p>
      <p><strong>使用设备格式：</strong>电子天平（分度值0.1mg）；游标卡尺（精度0.02mm）</p>
      <p style="color: #409eff; margin-bottom: 0;"><strong>文件命名：</strong>操作规程表_[标准编号]_[日期].xlsx</p>
    </div>
    `,
    '导出格式说明',
    {
      dangerouslyUseHTMLString: true,
      confirmButtonText: '知道了'
    }
  )
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
</style>
