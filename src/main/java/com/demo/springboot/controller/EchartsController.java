package com.demo.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.demo.springboot.common.Result;
import com.demo.springboot.entity.User;
import com.demo.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author LISHANSHAN
 * @ClassName EchartsController
 * @Description TODO
 * @date 2022/08/2022/8/5 16:04
 */
@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private IUserService userService;

    @GetMapping("/example")
    public Result get() {
        // 由于存在横轴和纵轴两列数据，这里使用Map来存储
        Map<String, Object> map = new HashMap<>(16);
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y", CollUtil.newArrayList(150, 230, 224, 218, 135, 147, 260));
        return Result.success(map);
    }

    @GetMapping("/members")
    public Result members() {
        List<User> list = userService.list();
        // 第一季度，第二季度，第三季度，第四季度
        int q1 = 0, q2 = 0, q3 = 0, q4 = 0;
        for (User user : list) {
            Date createTime = user.getCreateTime();
            Quarter quarter = DateUtil.quarterEnum(createTime);
            switch(quarter) {
                case Q1: q1 += 1;break;
                case Q2: q2 += 1; break;
                case Q3: q3 += 1; break;
                case Q4: q4 += 1; break;
                default: break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));
    }
}
