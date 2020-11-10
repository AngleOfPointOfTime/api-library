package com.anez.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author cxw
 * @description ChainExecute
 * @date 2020/11/10 18:13
 */
@Slf4j
@Component
public class ChainExecute implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Object initUser = applicationContext.getBean("initUser");
        log.info("init User is {}", initUser);
        // TODO 根据初始化的Bean  进行项目启动时的同步操作
    }
}
