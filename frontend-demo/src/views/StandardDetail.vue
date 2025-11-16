<template>
  <div class="standard-detail-container">
    <!-- 头部导航 -->
    <div class="header">
      <div class="header-content">
        <div class="header-left">
          <el-button @click="$router.back()" circle>
            <el-icon><ArrowLeft /></el-icon>
          </el-button>
          <div class="title-area">
            <h1>{{ standardData.standardInfo.standardName }}</h1>
            <div class="subtitle">
              <el-tag type="primary">{{ standardData.standardInfo.standardNo }}</el-tag>
              <span class="date-info">发布日期：{{ standardData.standardInfo.publishDate }}</span>
              <span class="date-info">实施日期：{{ standardData.standardInfo.effectiveDate }}</span>
            </div>
          </div>
        </div>
        <el-button-group>
          <el-button type="primary">
            <el-icon><Download /></el-icon> 导出
          </el-button>
          <el-button>
            <el-icon><Warning /></el-icon> 报告错误
          </el-button>
        </el-button-group>
      </div>
    </div>

    <!-- 主内容区 -->
    <div class="main-content">
      <!-- 标准基本信息卡片 -->
      <el-card class="info-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <span><el-icon><InfoFilled /></el-icon> 标准基本信息</span>
          </div>
        </template>
        <el-descriptions :column="2" border size="large">
          <el-descriptions-item label="标准编号">
            <el-tag type="primary" size="large">{{ standardData.standardInfo.standardNo }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="标准名称（中文）">
            {{ standardData.standardInfo.standardName }}
          </el-descriptions-item>
          <el-descriptions-item label="标准名称（英文）" :span="2">
            {{ standardData.standardInfo.standardNameEN }}
          </el-descriptions-item>
          <el-descriptions-item label="国际标准引用">
            {{ standardData.standardInfo.isoReference }}
          </el-descriptions-item>
          <el-descriptions-item label="发布日期">
            {{ standardData.standardInfo.publishDate }}
          </el-descriptions-item>
          <el-descriptions-item label="实施日期" :span="2">
            {{ standardData.standardInfo.effectiveDate }}
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- 标签页内容 -->
      <el-card class="tabs-card" shadow="hover">
        <el-tabs v-model="activeTab" type="border-card" class="content-tabs">
          <!-- 检验项目标签页 -->
          <el-tab-pane label="检验项目" name="items">
            <div class="tab-header">
              <div class="tab-info">
                <el-icon><List /></el-icon>
                <span>共 {{ standardData.requirementItems.length }} 个检验项目</span>
              </div>
              <el-input 
                v-model="searchKeyword" 
                placeholder="搜索项目名称或条款"
                style="width: 300px"
                clearable
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
            </div>

            <el-table 
              :data="filteredRequirementItems" 
              stripe
              border
              style="width: 100%"
              :default-expand-all="false"
            >
              <el-table-column type="expand">
                <template #default="{ row }">
                  <div class="expand-content">
                    <div class="expand-section">
                      <div class="section-title">
                        <el-icon><Document /></el-icon> 项目要求
                      </div>
                      <div class="section-content">{{ row.requirement }}</div>
                    </div>

                    <div class="expand-section" v-if="row.methodContent || row.methodRef">
                      <div class="section-title">
                        <el-icon><Reading /></el-icon> 检验方法
                      </div>
                      <div class="section-content">
                        <el-tag v-if="row.methodType === 2" type="warning" size="small" style="margin-bottom: 10px">
                          引用附录：{{ row.methodRef }}
                        </el-tag>
                        <div>{{ row.methodContent || '详见附录' + row.methodRef }}</div>
                      </div>
                    </div>

                    <div class="expand-section" v-if="row.equipments && row.equipments.length > 0">
                      <div class="section-title">
                        <el-icon><Tools /></el-icon> 使用设备
                      </div>
                      <div class="section-content">
                        <div v-for="(eq, index) in row.equipments" :key="index" class="equipment-item">
                          <el-tag type="success">{{ eq.code }}</el-tag>
                          <span class="eq-name">{{ eq.name }}</span>
                          <el-tag size="small" type="info">{{ eq.specification }}</el-tag>
                        </div>
                      </div>
                    </div>

                    <div class="expand-section" v-if="row.notes">
                      <div class="section-title">
                        <el-icon><Warning /></el-icon> 注意事项
                      </div>
                      <div class="section-content notes">{{ row.notes }}</div>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column label="项目条款" prop="clauseNo" width="100" />
              <el-table-column label="项目名称" prop="itemName" width="180" />
              <el-table-column label="项目要求" prop="requirement" show-overflow-tooltip min-width="300" />
              <el-table-column label="方法类型" width="120">
                <template #default="{ row }">
                  <el-tag v-if="row.methodType === 1" size="small">无独立方法</el-tag>
                  <el-tag v-else-if="row.methodType === 2" size="small" type="warning">引用附录</el-tag>
                  <el-tag v-else size="small" type="success">完整方法</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="使用设备" min-width="200">
                <template #default="{ row }">
                  <div v-if="row.equipments && row.equipments.length > 0">
                    <el-tag 
                      v-for="(eq, index) in row.equipments" 
                      :key="index" 
                      size="small"
                      type="success"
                      style="margin: 2px"
                    >
                      {{ eq.name }}
                    </el-tag>
                  </div>
                  <el-text v-else type="info" size="small">无</el-text>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>

          <!-- 说明性内容标签页 -->
          <el-tab-pane label="说明性内容" name="description">
            <el-collapse v-model="activeCollapse" accordion>
              <!-- 适用范围 -->
              <el-collapse-item title="1. 适用范围" name="scope">
                <div class="collapse-content">
                  {{ standardData.standardInfo.scope }}
                </div>
              </el-collapse-item>

              <!-- 规范性引用文件 -->
              <el-collapse-item title="2. 规范性引用文件" name="references">
                <div class="collapse-content reference-list">
                  <div v-for="(ref, index) in standardData.standardInfo.references.split('\n')" :key="index" class="reference-item">
                    {{ ref }}
                  </div>
                </div>
              </el-collapse-item>

              <!-- 术语和定义 -->
              <el-collapse-item title="3. 术语和定义" name="terms">
                <div class="collapse-content">
                  <el-card v-for="term in standardData.terms" :key="term.termCode" class="term-card" shadow="hover">
                    <div class="term-header">
                      <el-tag type="primary">{{ term.termCode }}</el-tag>
                      <span class="term-name">{{ term.termName }}</span>
                      <span class="term-name-en" v-if="term.termNameEN">{{ term.termNameEN }}</span>
                    </div>
                    <div class="term-definition">{{ term.definition }}</div>
                    <div v-if="term.example" class="term-example">
                      <el-text type="info">示例：{{ term.example }}</el-text>
                    </div>
                  </el-card>
                </div>
              </el-collapse-item>

              <!-- 前言 -->
              <el-collapse-item title="前言" name="foreword">
                <div class="collapse-content">
                  {{ standardData.standardInfo.foreword }}
                </div>
              </el-collapse-item>

              <!-- 引言 -->
              <el-collapse-item title="引言" name="introduction">
                <div class="collapse-content">
                  {{ standardData.standardInfo.introduction }}
                </div>
              </el-collapse-item>
            </el-collapse>
          </el-tab-pane>

          <!-- 附录内容标签页 -->
          <el-tab-pane label="附录内容" name="appendix">
            <div class="appendix-list">
              <el-card 
                v-for="appendix in standardData.appendices" 
                :key="appendix.appendixCode" 
                class="appendix-card"
                shadow="hover"
              >
                <template #header>
                  <div class="appendix-header">
                    <div class="appendix-title">
                      <el-tag type="warning" size="large">附录 {{ appendix.appendixCode }}</el-tag>
                      <span class="appendix-name">{{ appendix.appendixTitle }}</span>
                    </div>
                    <el-tag size="small" :type="appendix.appendixType === '规范性' ? 'danger' : 'info'">
                      {{ appendix.appendixType }}附录
                    </el-tag>
                  </div>
                </template>

                <div class="appendix-content">
                  <div 
                    v-for="section in appendix.sections" 
                    :key="section.sectionNo" 
                    class="appendix-section"
                  >
                    <div class="section-header">
                      <el-tag size="small">{{ section.sectionNo }}</el-tag>
                      <span class="section-title">{{ section.sectionTitle }}</span>
                    </div>
                    <div class="section-text" v-html="formatContent(section.content)"></div>
                  </div>
                </div>
              </el-card>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { mockStandardData } from '../mock/standardData'

const route = useRoute()
const standardData = ref(mockStandardData)
const activeTab = ref('items')
const activeCollapse = ref('scope')
const searchKeyword = ref('')

// 过滤后的检验项目
const filteredRequirementItems = computed(() => {
  if (!searchKeyword.value) {
    return standardData.value.requirementItems
  }
  const keyword = searchKeyword.value.toLowerCase()
  return standardData.value.requirementItems.filter(item => 
    item.itemName.toLowerCase().includes(keyword) ||
    item.clauseNo.toLowerCase().includes(keyword) ||
    item.requirement.toLowerCase().includes(keyword)
  )
})

// 格式化内容（处理换行和表格）
const formatContent = (content) => {
  return content.replace(/\n/g, '<br>')
}
</script>

<style scoped>
.standard-detail-container {
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

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
  flex: 1;
}

.title-area h1 {
  font-size: 24px;
  font-weight: 500;
  margin: 0 0 8px 0;
}

.subtitle {
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 14px;
  opacity: 0.9;
}

.date-info {
  display: flex;
  align-items: center;
}

.main-content {
  max-width: 1400px;
  margin: 0 auto;
  padding: 30px 20px;
}

.info-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 500;
}

