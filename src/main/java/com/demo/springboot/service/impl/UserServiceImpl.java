package com.demo.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.demo.springboot.common.Constants;
import com.demo.springboot.common.Result;
import com.demo.springboot.controller.dto.UserDto;
import com.demo.springboot.entity.Menu;
import com.demo.springboot.entity.User;
import com.demo.springboot.exception.ServiceException;
import com.demo.springboot.mapper.RoleMapper;
import com.demo.springboot.mapper.RoleMenuMapper;
import com.demo.springboot.mapper.UserMapper;
import com.demo.springboot.service.IMenuService;
import com.demo.springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.springboot.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.security.auth.login.Configuration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LISHANSHAN
 * @since 2022-06-02
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log log = Log.get();

    @Autowired
    UserMapper userMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    /**
     * Desc: 这里要接收一个UserDto对象
     * @param userDto
     * @return {@link boolean}
     * @author LISHANSHAN
     * @date 2022/6/8 10:48
     */
    @Override
    public UserDto login(UserDto userDto) {

        User one = getUserInfo(userDto);
        if (one != null) {
            // 将one中的内容copy到userDto中，忽略大小写
            BeanUtil.copyProperties(one, userDto, true);
            String token = TokenUtils.generateToken(one.getId().toString(), one.getPassword());
            userDto.setToken(token);

            // 根据用户的角色，获取角色id，进而获取角色id对应的菜单数组
            String role = one.getRole();
            userDto.setMenus(getRoleMenus(role));
            return userDto;
        } else {
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
    }

    @Override
    public User register(UserDto userDto) {
        User user = getUserInfo(userDto);
        System.out.println(user);
        if (user == null) {
            user = new User();
            // 将userDto中的内容存入对象user中
            BeanUtil.copyProperties(userDto, user, true);
            save(user);
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return user;
    }

    private User getUserInfo(UserDto userDto) {
        if (userDto != null) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", userDto.getUsername());
            queryWrapper.eq("password", userDto.getPassword());
            User one;
            try {
                one = getOne(queryWrapper);
            } catch (Exception e) {
                log.error(e);
                throw new ServiceException(Constants.CODE_500, "错误");
            }
            return one;
        }
        return null;
    }

    @Override
    public User getByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = getOne(queryWrapper);
        return user;
    }

    /**
     * Desc: 根据角色的唯一标识获取角色id，进而获取当前角色的菜单列表
     * @param roleFlag 角色的唯一标识
     * @return {@link List< Menu>}
     * @author LISHANSHAN
     * @date 2022/9/12 16:07
     */
    private List<Menu> getRoleMenus(String roleFlag) {

        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // 获取当前角色id对应的菜单id数组
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        // 查出系统中所有的菜单
        List<Menu> menus = menuService.findMenus("");
        // 结果放置在该数组中
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选出用户对应的角色所能访问的菜单
        // 这里只获取了一级菜单，但并不是没有一级，其对应的子菜单也没有
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            // 就比如，没有系统管理，但可以有文件管理，就需要再去获得子菜单，判断是否包含
            List<Menu> children = menu.getChildren();
            // 这里是移除子菜单中不包含在menuIds中的
            // 因为如果子菜单没有全部选中的话，其对应的父菜单的id是不存在的，但是之后要显示父菜单，所以额外加上
            boolean b = children.removeIf(child -> !menuIds.contains(child.getId()));
            if ((children.size() != 0) && b ) {
                roleMenus.add(menu);
            }
            // roleMenus.addAll(children);
        }
        return roleMenus;
    }
}
