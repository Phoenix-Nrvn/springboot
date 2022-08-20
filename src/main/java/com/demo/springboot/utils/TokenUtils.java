package com.demo.springboot.utils;

import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.demo.springboot.entity.User;
import com.demo.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author LISHANSHAN
 * @ClassName TokenUtils
 * @Description TODO
 * @date 2022/06/2022/6/16 11:42
 */
@Component
public class TokenUtils {

    private static IUserService staticUserService;

    @Resource
    private IUserService userService;

    /**
     * Desc: 将TokenUtils注册为Spring的一个Bean，注册完之后，通过Resource方式将userService的对象引进来
     * 之后通过PostConstruct，在后台项目启动时，将其赋值给staticUserService
     * PostConstruct在构造函数之后，init（）方法之前执行，在静态方法中调用依赖注入的Bean中的方法。
     * @param
     * @return
     * @author LISHANSHAN
     * @date 2022/6/16 16:44
     */
    @PostConstruct
    public void setStaticUserService() {
        staticUserService = userService;
    }

    /**
     * Desc: 生成token，将userId和用户密码传进来作为载荷
     * @param userId
     * @param sign
     * @return {@link String}
     * @author LISHANSHAN
     * @date 2022/6/16 11:47
     */
    public static String generateToken(String userId, String sign) {
        // 将userId和password传进来，作为载荷
        // 设置有效时长为2个小时
        // 设置服务器端token加密算法为HMAC256，token加密密钥为用户密码
        return JWT.create().withAudience(userId, sign)
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2))
                .sign(Algorithm.HMAC256(sign));
    }

    /**
     * Desc: 获取当前登录的用户信息，可以在全局的任何地方使用，很方便
     * @param
     * @return {@link User}
     * @author LISHANSHAN
     * @date 2022/6/16 16:38
     */
    public static User getCurrentUser(){
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StringUtils.isNotBlank(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(userId);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
