package com.cweeyii.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;

/**
 * Created by wenyi on 16/2/27.
 * Email:caowenyi@meituan.com
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static <T> T getBean(Class<T> clazz) {
        ApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
        if (context == null) {
            context = applicationContext;
        }
        return (T) context.getBean(clazz);
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringBeanUtil.applicationContext = applicationContext;
    }

}
