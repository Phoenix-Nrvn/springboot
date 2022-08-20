package com.demo.springboot.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.demo.springboot.common.Constants;
import com.demo.springboot.common.Result;
import com.demo.springboot.controller.dto.UserDto;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import com.demo.springboot.service.IUserService;
import com.demo.springboot.entity.User;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LISHANSHAN
 * @since 2022-06-02
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody User user) {
        boolean result = userService.saveOrUpdate(user);
        return Result.success(result);
    }

    @DeleteMapping("/delete/{userId}")
    public Result delete(@PathVariable("userId") Integer id) {
        boolean result = userService.removeById(id);
        return Result.success(result);
    }

    @PostMapping("/batchDelete")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        boolean result = userService.removeByIds(ids);
        return Result.success(result);
    }

    @GetMapping
    public Result findAll(){
        return Result.success(userService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id){
        return Result.success(userService.getById(id));
    }

    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        return Result.success(user);
    }

    @GetMapping("/pageSelect")
    public Result pageSelect(@RequestParam("pageNo") Integer pageNo,
                                  @RequestParam("pageSize") Integer pageSize,
                                  @RequestParam String username,
                                  @RequestParam String email,
                                  @RequestParam String address) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(username)) {
            queryWrapper.like("username", username);
        }
        if (!StringUtils.isEmpty(email)) {
            queryWrapper.like("email", email);
        }
        if (!StringUtils.isEmpty(address)) {
            queryWrapper.like("address", address);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(userService.page(new Page<>(pageNo, pageSize), queryWrapper));
    }

    /**
     * Desc: 导出到excel中
     * @param response
     * @return
     * @author LISHANSHAN
     * @date 2022/6/7 18:28
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        // 从数据库中查询出所有的数据
        List<User> list = userService.list();
        // 在内存操作，写出到浏览器
        // 新建一个writer对象，向其中写数据
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 自定义标题别名
        writer.addHeaderAlias("username", "用户名");
        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("nickname", "昵称");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("address","地址");
        writer.addHeaderAlias("createTime", "创建时间");
        writer.addHeaderAlias("avatarUrl", "头像");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式，必须要设置，而且基本上是固定的
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset-utf-8");
        String fileName = URLEncoder.encode("用户信息", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        // 通过response获取到输出流，将writer中的内容刷出到输出流中，通过输出流返回到浏览器
        ServletOutputStream outputStream = response.getOutputStream();
        writer.flush(outputStream, true);
        outputStream.close();
        writer.close();
    }

    /**
     * Desc: excel 导入
     * @param file
     * @return
     * @author LISHANSHAN
     * @date 2022/6/7 18:27
     */
    @PostMapping("/import")
    public void imp(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 这里指定了一个泛型User.class，确定获取的类型
        // 如果不指定的话，就不能使用List<User>，因为不知道获取的是什么类型，会返回一个Object类型
        List<User> list = reader.readAll(User.class);
        System.out.println(list);
        userService.saveBatch(list);
    }

    /**
     * Desc: 验证登录信息，将请求体中的内容取出放进一个对象中，那么要传到后台的是一个json参数
     * @param userDto
     * @return {@link Result}
     * @author LISHANSHAN
     * @date 2022/6/8 10:21
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserDto userDto) {
        /*String username = userDto.getUsername();
        String password = userDto.getPassword();*/
        // return userService.login(userDto);
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        UserDto login = userService.login(userDto);
        return Result.success(login);
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        // 将form表单中的数据放入数据库中，并且返回一个实体对象
        User register = userService.register(userDto);
        return Result.success(register);
    }

    // 根据上边的代码不难看出，由于不同的操作，需要返回不同的内容，所以返回类型是多种多样的
    // 这就导致前端需要处理不同的返回值，存在不统一性，显得杂乱，因此，最好将返回结果统一包装
}
