package com.medical.standard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical.standard.entity.Equipment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备信息表Mapper接口
 */
@Mapper
public interface EquipmentMapper extends BaseMapper<Equipment> {
}

