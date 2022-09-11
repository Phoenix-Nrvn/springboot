package com.demo.springboot.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.demo.springboot.common.Constants;
import com.demo.springboot.common.Result;
import com.demo.springboot.entity.Dict;
import com.demo.springboot.entity.Role;
import com.demo.springboot.mapper.DictMapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

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

    @Resource
    private DictMapper dictMapper;

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
    public Result findAll(@RequestParam(defaultValue = "") String name){
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.like("name", name);
        }
        // 查询所有数据
        List<Menu> list = menuService.list(queryWrapper);
        // 找出pid为null的一级菜单
        List<Menu> parentNodes = list.stream().filter(menu -> menu.getPid() == null).collect(Collectors.toList());
        System.out.println(parentNodes);
        // 找出一级菜单的子菜单
        for (Menu menu : parentNodes) {
            // 从第一步获取的list中，筛选出所有pid为一级菜单id的Menu
            // 由于list中包含pid为null的一级菜单，所以不能m.getPid().equals(menu.getId())，会抛出空指针异常
            List<Menu> cMenu = list.stream().filter(m -> menu.getId().equals(m.getPid())).collect(Collectors.toList());
            System.out.println(cMenu);
            menu.setChildren(cMenu);
        }
        return Result.success(parentNodes);
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

    @GetMapping("/icons")
    public Result getIcons() {
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dict::getType, Constants.DICT_TYPE_ICON);
        return Result.success(dictMapper.selectList(queryWrapper));
    }

}
