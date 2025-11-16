package com.medical.standard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical.standard.entity.ExportTemplate;
import org.apache.ibatis.annotations.Mapper;

/**
 * 自定义输出模板表Mapper接口
 */
@Mapper
public interface ExportTemplateMapper extends BaseMapper<ExportTemplate> {
}

