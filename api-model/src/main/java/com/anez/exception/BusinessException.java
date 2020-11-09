package com.anez.exception;

/**
 * @author cxw
 * @description BusinessException
 * @date 2020/11/9 10:34
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