.tabs-card {
  margin-bottom: 20px;
}

.tab-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
}

.tab-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 500;
}

/* 展开行样式 */
.expand-content {
  padding: 20px;
  background: #fafafa;
}

.expand-section {
  margin-bottom: 20px;
}

.expand-section:last-child {
  margin-bottom: 0;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  font-weight: 600;
  color: #409eff;
  margin-bottom: 10px;
  padding-bottom: 8px;
  border-bottom: 2px solid #e4e7ed;
}

.section-content {
  padding-left: 24px;
  line-height: 1.8;
  color: #333;
}

.section-content.notes {
  color: #e6a23c;
  font-style: italic;
}

.equipment-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 0;
}

.eq-name {
  font-weight: 500;
}

/* 折叠面板样式 */
.collapse-content {
  padding: 20px;
  line-height: 1.8;
  color: #333;
  white-space: pre-wrap;
}

.reference-list {
  padding: 0;
}

.reference-item {
  padding: 10px 20px;
  border-bottom: 1px solid #e4e7ed;
}

.reference-item:last-child {
  border-bottom: none;
}

/* 术语卡片 */
.term-card {
  margin-bottom: 15px;
}

.term-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.term-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.term-name-en {
  font-size: 14px;
  color: #909399;
  font-style: italic;
}

.term-definition {
  line-height: 1.8;
  color: #606266;
  margin-bottom: 10px;
}

.term-example {
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
}

/* 附录样式 */
.appendix-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.appendix-card {
  background: linear-gradient(to right, #fff 0%, #fafbfc 100%);
}

.appendix-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.appendix-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.appendix-name {
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.appendix-content {
  padding: 10px 0;
}

.appendix-section {
  margin-bottom: 25px;
}

.appendix-section:last-child {
  margin-bottom: 0;
}

.appendix-section .section-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 2px solid #e4e7ed;
}

.appendix-section .section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0;
  padding: 0;
  border: none;
}

.appendix-section .section-text {
  line-height: 2;
  color: #606266;
  padding-left: 32px;
}

:deep(.el-tabs__content) {
  padding: 20px;
}
</style>

