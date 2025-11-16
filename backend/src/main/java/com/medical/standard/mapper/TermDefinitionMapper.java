package com.medical.standard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical.standard.entity.TermDefinition;
import org.apache.ibatis.annotations.Mapper;

/**
 * 术语定义表Mapper接口
 */
@Mapper
public interface TermDefinitionMapper extends BaseMapper<TermDefinition> {
}

