package com.anez.config;

import com.anez.pojo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author cxw
 * @description UserConfig
 * @date 2020/11/10 18:03
 */
@Slf4j
@Configuration
@PropertySource("classpath:anez.properties")
public class UserConfig {
    @Value("${registerSystem}")
    private String registerSystem;
    @Bean("initUser")
    public UserInfo initUser(){
        log.info("---------------------" + registerSystem);
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(registerSystem);
        return userInfo;
    }
}
