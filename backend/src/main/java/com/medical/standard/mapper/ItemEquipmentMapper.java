package com.medical.standard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical.standard.entity.ItemEquipment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目设备关联表Mapper接口
 */
@Mapper
public interface ItemEquipmentMapper extends BaseMapper<ItemEquipment> {
}

