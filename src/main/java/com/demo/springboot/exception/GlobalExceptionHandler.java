package com.demo.springboot.exception;

import com.demo.springboot.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author LISHANSHAN
 * @ClassName ExceptionHandler
 * @Description TODO
 * @date 2022/06/2022/6/9 15:36
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Desc: 如果抛出的是ServiceException，则调用该方法
     * @param se
     * @return {@link Result}
     * @author LISHANSHAN
     * @date 2022/6/9 15:43
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handle(ServiceException se) {
        return Result.error(se.getCode(), se.getMessage());
    }
}
