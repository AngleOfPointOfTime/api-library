package com.anez.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author cxw
 * @description AccessToken
 * @date 2020/11/9 9:38
 */
@Data
@AllArgsConstructor
public class AccessToken {
    /**
     * token
     */
    private String token;

    /**
     * 失效时间
     */
    private Date expireTime;
}