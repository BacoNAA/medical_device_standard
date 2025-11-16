package com.medical.standard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical.standard.entity.Method;
import org.apache.ibatis.annotations.Mapper;

/**
 * 检验方法表Mapper接口
 */
@Mapper
public interface MethodMapper extends BaseMapper<Method> {
}

