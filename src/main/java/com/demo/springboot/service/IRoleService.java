package com.demo.springboot.service;

import com.demo.springboot.entity.Role;
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
public interface IRoleService extends IService<Role> {

    /**
     * Desc: 根据角色设定菜单
     * @param roleId 角色id
     * @param menuIds 菜单id数组
     * @return
     * @author LISHANSHAN
     * @date 2022/9/11 21:38
     */
    void setRoleMenu(Integer roleId, List<Integer> menuIds);

    /**
     * Desc: 根据角色获取菜单
     * @param roleId 角色id
     * @return {@link List< Integer>}
     * @author LISHANSHAN
     * @date 2022/9/11 21:39
     */
    List<Integer> getRoleMenu(Integer roleId);

}
