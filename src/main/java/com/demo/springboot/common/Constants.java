package com.demo.springboot.common;

/**
 * @author LISHANSHAN
 * @ClassName Constants
 * @Description TODO
 * @date 2022/06/2022/6/9 15:09
 */

public interface Constants {

    /** 前后端统一返回码所代表的含义
     * 成功
     * 参数错误
     * 权限不足
     * 未找到
     * 系统错误
     * 其他业务异常
     */

    String CODE_200 = "200";

    String CODE_400 = "400";

    String CODE_401 = "401";

    String CODE_404 = "404";

    String CODE_500 = "500";

    String CODE_600 = "600";

    /** 类型为icon的数据 */
    String DICT_TYPE_ICON = "icon";
}
