package com.demo.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author LISHANSHAN
 * @ClassName RoleMenu
 * @Description TODO
 * @date 2022/09/2022/9/11 19:29
 */

@TableName("sys_role_menu")
@Data
public class RoleMenu {

    @ApiModelProperty("角色id")
    private Integer roleId;

    @ApiModelProperty("菜单id")
    private Integer menuId;

}
