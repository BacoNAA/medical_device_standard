import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    redirect: '/import-preview',
    children: [
      {
        path: 'import-preview',
        name: 'ImportPreview',
        component: () => import('../views/ImportPreview.vue'),
        meta: { title: '标准导入预览' }
      },
      {
        path: 'standard-detail/:id',
        name: 'StandardDetail',
        component: () => import('../views/StandardDetail.vue'),
        meta: { title: '标准详情' }
      },
      {
        path: 'standard-list',
        name: 'StandardList',
        component: () => import('../views/StandardList.vue'),
        meta: { title: '标准列表' }
      },
      {
        path: 'equipment-manage',
        name: 'EquipmentManage',
        component: () => import('../views/EquipmentManage.vue'),
        meta: { title: '设备管理' }
      },
      {
        path: 'ptr-editor',
        name: 'PTREditor',
        component: () => import('../views/PTREditor/index.vue'),
        meta: { title: '产品技术要求编辑' }
      },
      {
        path: 'export-table1',
        name: 'ExportTable1',
        component: () => import('../views/Export/Table1.vue'),
        meta: { title: '能力建设信息网站项目表' }
      },
      {
        path: 'export-table2',
        name: 'ExportTable2',
        component: () => import('../views/Export/Table2.vue'),
        meta: { title: '认可检验检测能力表' }
      },
      {
        path: 'export-table3',
        name: 'ExportTable3',
        component: () => import('../views/Export/Table3.vue'),
        meta: { title: '操作规程表' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
