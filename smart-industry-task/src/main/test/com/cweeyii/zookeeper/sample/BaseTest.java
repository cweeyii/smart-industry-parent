package com.cweeyii.zookeeper.sample;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:applicationContext-datasource.xml","classpath*:webmvc-basic-webapp.xml",
        "classpath*:applicationContext-mq.xml", "classpath*:applicationContext-redis.xml",
        "classpath*:applicationContext-beans.xml", "classpath*:applicationContext-crane.xml",
        "classpath*:applicationContext-mq-listener.xml","classpath*:applicationContext-mq-sender.xml",
        "classpath*:applicationContext-task.xml","classpath*:webmvc-config.xml"})
public class BaseTest {
    private static String path;
    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        log.info("run setUpBeforeClass");
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        log.info("run tearDownAfterClass");
    }

    @Test
    public void doNothing() {
        log.info("run doNothing");
    }
}