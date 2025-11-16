import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useStandardsStore = defineStore('standards', () => {
  // 模拟标准数据
  const standards = ref([
    {
      id: 1,
      standardNo: 'GB 15810-2019',
      standardName: '一次性使用无菌注射器',
      standardNameEN: 'Sterile hypodermic syringes for single use',
      publishDate: '2019-08-30',
      effectiveDate: '2020-07-01',
      scope: '本标准规定了一次性使用无菌注射器的要求、试验方法、标志、包装、运输和贮存。\n本标准适用于不带针的、以塑料为主要材料制成的一次性使用无菌注射器。',
      itemCount: 24,
      items: [
        {
          itemId: 1,
          clauseNo: '5.1',
          itemName: '外观',
          requirement: '注射器应清洁，无肉眼可见污染物。',
          methodType: 1,
          methodRef: '',
          methodContent: '',
          equipment: []
        },
        {
          itemId: 2,
          clauseNo: '5.7.1',
          itemName: '残留容量',
          requirement: '按附录 B 中 B.2 试验，在活塞被完全推入后，在圆柱型部分、圆锥接头及任何附加装置内的残留容量应符合表 1 的规定。',
          methodType: 2,
          methodRef: '附录B',
          methodContent: 'B.2 残留容量试验：将注射器安装在试验装置上，以稳定的速度推动活塞至底部，测量残留液体体积。',
          equipment: ['A001', 'A002']
        },
        {
          itemId: 3,
          clauseNo: '5.7.2',
          itemName: '器身密合性',
          requirement: '按附录 B 中 B.3 试验，注射器的器身密合性应符合表 2 的规定。',
          methodType: 2,
          methodRef: '附录B',
          methodContent: 'B.3 器身密合性试验：将注射器固定在试验装置上，施加规定的压力，观察活塞滑动情况。',
          equipment: ['A003']
        }
      ],
      appendixes: [
        {
          appendixId: 1,
          appendixCode: 'A',
          appendixTitle: '材料要求',
          content: '附录A的详细内容...'
        },
        {
          appendixId: 2,
          appendixCode: 'B',
          appendixTitle: '试验方法',
          content: 'B.1 总则\nB.2 残留容量试验\nB.3 器身密合性试验\n...'
        }
      ]
    },
    {
      id: 2,
      standardNo: 'YY 0469-2023',
      standardName: '医用外科口罩',
      standardNameEN: 'Surgical mask',
      publishDate: '2023-03-17',
      effectiveDate: '2024-04-01',
      scope: '本标准规定了医用外科口罩的要求、试验方法、标识、使用说明书、包装、运输和贮存。',
      itemCount: 18,
      items: [
        {
          itemId: 4,
          clauseNo: '5.1',
          itemName: '外观',
          requirement: '口罩外观应整洁、形状完好，表面不得有破洞、污渍。',
          methodType: 1,
          methodRef: '',
          methodContent: '',
          equipment: []
        },
        {
          itemId: 5,
          clauseNo: '5.2',
          itemName: '细菌过滤效率',
          requirement: '口罩的细菌过滤效率(BFE)应不小于95%。',
          methodType: 3,
          methodRef: '见附录A',
          methodContent: '将口罩样品固定在测试装置上，通入含菌气溶胶，采集过滤前后的空气样本，培养计数后计算过滤效率。测试流量为(28±2)L/min。',
          equipment: ['B001', 'B002']
        }
      ],
      appendixes: [],
      itemCount: 18
    }
  ])
  
  // 设备数据
  const equipments = ref([
    { equipmentCode: 'A001', equipmentName: '电子天平', model: 'XS205-赛多利斯', description: '用于测量物体质量，精度0.01mg', status: 1 },
    { equipmentCode: 'A002', equipmentName: '移液器', model: '100-1000μL', description: '用于精确移取液体', status: 1 },
    { equipmentCode: 'A003', equipmentName: '注射器器身密合性测试仪', model: 'ZY-100', description: '用于测试注射器密合性', status: 1 },
    { equipmentCode: 'B001', equipmentName: '细菌过滤效率测试仪', model: 'BFE-2000', description: '用于测试口罩细菌过滤效率', status: 1 },
    { equipmentCode: 'B002', equipmentName: '微生物培养箱', model: 'MJ-250', description: '用于培养微生物', status: 1 }
  ])
  
  // 获取标准列表
  const getStandards = () => {
    return standards.value
  }
  
  // 根据ID获取标准
  const getStandardById = (id) => {
    return standards.value.find(s => s.id === parseInt(id))
  }
  
  // 搜索标准
  const searchStandards = (keyword) => {
    if (!keyword) return standards.value
    return standards.value.filter(s => 
      s.standardNo.includes(keyword) || 
      s.standardName.includes(keyword)
    )
  }
  
  // 获取设备列表
  const getEquipments = () => {
    return equipments.value
  }
  
  // 根据设备编号获取设备名称
  const getEquipmentName = (code) => {
    const equipment = equipments.value.find(e => e.equipmentCode === code)
    return equipment ? equipment.equipmentName : code
  }
  
  return {
    standards,
    equipments,
    getStandards,
    getStandardById,
    searchStandards,
    getEquipments,
    getEquipmentName
  }
})

