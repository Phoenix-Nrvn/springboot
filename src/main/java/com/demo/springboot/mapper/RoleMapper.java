package com.demo.springboot.mapper;

import com.demo.springboot.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LISHANSHAN
 * @since 2022-08-19
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * Desc: 根据角色的flag获取角色在表中的id
     * @param flag 角色的唯一标识
     * @return {@link Integer}
     * @author LISHANSHAN
     * @date 2022/9/12 13:58
     */
    @Select("select id from sys_role where flag = #{flag}")
    Integer selectByFlag(@Param("flag") String flag);
}
