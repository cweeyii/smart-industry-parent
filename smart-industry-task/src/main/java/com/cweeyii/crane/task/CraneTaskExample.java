/*
package com.cweeyii.crane.task;
 
import com.cip.crane.client.common.log.LogConstants;
import com.cip.crane.client.spring.annotation.Crane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
 
*/
/**
 * Author   mingdongli
 * 16/6/30  上午10:03.
 *//*

@Component
public class CraneTaskExample {
    private final static Logger CRANE_LOGGER = LoggerFactory.getLogger(LogConstants.Crane);

    @Crane("wdc-crane-demo")    //crane-demo为在管理平台新建的任务名
    public void testAnnotation(String value){   //value可通过新建任务时的方法参数传递到客户端
        CRANE_LOGGER.info(value);
        //实现自己的任务逻辑
    }

    @Crane("wdc-crane-demo1")
    public void testAnnotation1(String value){   //value可通过新建任务时的方法参数传递到客户端
        CRANE_LOGGER.info(value);
        //实现自己的任务逻辑
    }
    
}*/
