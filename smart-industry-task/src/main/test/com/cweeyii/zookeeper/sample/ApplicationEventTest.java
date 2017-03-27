package com.cweeyii.zookeeper.sample;

import com.cweeyii.event.BrandChangePublisher;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * Created by wenyi on 17/1/8.
 * Email:caowenyi@meituan.com
 */
public class ApplicationEventTest extends BaseTest {
    @Resource
    private BrandChangePublisher brandChangePublisher;

    @Test
    public void testPublisher(){
        brandChangePublisher.publishBrandInsert();
        brandChangePublisher.publishBrandUpdate();
    }
}
