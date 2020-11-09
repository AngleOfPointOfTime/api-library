package com.anez.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author cxw
 * @description 禁止重复提交
 * @date 2020/11/9 9:20
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotRepeatSubmit {
    /**
     * 过期时间，单位毫秒
     **/
    long value() default 5000;
}
