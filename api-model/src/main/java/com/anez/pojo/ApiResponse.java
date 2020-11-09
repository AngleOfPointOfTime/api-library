package com.anez.pojo;

import cn.hutool.crypto.SecureUtil;
import com.anez.enumerate.ApiCodeEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import com.anez.utils.ApiUtil;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cxw
 * @description ApiResponse
 * @date 2020/11/9 9:42
 */
@Data
@Slf4j
public class ApiResponse<T> {
    /**
     * 结果
     */
    private ApiResult result;

    /**
     * 数据
     */
    private T data;

    /**
     * 签名
     */
    private String sign;


    public static <T> ApiResponse<T> success(T data) {
        return response(ApiCodeEnum.SUCCESS.getCode(), ApiCodeEnum.SUCCESS.getMsg(), data);
    }

    public static <T> ApiResponse<T> error(ApiCodeEnum codeEnum) {
        return response(codeEnum.getCode(), codeEnum.getMsg(), null);
    }

    public static <T> ApiResponse<T> error(String code, String msg) {
        return response(code, msg, null);
    }

    public static <T> ApiResponse<T> response(String code, String msg, T data) {
        ApiResult result = new ApiResult(code, msg);
        ApiResponse<T> response = new ApiResponse<>();
        response.setResult(result);
        response.setData(data);
        String sign = signData(data);
        response.setSign(sign);
        return response;
    }

    private static <T> String signData(T data) {
        // TODO 查询key
        String key = "12345678954556";
        Map<String, String> responseMap = null;
        try {
            responseMap = getFields(data);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (responseMap == null) {responseMap = new HashMap<>();}
        String urlComponent = ApiUtil.concatSignString(responseMap);
        String signature = urlComponent + "key=" + key;
        return SecureUtil.md5(signature);
    }

    /**
     * @param data 反射的对象,获取对象的字段名和值
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static Map<String, String> getFields(Object data) throws IllegalAccessException {
        if (data == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        Field[] fields = data.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            Object value = field.get(data);
            if (field.get(data) != null) {
                map.put(name, value.toString());
            }
        }

        return map;
    }
}