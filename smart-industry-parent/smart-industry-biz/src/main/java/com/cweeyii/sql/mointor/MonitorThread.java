package com.cweeyii.sql.mointor;

import com.cweeyii.sql.mointor.vo.SQLStatVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by cweeyii on 2/7/16 ${EMAIL}.
 */
public class MonitorThread<T> implements Runnable {
    private static Logger LOGGER = LoggerFactory.getLogger(MonitorThread.class);
    private SQLContextListener<T> contextListener;
    private Long sleepTime;

    public MonitorThread(SQLContextListener<T> contextListener, Long sleepTime) {
        this.contextListener = contextListener;
        this.sleepTime = sleepTime;
    }

    public MonitorThread(SQLContextListener<T> contextListener) {
        this(contextListener, 5000L);
    }

    @Override
    public void run() {
        while (true) {
            Map<T, Map<String, SQLStatVo>> statSqlMap = contextListener.getStatSqlMap();
            if (statSqlMap.size() > 0) {
                for (Map.Entry<T, Map<String, SQLStatVo>> entry : statSqlMap.entrySet()) {
                    T method = entry.getKey();
                    Map<String, SQLStatVo> sqlMap = entry.getValue();
                    if (sqlMap != null && !sqlMap.isEmpty()) {
                        for (Map.Entry<String, SQLStatVo> sqlEntry : sqlMap.entrySet()) {
                            String sql = sqlEntry.getKey();
                            SQLStatVo sqlStatVo = sqlEntry.getValue();
                            LOGGER.info("Druid SQL调用统计: 调用方法=" + method.toString() + " sql=" + sql
                                    + " 执行次数=" + sqlStatVo.getExecuteCount() + " 异常次数=" + sqlStatVo.getErrorCount());
                        }
                    }
                }
            }
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}
