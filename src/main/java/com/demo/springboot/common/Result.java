package com.demo.springboot.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LISHANSHAN
 * @ClassName Result
 * @Description 接口统一返回包装类
 * @date 2022/06/2022/6/9 15:04
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    /** 首先要告诉前端，此次请求结果，成功or失败，即code
     * 如果失败，要返回失败的原因 ，即msg
     * 如果成功，要返回请求的结果
     */
    private String code;

    private String msg;

    private Object data;

    public static Result success() {
        return new Result(Constants.CODE_200, "", null);
    }

    public static Result success(Object data) {
        return new Result(Constants.CODE_200, "", data);
    }

    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }

    public static Result error() {
        return new Result(Constants.CODE_500, "系统错误", null);
    }
}
