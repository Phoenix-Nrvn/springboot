package com.demo.springboot.exception;

import lombok.Getter;

/**
 * @author LISHANSHAN
 * @ClassName ServiceException
 * @Description RuntimeException是运行时异常
 * @date 2022/06/2022/6/9 15:38
 */
@Getter
public class ServiceException extends RuntimeException {

    private String code;

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}
