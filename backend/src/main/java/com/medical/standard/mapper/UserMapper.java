package com.medical.standard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.medical.standard.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}

