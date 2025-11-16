<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">认可检验检测能力表（表2）</h2>
      <p class="page-description">导出检验能力认可所需的设备能力表，支持按设备大类和规格要求两级筛选</p>
    </div>
    
    <!-- 筛选条件 -->
    <el-card shadow="hover" style="margin-bottom: 20px;">
      <template #header>
        <span style="font-weight: bold;">筛选条件</span>
      </template>
      
      <el-form :model="filterForm" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="选择标准">
              <el-select v-model="filterForm.standardId" placeholder="请选择标准" style="width: 100%;">
                <el-option 
                  v-for="std in standards" 
                  :key="std.id" 
                  :label="`${std.standardNo} ${std.standardName}`"
                  :value="std.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          
          <el-col :span="12">
            <el-form-item label="项目名称">
              <el-input v-model="filterForm.itemName" placeholder="筛选项目名称" clearable />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-divider content-position="left">
          <el-icon><Filter /></el-icon> 按设备筛选（两级筛选）
        </el-divider>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="第一级：设备大类">
              <el-select 
                v-model="filterForm.equipmentCode" 
                placeholder="选择设备类型"
                style="width: 100%;"
                clearable
                @change="onEquipmentChange"
              >
                <el-option label="（全部设备）" :value="null" />
                <el-option 
                  v-for="eq in equipmentList" 
                  :key="eq.code" 
                  :label="`${eq.code} - ${eq.name}`"
                  :value="eq.code"
                />
              </el-select>
            </el-form-item>
          </el-col>
          
          <el-col :span="12">
            <el-form-item label="第二级：规格要求">
              <el-select 
                v-model="filterForm.specRequirement" 
                placeholder="选择规格要求"
                style="width: 100%;"
                clearable
                :disabled="!filterForm.equipmentCode"
              >
                <el-option label="（所有规格）" :value="null" />
                <el-option 
                  v-for="spec in availableSpecs" 
                  :key="spec" 
                  :label="spec"
                  :value="spec"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item>
          <el-button type="primary" @click="loadData">
            <el-icon><Refresh /></el-icon> 刷新数据
          </el-button>
          <el-button type="success" @click="exportExcel">
            <el-icon><Download /></el-icon> 导出Excel
          </el-button>
          <el-button @click="resetFilter">
            <el-icon><CircleClose /></el-icon> 重置筛选
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <!-- 说明卡片 -->
    <el-alert 
      title="筛选说明"
      type="info"
      :closable="false"
      style="margin-bottom: 20px;"
    >
      <ul style="margin: 5px 0; padding-left: 20px;">
        <li>仅选择设备大类：筛选出使用该设备的所有项目（不限规格）</li>
        <li>选择设备+规格要求：精确筛选出使用该设备且符合指定规格的项目</li>
        <li>不选择设备：导出所有项目</li>
        <li>导出格式：使用设备显示为"设备名称（规格要求）"，不显示设备编号</li>
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
      
      <el-table :data="tableData" border stripe max-height="500">
        <el-table-column type="index" label="序号" width="60" align="center" />
        <el-table-column prop="itemName" label="项目名称" min-width="150" />
        <el-table-column prop="standardName" label="标准名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="standardNoClause" label="标准编号+项目条款" min-width="180" />
        <el-table-column label="使用设备" min-width="250" show-overflow-tooltip>
          <template #default="{ row }">
            <el-tag 
              v-for="(eq, idx) in row.equipments" 
              :key="idx"
              size="small"
              style="margin-right: 5px; margin-bottom: 5px;"
            >
              {{ eq }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      
      <div style="margin-top: 15px; text-align: right; color: #999; font-size: 12px;">
        <el-icon><InfoFilled /></el-icon>
        导出时设备格式为：电子天平（分度值0.1mg）；游标卡尺（精度0.02mm）
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'

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
        equipments: [
          { code: 'C001', name: '显微镜', spec: '放大倍数40倍' }
        ]
      },
      {
        itemId: 2,
        itemName: '残留容量',
        clauseNo: '5.7.1',
        equipments: [
          { code: 'A001', name: '电子天平', spec: '分度值0.1mg' }
        ]
      },
      {
        itemId: 3,
        itemName: '器身密合性',
        clauseNo: '5.7.2',
        equipments: [
          { code: 'B001', name: '注射器器身密合性测试仪', spec: '压力范围0-100kPa' }
        ]
      },
      {
        itemId: 4,
        itemName: '密封性',
        clauseNo: '5.3',
        equipments: [
          { code: 'B001', name: '注射器器身密合性测试仪', spec: '压力范围0-100kPa' },
          { code: 'A002', name: '游标卡尺', spec: '精度0.02mm' }
        ]
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
        equipments: [
          { code: 'C001', name: '显微镜', spec: '放大倍数10倍' }
        ]
      },
      {
        itemId: 6,
        itemName: '细菌过滤效率',
        clauseNo: '5.2',
        equipments: [
          { code: 'B002', name: '高低温试验箱', spec: '温度范围-40~150℃' }
        ]
      }
    ]
  }
]

const filterForm = ref({
  standardId: null,
  itemName: '',
  equipmentCode: null,
  specRequirement: null
})

const standards = computed(() => mockStandards)

// 设备列表（Mock数据）
const equipmentList = ref([
  { code: 'A001', name: '电子天平' },
  { code: 'A002', name: '游标卡尺' },
  { code: 'B001', name: '注射器器身密合性测试仪' },
  { code: 'B002', name: '高低温试验箱' },
  { code: 'C001', name: '显微镜' }
])

// 当前设备的可用规格列表（动态生成）
const availableSpecs = computed(() => {
  if (!filterForm.value.equipmentCode || !filterForm.value.standardId) return []
  
  const standard = mockStandards.find(s => s.id === filterForm.value.standardId)
  if (!standard) return []
  
  const specs = new Set()
  standard.items.forEach(item => {
    if (item.equipments) {
      item.equipments.forEach(eq => {
        if (eq.code === filterForm.value.equipmentCode && eq.spec) {
          specs.add(eq.spec)
        }
      })
    }
  })
  
  return Array.from(specs)
})

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
      itemName: item.itemName,
      standardName: standard.standardName,
      standardNoClause: `${standard.standardNo} ${item.clauseNo}`,
      equipments: equipments,
      _equipmentCodes: item.equipments ? item.equipments.map(eq => eq.code) : [],
      _equipmentSpecs: item.equipments ? item.equipments.map(eq => eq.spec) : []
    }
  })
  
  // 按项目名称筛选
  if (filterForm.value.itemName) {
    items = items.filter(item => item.itemName.includes(filterForm.value.itemName))
  }
  
  // 按设备大类筛选
  if (filterForm.value.equipmentCode) {
    items = items.filter(item => 
      item._equipmentCodes.includes(filterForm.value.equipmentCode)
    )
  }
  
  // 按规格要求筛选
  if (filterForm.value.specRequirement) {
    items = items.filter(item => 
      item._equipmentSpecs.includes(filterForm.value.specRequirement)
    )
  }
  
  return items
})

const onEquipmentChange = () => {
  // 切换设备时清空规格筛选
  filterForm.value.specRequirement = null
}

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

const resetFilter = () => {
  filterForm.value = {
    standardId: filterForm.value.standardId,
    itemName: '',
    equipmentCode: null,
    specRequirement: null
  }
  ElMessage.info('筛选条件已重置')
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
