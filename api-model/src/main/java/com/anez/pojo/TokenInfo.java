package com.anez.pojo;

import lombok.Data;

/**
 * @author cxw
 * @description TokenInfo
 * @date 2020/11/9 9:39
 */
@Data
public class TokenInfo {
    /**
     * token类型: api:0 、user:1
     */
    private Integer tokenType;

    /**
     * App 信息
     */
    private AppInfo appInfo;

    /**
     * 用户其他数据
     */
    private UserInfo userInfo;
}
