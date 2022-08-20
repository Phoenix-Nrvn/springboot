package com.demo.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author LISHANSHAN
 * @ClassName File
 * @Description TODO
 * @date 2022/07/2022/7/6 17:26
 */
@Data
@TableName("sys_file")
public class Files {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String type;

    private Long size;

    private String url;

    private String md5;

    private Boolean isDeleted;

    private Boolean enable;

}
