package com.demo.springboot.service;

import com.demo.springboot.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LISHANSHAN
 * @since 2022-08-19
 */
public interface IMenuService extends IService<Menu> {

    /**
     * Desc: 获取所有的菜单项
     * @param name 根据菜单名获取菜单及其子菜单
     * @return {@link List< Menu>}
     * @author LISHANSHAN
     * @date 2022/9/12 14:05
     */
    List<Menu> findMenus(String name);
}
