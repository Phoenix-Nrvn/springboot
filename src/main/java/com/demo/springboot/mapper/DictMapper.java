package com.demo.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.springboot.entity.Dict;
import org.apache.ibatis.annotations.Mapper;

/**
 * Desc TODO
 *
 * @author LISHANSHAN
 * @InterfaceName DictMapper
 * @date 2022/09/2022/9/11 18:23
 */
@Mapper
public interface DictMapper extends BaseMapper<Dict> {
}
