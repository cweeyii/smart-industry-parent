package com.cweeyii.event;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by wenyi on 17/1/8.
 * Email:caowenyi@meituan.com
 */
@Component
public class BrandChangePublisher implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    public void publishBrandInsert(){
        BrandEvent brandEvent=new BrandEvent(applicationContext,"insert");
        applicationContext.publishEvent(brandEvent);
    }

    public void publishBrandUpdate(){
        BrandEvent brandEvent=new BrandEvent(applicationContext,"update");
        applicationContext.publishEvent(brandEvent);
    }
}
