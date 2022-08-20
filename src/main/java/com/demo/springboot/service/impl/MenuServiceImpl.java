package com.demo.springboot.service.impl;

import com.demo.springboot.entity.Menu;
import com.demo.springboot.mapper.MenuMapper;
import com.demo.springboot.service.IMenuService;
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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

}
