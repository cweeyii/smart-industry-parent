package com.cweeyii.cache.framework;


import com.cweeyii.cache.framework.redis.SmartRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gongdaoqi
 * Date: 15/4/28
 * Time: 下午3:14
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ObjectCacheDataService<K, V> extends RedisCacheDataService<K, V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapCacheDataService.class);

    @Resource
    private SmartRedis smartRedis;

    @Resource
    private DBDataReader dbDataProvider;

    @SuppressWarnings("unchecked")
    @Override
    protected Map<K, V> getFromCache(List<K> keys, List<String> fields, Class<?> object) {
        Map<K,V> map = new HashMap<K, V>();
        if (keys == null || keys.isEmpty()) {
            return map;
        }
        List<String> mKeys = new ArrayList<String>();
        for (K key : keys) {
            mKeys.add(encodeCacheKey(key, object));
        }
        Map<String,V> cachedMap = (Map<String,V>) smartRedis.multiGetObject(mKeys);
        for (K key : keys) {
            String mKey = encodeCacheKey(key, object);
            V obj = cachedMap.get(mKey);
            map.put(key,obj);
        }
        return map;
    }

    protected void put(K key, V v, List<String> fields, Class<?> object) {
        smartRedis.setObject(encodeCacheKey(key, object), getExpireSecond(), v);
    }

    @SuppressWarnings("unchecked")
    public void refresh(List<K> keys, List<String> fields, Class<?> object) {
        refresh(keys, fields, object, false);
    }

    @Override
    public void refresh(List<K> keys, List<String> fields, Class<?> object, boolean delFirst) {
        assertCacheType(object);
        if (delFirst) {
            delete(keys, object);
        }
        Map<K, V> dbMap = dbDataProvider.get(keys, fields, object);
        if (!CollectionUtils.isEmpty(dbMap)) {
            Map<String,V> needCachedMap = new HashMap<String,V>();
            for (Map.Entry<K,V> entry : dbMap.entrySet()) {
                needCachedMap.put(encodeCacheKey(entry.getKey(), object), entry.getValue());
            }
            smartRedis.multiSetObject(needCachedMap, getExpireSecond());
        }
    }

    @Override
    public SmartRedis getSmartRedis() {
        return smartRedis;
    }

}
