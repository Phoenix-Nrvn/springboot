package com.demo.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.demo.springboot.common.Constants;
import com.demo.springboot.common.Result;
import com.demo.springboot.controller.dto.UserDto;
import com.demo.springboot.entity.User;
import com.demo.springboot.exception.ServiceException;
import com.demo.springboot.mapper.UserMapper;
import com.demo.springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.springboot.utils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.Configuration;


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
}
