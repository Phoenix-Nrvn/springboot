package com.demo.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author LISHANSHAN
 * @ClassName Dict
 * @Description TODO
 * @date 2022/09/2022/9/11 18:26
 */
@TableName("sys_dict")
@Data
public class Dict {

    @ApiModelProperty("图标名称")
    private String name;

    @ApiModelProperty("图标内容")
    private String value;

    @ApiModelProperty("图标类型")
    private String type;
}
