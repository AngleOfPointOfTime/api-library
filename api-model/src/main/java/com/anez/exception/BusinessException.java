package com.anez.exception;

/**
 * @author cxw
 * @description 实际业务类中所产生的异常信息
 * @date 2020/11/9 10:34
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
