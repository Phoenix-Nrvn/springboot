package com.demo.springboot.mapper;

import com.demo.springboot.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LISHANSHAN
 * @since 2022-08-19
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

}
