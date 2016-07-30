package com.cweeyii.sql.mointor;

import com.alibaba.druid.filter.stat.StatFilterContext;
import com.cweeyii.thread.local.ThreadCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;

/**
 * Created by cweeyii on 1/7/16 ${EMAIL}.
 */
public class SqlStatInterceptor implements MethodBeforeAdvice, InitializingBean, DisposableBean {
    private static final Logger LOGGER= LoggerFactory.getLogger(SqlStatInterceptor.class);
    private SQLContextListener<Method> contextListener = new SQLMethodContextListener();

    private static final String currentSignature = "currentMethodSignature";

    @PostConstruct
    private void monitorThread() {
        new Thread(new MonitorThread<>(contextListener)).start();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        StatFilterContext.getInstance().addContextListener(contextListener);
    }

    @Override
    public void destroy() throws Exception {
        StatFilterContext.getInstance().removeContextListener(contextListener);
    }

    @Override
    public void before(Method method, Object[] objects, Object o) throws Throwable {
        if (ThreadCache.getObject(currentSignature) == null) {
            ThreadCache.setLocalObject(currentSignature, method);
        }
    }

    class SQLMethodContextListener extends SQLContextListener<Method> {
        @Override
        protected String getCurrentSignature() {
            return currentSignature;
        }
    }
}
