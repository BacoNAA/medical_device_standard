package com.medical.standard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical.standard.entity.Clause;
import org.apache.ibatis.annotations.Mapper;

/**
 * 条款信息表Mapper接口
 */
@Mapper
public interface ClauseMapper extends BaseMapper<Clause> {
}

