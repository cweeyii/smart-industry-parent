package com.cweeyii.sql.mointor;

import com.alibaba.druid.filter.stat.StatFilterContextListenerAdapter;
import com.cweeyii.sql.mointor.vo.SQLStatVo;
import com.cweeyii.thread.local.ThreadCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class SQLContextListener<T> extends StatFilterContextListenerAdapter {
    private static final Logger LOGGER= LoggerFactory.getLogger(SQLContextListener.class);
    private Map<T, Map<String, SQLStatVo>> statSqlMap = new HashMap<>();

    private SQLStatVo preInstanceSqlStatVo(T key, String sql) {
        Map<String, SQLStatVo> sqlMap = statSqlMap.get(key);
        if (sqlMap == null) {
            sqlMap = new HashMap<>();
            statSqlMap.put(key, sqlMap);
        }
        SQLStatVo sqlStatVo = sqlMap.get(sql);
        if (sqlStatVo == null) {
            sqlStatVo = new SQLStatVo(sql);
            sqlMap.put(sql, sqlStatVo);
        }
        return sqlStatVo;
    }

    protected abstract String getCurrentSignature();

    @Override
    public void executeBefore(String sql, boolean inTransaction) {
        T key = (T) ThreadCache.getObject(getCurrentSignature());
        if (key != null) {
            SQLStatVo sqlStatVo = preInstanceSqlStatVo(key, sql);
            sqlStatVo.incrementExecuteCount();
        }
    }

    @Override
    public void executeAfter(String sql, long nanos, Throwable error) {
        T key = (T) ThreadCache.getObject(getCurrentSignature());
        if (key != null) {
            if (error != null) {
                SQLStatVo sqlStatVo = preInstanceSqlStatVo(key, sql);
                sqlStatVo.incrementErrorCount();
            }
        }
    }

    public Map<T, Map<String, SQLStatVo>> getStatSqlMap() {
        return statSqlMap;
    }
}