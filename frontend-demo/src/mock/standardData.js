// 模拟完整的标准数据结构
export const mockStandardData = {
  // 标准基本信息
  standardInfo: {
    standardNo: 'GB 15810-2019',
    standardName: '一次性使用无菌注射器',
    standardNameEN: 'Sterile hypodermic syringes for single use',
    isoReference: 'ISO 7886-1:1993，MOD',
    publishDate: '2019-06-04',
    effectiveDate: '2020-05-01',
    scope: '本标准规定了一次性使用无菌注射器的要求、试验方法、标志和包装。\n本标准适用于以无菌状态提供的，由聚丙烯或其他适宜材料制成的一次性使用注射器。',
    references: 'GB/T 191 包装储运图示标志\nGB/T 1962.1 注射器、注射针及其他医疗器械 6%(鲁尔)圆锥接头 第1部分:通用要求\nGB/T 14233.1 医用输液、输血、注射器具检验方法 第1部分:化学分析方法\nYY 0581.1 医用输液、输血器具用聚丙烯专用料 第1部分:注射器用聚丙烯专用料',
    foreword: '本标准按照GB/T 1.1-2009给出的规则起草。\n本标准代替GB 15810-2001《一次性使用无菌注射器》。与GB 15810-2001相比，除编辑性修改外主要技术变化如下：\n——增加了6(鲁尔)锥头注射器的要求...',
    introduction: '本标准参考采用ISO 7886-1:1993《一次性使用无菌注射器》。'
  },

  // 术语定义
  terms: [
    {
      termCode: '3.1',
      termName: '公称容量',
      termNameEN: 'nominal capacity',
      definition: '标注在注射器上的容量值。',
      example: ''
    },
    {
      termCode: '3.2',
      termName: '残留容量',
      termNameEN: 'dead space',
      definition: '注射器在使用后，活塞被完全推入后，与相配注射针或配件内所滞留液体的容量。',
      example: ''
    },
    {
      termCode: '3.3',
      termName: '滑动性能',
      termNameEN: 'glide force',
      definition: '使活塞在外套筒内移动所需的力。',
      example: ''
    }
  ],

  // 检验项目列表
  requirementItems: [
    {
      itemId: 1,
      clauseNo: '5.1',
      itemName: '外观',
      requirement: '注射器应清洁，不应有可见的异物、污染物、毛刺、粗糙、气泡等缺陷。',
      methodType: 1, // 无独立方法
      methodRef: '',
      methodContent: '',
      notes: '目视检查',
      equipments: [], // 无需设备
      status: 'success' // success, warning, error
    },
    {
      itemId: 2,
      clauseNo: '5.7.1',
      itemName: '残留容量',
      requirement: '按附录 B 中 B.2 试验，在活塞被完全推入以及与所配注射针连接后，注射器的残留容量应不大于表2规定的值。',
      methodType: 2, // 引用附录
      methodRef: '附录B.2',
      methodContent: '详细方法见附录B.2章节',
      notes: '试验条件：室温20±5℃，相对湿度不大于80%',
      equipments: [
        { code: 'A001', name: '电子天平', specification: '分度值0.1mg' }
      ],
      status: 'success'
    },
    {
      itemId: 3,
      clauseNo: '5.7.2',
      itemName: '器身密合性',
      requirement: '按附录 B 中 B.3 试验，注射器在承受100kPa压力时不应出现泄漏。',
      methodType: 2,
      methodRef: '附录B.3',
      methodContent: '详细方法见附录B.3章节',
      notes: '试验用液体为20±5℃的蒸馏水或去离子水',
      equipments: [
        { code: 'B001', name: '注射器器身密合性测试仪', specification: '压力范围0-150kPa' }
      ],
      status: 'success'
    },
    {
      itemId: 4,
      clauseNo: '5.8',
      itemName: '活塞推动力',
      requirement: '按附录 B 中 B.5 试验，活塞推动力应符合表3的规定。',
      methodType: 2,
      methodRef: '附录B.5',
      methodContent: '',
      notes: '',
      equipments: [
        { code: 'C001', name: '活塞推动力测试仪', specification: '量程0-20N，精度±0.1N' }
      ],
      status: 'warning' // 新设备需确认
    },
    {
      itemId: 5,
      clauseNo: '5.9',
      itemName: '化学性能',
      requirement: '按GB/T 14233.1的规定试验，注射器应符合下列要求：\na) 酸碱度：pH值应在4.0~7.5范围内；\nb) 易氧化物：消耗高锰酸钾溶液体积应不大于2.0mL；\nc) 重金属：应不大于百万分之一（1ppm）。',
      methodType: 3, // 完整方法内容
      methodRef: 'GB/T 14233.1',
      methodContent: '按GB/T 14233.1-2008《医用输液、输血、注射器具检验方法 第1部分：化学分析方法》规定的方法进行试验。',
      notes: '试验用提取液为水',
      equipments: [
        { code: 'D001', name: 'pH计', specification: '精度±0.01pH' },
        { code: 'E001', name: '滴定管', specification: '50mL，A级' }
      ],
      status: 'error' // 缺少设备信息，需人工补充
    }
  ],

  // 附录内容
  appendices: [
    {
      appendixCode: 'A',
      appendixTitle: '容量与分度值的关系',
      appendixType: '规范性',
      sections: [
        {
          sectionNo: 'A.1',
          sectionTitle: '总则',
          content: '本附录规定了注射器公称容量与分度值的对应关系。'
        },
        {
          sectionNo: 'A.2',
          sectionTitle: '要求',
          content: '注射器的分度值应符合表A.1的规定。\n\n表A.1 容量与分度值\n公称容量/mL | 分度值/mL\n1 | 0.01\n2, 3 | 0.1\n5, 10 | 0.2\n20, 30 | 0.5 或 1\n50, 60 | 1 或 2'
        }
      ]
    },
    {
      appendixCode: 'B',
      appendixTitle: '试验方法',
      appendixType: '规范性',
      sections: [
        {
          sectionNo: 'B.1',
          sectionTitle: '试验条件',
          content: '除非另有规定，所有试验应在温度为20±5℃、相对湿度不大于80%的环境中进行。试验前，样品应在试验环境中放置至少2小时。'
        },
        {
          sectionNo: 'B.2',
          sectionTitle: '残留容量测定',
          content: '1) 将注射器与适配的注射针连接；\n2) 抽取适量蒸馏水；\n3) 排除气泡后，将活塞完全推入；\n4) 卸下注射针，用电子天平称量注射器及残留液体的质量；\n5) 拆开注射器，排空残留液体后，擦干并称量空注射器质量；\n6) 两次质量之差即为残留液体质量，按水的密度计算残留容量。\n\n注：称量精度应达到0.1mg。'
        },
        {
          sectionNo: 'B.3',
          sectionTitle: '器身密合性试验',
          content: '1) 将注射器抽满水，排除气泡；\n2) 将注射器安装到密合性测试仪上；\n3) 对注射器施加100kPa的压力，保持30秒；\n4) 观察是否有水滴泄漏或活塞明显移动。\n\n判定：如无泄漏且活塞无明显移动，则判定为合格。'
        },
        {
          sectionNo: 'B.5',
          sectionTitle: '活塞推动力试验',
          content: '1) 将注射器垂直固定在测试仪上；\n2) 以100±20mm/min的速度向下推动活塞；\n3) 记录推动过程中的最大力值；\n4) 与表3规定值比较。\n\n表3 活塞推动力要求\n公称容量/mL | 最大推动力/N\n1 | 9\n2, 3, 5 | 12\n10, 20 | 20\n30, 50, 60 | 30'
        }
      ]
    }
  ]
}

