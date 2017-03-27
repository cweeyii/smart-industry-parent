package com.cweeyii.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Created by wenyi on 17/1/8.
 * Email:caowenyi@meituan.com
 */
@Component
public class BrandInsertListener2 implements ApplicationListener<BrandEvent> {
    @Override
    public void onApplicationEvent(BrandEvent event) {
        String operate=(String)event.getContext();
        if(event.getContext() != null && event.getContext().equals("insert"))
            System.out.println("InsertListener2 operate="+operate);
    }
}
