package com.anez.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.SecureUtil;
import com.anez.annotation.NotRepeatSubmit;
import com.anez.enumerate.ApiCodeEnum;
import com.anez.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author cxw
 * @description TokenController
 * @date 2020/11/9 9:15
 */
@Slf4j
@RestController
@RequestMapping("/api/token")
public class TokenController {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * API Token
     *
     * @param sign
     * @return
     */
    @PostMapping("/api_token")
    public ApiResponse<AccessToken> apiToken(String appId, @RequestHeader("timestamp") String timestamp, @RequestHeader("sign") String sign) {
        Assert.isTrue(StringUtils.isEmpty(appId) || StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(sign), ApiCodeEnum.PARAMETER_ERROR.getMsg());

        // long requestInterval = System.currentTimeMillis() - Long.parseLong(timestamp);
        // Assert.isTrue(requestInterval < 5 * 60 * 1000, ApiCodeEnum.REQUEST_TIMEOUT.getMsg());

        // 1. 根据appId查询数据库获取appSecret
        AppInfo appInfo = new AppInfo("1", "12345678954556");

        // 2. 校验签名
        String signString = timestamp + appId + appInfo.getKey();
        String signature = SecureUtil.md5(signString);
        log.info(signature);
        // Assert.isTrue(signature.equals(sign), ApiCodeEnum.SIGN_ERROR.getMsg());
        // 3. 如果正确生成一个token保存到redis中，如果错误返回错误信息
        AccessToken accessToken = this.saveToken(0, appInfo, null);

        return ApiResponse.success(accessToken);
    }


    @NotRepeatSubmit
    @PostMapping("user_token")
    public ApiResponse<UserInfo> userToken(String username, String password) {
        // 根据用户名查询密码, 并比较密码(密码可以RSA加密一下)
        UserInfo userInfo = new UserInfo(username, "81255cb0dca1a5f304328a70ac85dcbd", "111111");
        String pwd = password + userInfo.getSalt();
        String passwordMd5 = SecureUtil.md5(pwd);
        Assert.isTrue(passwordMd5.equals(userInfo.getPassword()), "密码错误");

        // 2. 保存Token
        AppInfo appInfo = new AppInfo("1", "12345678954556");
        AccessToken accessToken = this.saveToken(1, appInfo, userInfo);
        userInfo.setAccessToken(accessToken);
        return ApiResponse.success(userInfo);
    }

    private AccessToken saveToken(int tokenType, AppInfo appInfo, UserInfo userInfo) {
        String token = UUID.randomUUID().toString();

        // token有效期为2小时
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, 7200);
        Date expireTime = calendar.getTime();

        // 4. 保存token
        ValueOperations<String, TokenInfo> operations = redisTemplate.opsForValue();
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setTokenType(tokenType);
        tokenInfo.setAppInfo(appInfo);

        if (tokenType == 1) {
            tokenInfo.setUserInfo(userInfo);
        }
        operations.set(token, tokenInfo, 7200, TimeUnit.SECONDS);
        return new AccessToken(token, expireTime);
    }

    public static void main(String[] args) {
        long timestamp = System.currentTimeMillis();
        System.out.println(timestamp);
        String signString = timestamp + "1" + "12345678954556";
        String sign = SecureUtil.md5(signString);
        System.out.println(sign);

        System.out.println("-------------------");
        signString = "password=123456&username=1&12345678954556" + "ff03e64b-427b-45a7-b78b-47d9e8597d3b1529815393153sdfsdfsfs" + timestamp + "A1scr6";
        sign = SecureUtil.md5(signString);
        System.out.println(sign);
        String s = SecureUtil.md5(signString);
        System.out.println(s);
        System.out.println(System.currentTimeMillis());
    }
}