package com.demo.springboot.service.impl;

import com.demo.springboot.entity.Role;
import com.demo.springboot.mapper.RoleMapper;
import com.demo.springboot.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LISHANSHAN
 * @since 2022-08-19
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
