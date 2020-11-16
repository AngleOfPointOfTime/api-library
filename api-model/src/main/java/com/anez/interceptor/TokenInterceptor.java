package com.anez.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.anez.annotation.NotRepeatSubmit;
import com.anez.enumerate.ApiCodeEnum;
import com.anez.filter.ContentCachingRequestWrapper;
import com.anez.pojo.TokenInfo;
import com.anez.utils.ApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author cxw
 * @description TokenInterceptor
 * @date 2020/11/9 9:17
 */
@Slf4j
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * @param request
     * @param response
     * @param handler  访问的目标方法
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取到复制出来的body，可以根据此body进行一些操作
        ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
        String body = IOUtils.toString(requestWrapper.getBody(), request.getCharacterEncoding());

        String token = request.getHeader("token");
        String timestamp = request.getHeader("timestamp");
        // 随机字符串
        String nonce = request.getHeader("nonce");
        String sign = request.getHeader("sign");
        Assert.isTrue(StrUtil.isNotBlank(timestamp) && StrUtil.isNotBlank(sign), ApiCodeEnum.PARAMETER_ERROR.getMsg());

        // 获取超时时间
        NotRepeatSubmit notRepeatSubmit = ApiUtil.getNotRepeatSubmit(handler);
        long expireTime = notRepeatSubmit == null ? 5 * 60 * 1000 : notRepeatSubmit.value();

        // 2. 请求时间间隔
        // long requestInterval = System.currentTimeMillis() - Long.parseLong(timestamp);
        // Assert.isTrue(requestInterval < expireTime, ApiCodeEnum.REQUEST_TIMEOUT.getMsg());

        // 3. 校验Token是否存在
        ValueOperations<String, TokenInfo> tokenRedis = redisTemplate.opsForValue();
        TokenInfo tokenInfo = tokenRedis.get(token);
        Assert.notNull(tokenInfo, ApiCodeEnum.SIGN_ERROR.getMsg());

        // 4. 校验签名(将所有的参数加进来，防止别人篡改参数) 所有参数看参数名升续排序拼接成url
        // 请求参数 + token + timestamp + nonce
        String signString = ApiUtil.concatSignString(request) + tokenInfo.getAppInfo().getKey() + token + timestamp + nonce;
        String signature = SecureUtil.md5(signString);
        log.info("local sign is：{}", signature);
        boolean signFlag = signature.equals(sign);
        Assert.isTrue(signFlag, ApiCodeEnum.SIGN_ERROR.getMsg());

        // 5. 拒绝重复调用(第一次访问时存储，)过期时间和请求超时时间保持一致, 只有标注不允许重复提交注解的才会校验
        if (notRepeatSubmit != null) {
            ValueOperations<String, Integer> signRedis = redisTemplate.opsForValue();
            boolean exists = redisTemplate.hasKey(sign);
            Assert.isTrue(!exists, ApiCodeEnum.REPEAT_SUBMIT.getMsg());
            signRedis.set(sign, 0, expireTime, TimeUnit.MILLISECONDS);
        }
        return super.preHandle(request, response, handler);
    }
}