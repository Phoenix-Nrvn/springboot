package com.demo.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.demo.springboot.common.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.demo.springboot.service.IRoleService;
import com.demo.springboot.entity.Role;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LISHANSHAN
 * @since 2022-08-19
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    // 因为userService中的user是活的，是可以跟随具体的实体类名称发生改变的，所以不能写死
    // table.entityPath = user
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Role role) {
        boolean result = roleService.saveOrUpdate(role);
        return Result.success(result);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        boolean result = roleService.removeById(id);
        return Result.success(result);
    }

    @PostMapping("/batchDelete")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        boolean result = roleService.removeByIds(ids);
        return Result.success(result);
    }

    @GetMapping
    public Result findAll(){
        return Result.success(roleService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id){
        return Result.success(roleService.getById(id));
    }

    @GetMapping("/pageSelect")
    public Result pageSelect(@RequestParam("pageNo") Integer pageNo,
                             @RequestParam("pageSize") Integer pageSize,
                             @RequestParam("name") String name) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(roleService.page(new Page<>(pageNo, pageSize), queryWrapper));
    }

}
