package com.demo.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demo.springboot.entity.Role;
import com.demo.springboot.entity.RoleMenu;
import com.demo.springboot.mapper.RoleMapper;
import com.demo.springboot.mapper.RoleMenuMapper;
import com.demo.springboot.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private RoleMenuMapper roleMenuMapper;

    /**
     * Desc: 先删后增，即先删除绑定关系，再重新设定；添加事务注解，实现出错回滚
     * @param roleId 角色id
     * @param menuIds 菜单id数组
     * @return
     * @author LISHANSHAN
     * @date 2022/9/11 20:04
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {

        /* LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<RoleMenu>()
                .eq(RoleMenu::getRoleId, roleId);
        roleMenuMapper.delete(queryWrapper); */

        // 删除当前roleId所有的绑定菜单
        roleMenuMapper.deleteByRoleId(roleId);

        //再将前端传的菜单绑定给当前的roleId
        for (Integer menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    @Override
    public List<Integer> getRoleMenu(Integer roleId) {
        return roleMenuMapper.selectByRoleId(roleId);
    }
}
