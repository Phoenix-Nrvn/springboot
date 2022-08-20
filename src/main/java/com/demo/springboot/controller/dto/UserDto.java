package com.demo.springboot.controller.dto;

import lombok.Data;

/**
 * @author LISHANSHAN
 * @ClassName UserDto
 * @Description 接收前端登录信息
 * @date 2022/06/2022/6/8 10:24
 */

@Data
public class UserDto {

    private String username;

    private String password;

    private String nickname;

    private String avatarUrl;

    private String token;

}
