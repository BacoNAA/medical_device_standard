<template>
  <div class="standard-list-container">
    <!-- 头部导航 -->
    <div class="header">
      <div class="header-content">
        <h1>标准列表浏览</h1>
        <el-button-group>
          <el-button @click="$router.push('/import-preview')">导入预览</el-button>
          <el-button @click="$router.push('/equipment-manage')">设备管理</el-button>
        </el-button-group>
      </div>
    </div>

    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 搜索栏 -->
      <el-card class="search-card" shadow="hover">
        <el-form :inline="true">
          <el-form-item label="标准编号">
            <el-input v-model="searchForm.standardNo" placeholder="请输入标准编号" clearable />
          </el-form-item>
          <el-form-item label="标准名称">
            <el-input v-model="searchForm.standardName" placeholder="请输入标准名称" clearable />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">
              <el-icon><Search /></el-icon> 搜索
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon> 重置
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 标准列表 -->
      <el-card class="list-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span><el-icon><List /></el-icon> 标准列表（共 {{ standardList.length }} 条）</span>
          </div>
        </template>

        <el-table 
          :data="standardList" 
          stripe
          border
          style="width: 100%"
          @row-click="handleRowClick"
          :row-style="{ cursor: 'pointer' }"
        >
          <el-table-column label="标准编号" prop="standardNo" width="180">
            <template #default="{ row }">
              <el-tag type="primary">{{ row.standardNo }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="标准名称" prop="standardName" min-width="300" />
          <el-table-column label="发布日期" prop="publishDate" width="120" />
          <el-table-column label="实施日期" prop="effectiveDate" width="120" />
          <el-table-column label="检验项目数" prop="itemCount" width="120" align="center">
            <template #default="{ row }">
              <el-tag type="success" round>{{ row.itemCount }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="180" fixed="right">
            <template #default="{ row }">
              <el-button type="primary" size="small" @click.stop="viewDetail(row)">
                <el-icon><View /></el-icon> 查看详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { mockStandardList } from '../mock/standardData'

const router = useRouter()
const standardList = ref(mockStandardList)
const searchForm = ref({
  standardNo: '',
  standardName: ''
})

const handleSearch = () => {
  console.log('搜索条件：', searchForm.value)
}

const handleReset = () => {
  searchForm.value = {
    standardNo: '',
    standardName: ''
  }
}

const handleRowClick = (row) => {
  viewDetail(row)
}

const viewDetail = (row) => {
  router.push(`/standard-detail/${row.id}`)
}
</script>

<style scoped>
.standard-list-container {
  min-height: 100vh;
  background: linear-gradient(to bottom, #f5f7fa 0%, #ffffff 100%);
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px 0;
  box-shadow: 0 2px 12px rgba(0,0,0,0.1);
}

.header-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-content h1 {
  font-size: 24px;
  font-weight: 500;
  margin: 0;
}

.main-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 30px 20px;
}

.search-card,
.list-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 500;
}
</style>