// 导入预览数据（包含状态标注）
export const mockImportPreview = {
  fileName: 'GB 15810-2019 一次性使用无菌注射器.xlsx',
  uploadTime: '2025-10-22 14:30:25',
  standardInfo: mockStandardData.standardInfo,
  
  // 自动处理统计
  statistics: {
    total: 15,
    success: 10,
    warning: 3,
    error: 2
  },
  
  // 预览数据（带状态标注）
  items: mockStandardData.requirementItems,
  
  // 新设备列表（需确认）
  newEquipments: [
    {
      keywordFound: '活塞推动力测试仪',
      suggestedName: '活塞推动力测试仪',
      suggestedCode: 'C001',
      specification: '量程0-20N，精度±0.1N',
      relatedItems: ['5.8 活塞推动力']
    }
  ],
  
  // 需人工补充的项目
  incompleteItems: [
    {
      clauseNo: '5.9',
      itemName: '化学性能',
      missingInfo: '部分设备信息未识别',
      suggestion: '请补充滴定管等化学分析设备'
    }
  ]
}

// 设备库数据
export const mockEquipments = [
  {
    code: 'A001',
    name: '电子天平',
    model: '精密分析天平',
    description: '用于精密称量',
    relatedProjects: 12,
    specifications: ['分度值0.1mg', '分度值1mg', '分度值0.01mg']
  },
  {
    code: 'B001',
    name: '注射器器身密合性测试仪',
    model: '密合性测试设备',
    description: '用于测试注射器密封性能',
    relatedProjects: 8,
    specifications: ['压力范围0-150kPa', '压力范围0-200kPa']
  },
  {
    code: 'C001',
    name: '活塞推动力测试仪',
    model: '推力测试仪',
    description: '用于测试活塞推动力',
    relatedProjects: 5,
    specifications: ['量程0-20N，精度±0.1N', '量程0-50N，精度±0.1N']
  },
  {
    code: 'D001',
    name: 'pH计',
    model: '酸度计',
    description: '用于测定溶液pH值',
    relatedProjects: 15,
    specifications: ['精度±0.01pH', '精度±0.02pH']
  },
  {
    code: 'E001',
    name: '滴定管',
    model: 'A级玻璃滴定管',
    description: '用于容量分析',
    relatedProjects: 10,
    specifications: ['50mL，A级', '25mL，A级', '10mL，A级']
  }
]

// 标准列表数据
export const mockStandardList = [
  {
    id: 1,
    standardNo: 'GB 15810-2019',
    standardName: '一次性使用无菌注射器',
    publishDate: '2019-06-04',
    effectiveDate: '2020-05-01',
    itemCount: 15
  },
  {
    id: 2,
    standardNo: 'YY 0469-2023',
    standardName: '医用外科口罩',
    publishDate: '2023-03-17',
    effectiveDate: '2024-04-01',
    itemCount: 12
  },
  {
    id: 3,
    standardNo: 'YY/T 0969-2023',
    standardName: '一次性使用医用口罩',
    publishDate: '2023-03-17',
    effectiveDate: '2024-04-01',
    itemCount: 10
  }
]

