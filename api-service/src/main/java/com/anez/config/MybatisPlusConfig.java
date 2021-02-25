package com.anez.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author cxw
 * @description MybatisPlusConfig
 * @date 2021/2/25 15:24
 */
@Configuration
@EnableTransactionManagement
public class MybatisPlusConfig {

    /**
     *分页查询拦截器
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}