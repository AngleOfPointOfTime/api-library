package com.anez.aspect;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cxw
 * @description LogAspect
 * @date 2020/11/3 17:31
 */
@Component
@Aspect
@Slf4j
public class LogAspect {
    //跳过导入导出的controller&& !execution(* com.wl.partner.controller.ExcelAllController.*(..))
    // @Pointcut("execution(public * com.anez.controller..*.*(..)) && !execution(* com.anez.controller..*(..))")
    @Pointcut("execution(public * com.anez.controller..*.*(..))")
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        if (joinPoint.getArgs().length > 0) {
            if (joinPoint.getArgs()[0] instanceof HttpServletResponse) {
                if (joinPoint.getArgs().length > 1) {
                    log.info("----收到请求----> " + request.getRequestURL().toString() + ":" + JSONUtil.toJsonStr(joinPoint.getArgs()[1]));
                } else {
                    log.info("----收到请求----> " + request.getRequestURL().toString());
                }
            } else {
                log.info("----收到请求----> " + request.getRequestURL().toString() + ":" + JSONUtil.toJsonStr(joinPoint.getArgs()[0]));
            }
        }
    }

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) { 
        // 处理完请求，返回内容
        log.info("----返回参数----> : " + JSONUtil.toJsonStr(ret));
    }
}
