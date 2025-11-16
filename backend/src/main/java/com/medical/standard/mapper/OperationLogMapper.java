package com.medical.standard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical.standard.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志表Mapper接口
 */
@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}

