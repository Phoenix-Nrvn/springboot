package com.demo.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.springboot.entity.RoleMenu;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Desc TODO
 *
 * @author LISHANSHAN
 * @InterfaceName RoleMenuMapper
 * @date 2022/09/2022/9/11 19:31
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * Desc: 删除表中所有和roleId关联的菜单
     * @param roleId 角色id
     * @return {@link int}
     * @author LISHANSHAN
     * @date 2022/9/11 19:59
     */
    @Delete("delete from sys_role_menu where role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Integer roleId);

    /**
     * Desc: 根据roleId查询出对应的菜单信息
     * @param roleId 角色id
     * @return {@link List< Integer>}
     * @author LISHANSHAN
     * @date 2022/9/11 21:37
     */
    @Select("select menu_id from sys_role_menu where role_id = #{roleId}")
    List<Integer> selectByRoleId(@Param("roleId") Integer roleId);
}
