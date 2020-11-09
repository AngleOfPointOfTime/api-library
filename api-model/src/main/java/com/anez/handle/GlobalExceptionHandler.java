package com.anez.handle;

import cn.hutool.core.util.StrUtil;
import com.anez.enumerate.ApiCodeEnum;
import com.anez.exception.BusinessException;
import com.anez.pojo.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author cxw
 * @description 捕获全局的异常信息
 * @date 2020/11/9 10:01
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(NullPointerException.class)
    public ApiResponse<?> globalException(HttpServletResponse response, NullPointerException ex) {
        log.info("错误代码：{}", response.getStatus());
        log.info("错误信息：{}", response.getStatus());
        return ApiResponse.error(response.getStatus() + "", ex.getMessage());
    }

    /**
     * 处理所有不可知的异常
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    ApiResponse<?> handleException(Exception e){
        String message = e.getMessage();
        String code = ApiCodeEnum.getCode(message);
        if (StrUtil.isBlank(code)) {
            return ApiResponse.error(ApiCodeEnum.UNKNOWN_ERROR);
        }
        return ApiResponse.error(code, e.getMessage());
    }

    /**
     * 处理所有业务异常
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    ApiResponse<?> handleBusinessException(BusinessException e){
        String message = e.getMessage();
        String code = ApiCodeEnum.getCode(message);
        if (StrUtil.isBlank(code)) {
            return ApiResponse.error(ApiCodeEnum.UNKNOWN_ERROR);
        }
        return ApiResponse.error(code, e.getMessage());
    }
}