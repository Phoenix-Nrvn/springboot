package com.demo.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.demo.springboot.entity.Menu;
import com.demo.springboot.mapper.MenuMapper;
import com.demo.springboot.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<Menu> findMenus(String name) {

        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.like("name", name);
        }
        // 查询所有数据
        List<Menu> list = list(queryWrapper);
        // 找出pid为null的一级菜单
        List<Menu> parentNodes = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        // 找出一级菜单的子菜单
        for (Menu menu : parentNodes) {
            // 从第一步获取的list中，筛选出所有pid为一级菜单id的Menu
            // 由于list中包含pid为null的一级菜单，所以不能m.getPid().equals(menu.getId())，会抛出空指针异常
            List<Menu> cMenu = list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList());
            System.out.println(cMenu);
            menu.setChildren(cMenu);
        }
        return parentNodes;
    }
}
