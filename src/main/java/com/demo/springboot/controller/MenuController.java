package com.demo.springboot.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.springboot.common.Result;
import com.demo.springboot.entity.Role;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

import com.demo.springboot.service.IMenuService;
import com.demo.springboot.entity.Menu;

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
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody Menu menu) {
        boolean result = menuService.saveOrUpdate(menu);
        return Result.success(result);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        boolean result = menuService.removeById(id);
        return Result.success(result);
    }

    @PostMapping("/batchDelete")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        boolean result = menuService.removeByIds(ids);
        return Result.success(result);
    }

    @GetMapping
    public Result findAll(){
        return Result.success(menuService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id){
        return Result.success(menuService.getById(id));
    }

    @GetMapping("/pageSelect")
    public Result pageSelect(@RequestParam("pageNo") Integer pageNo,
                             @RequestParam("pageSize") Integer pageSize,
                             @RequestParam("name") String name) {
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(menuService.page(new Page<>(pageNo, pageSize), queryWrapper));
    }

}
