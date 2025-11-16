package com.medical.standard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical.standard.entity.Standard;
import org.apache.ibatis.annotations.Mapper;

/**
 * 标准信息表Mapper接口
 */
@Mapper
public interface StandardMapper extends BaseMapper<Standard> {
}

