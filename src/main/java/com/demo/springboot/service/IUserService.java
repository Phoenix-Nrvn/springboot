package com.demo.springboot.service;

import com.demo.springboot.controller.dto.UserDto;
import com.demo.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LISHANSHAN
 * @since 2022-06-02
 */
public interface IUserService extends IService<User> {

    /**
     * Desc: 验证前端传输的登录信息
     * @param userDto
     * @return {@link boolean}
     * @author LISHANSHAN
     * @date 2022/6/8 10:27
     */
    UserDto login(UserDto userDto);

    /**
     * Desc: 注册
     * @param userDto
     * @return {@link User}
     * @author LISHANSHAN
     * @date 2022/6/16 12:08
     */
    User register(UserDto userDto);

    /**
     * Desc: 通过用户名获得用户
     * @param username
     * @return {@link User}
     * @author LISHANSHAN
     * @date 2022/6/16 12:08
     */
    User getByUsername(String username);
}
