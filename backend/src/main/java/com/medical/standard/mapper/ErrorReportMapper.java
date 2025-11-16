package com.medical.standard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical.standard.entity.ErrorReport;
import org.apache.ibatis.annotations.Mapper;

/**
 * 错误报告表Mapper接口
 */
@Mapper
public interface ErrorReportMapper extends BaseMapper<ErrorReport> {
}

