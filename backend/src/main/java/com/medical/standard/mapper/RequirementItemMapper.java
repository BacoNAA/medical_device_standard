package com.medical.standard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical.standard.entity.RequirementItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 检验项目表Mapper接口
 */
@Mapper
public interface RequirementItemMapper extends BaseMapper<RequirementItem> {
}

