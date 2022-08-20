package com.demo.springboot.mapper;

import com.demo.springboot.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LISHANSHAN
 * @since 2022-06-02
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
