package com.cweeyii.cache.framework;

import com.cweeyii.cache.framework.redis.SmartRedis;
import com.cweeyii.util.EnterpriseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;


@Service
public class MapCacheDataService<K, V> extends RedisCacheDataService<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapCacheDataService.class);


    @Resource
    private SmartRedis smartRedis;

    @Resource
    private DBDataReader dbDataProvider;

    @SuppressWarnings("unchecked")
    @Override
    protected Map<K, V> getFromCache(List<K> keys, List<String> fields, Class<?> object) {
        Map<K, V> resultMap = new HashMap<>();
        for (K key : keys) {
            Map<String, Object> fieldMap = smartRedis.hMultiGetObject(encodeCacheKey(key, object), fields);
            if (!CollectionUtils.isEmpty(fieldMap)) {
                try {
                    boolean isAllFields = true;
                    //某些字段为空，也为缓存失效
                    for (String field : fields) {
                        if (!fieldMap.keySet().contains(field)) {
                            isAllFields = false;
                            break;
                        }
                    }
                    if (isAllFields) {
                        changeNullAndEmpty(fieldMap);
                        V v = (V) object.newInstance();
                        EnterpriseUtil.setObjectValueByFields(v, fieldMap);
                        resultMap.put(key, v);
                    }
                } catch (IllegalAccessException ila) {
                    LOGGER.error("class:" + object.getName() + "IllegalAccessException");
                    return resultMap;
                } catch (InstantiationException ite) {
                    LOGGER.error("class:" + object.getName() + "InstantiationException");
                    return resultMap;
                }
            }
        }
        return resultMap;
    }

    protected void put(K key, V v, List<String> fields, Class<?> object) {
        //根据type来确定用哪种set方法
        Map<String, Object> fieldMap = EnterpriseUtil.getFieldMap(v, fields);
        if (!CollectionUtils.isEmpty(fieldMap)) {
            changeNullAndEmpty(fieldMap);
            smartRedis.hMultiSetObject(encodeCacheKey(key, object), fieldMap);
            //对整个key设置过期时间
            smartRedis.expire(encodeCacheKey(key, object), getExpireSecond());
        }
    }

    @SuppressWarnings("unchecked")
    public void refresh(List<K> keys, List<String> fields, Class<?> object, boolean delFirst) {
        assertCacheType(object);
        if (delFirst) {
            delete(keys, object);
        }
        Map<K, V> dbMap = dbDataProvider.get(keys, fields, object);
        if (!CollectionUtils.isEmpty(dbMap)) {
            for (Map.Entry<K, V> entry : dbMap.entrySet()) {
                put(entry.getKey(), entry.getValue(), fields, object);
            }
        }

    }

    public void refresh(List<K> keys, List<String> fields, Class<?> object) {
        refresh(keys, fields, object, false);
    }

    @Override
    public SmartRedis getSmartRedis() {
        return smartRedis;
    }
}
