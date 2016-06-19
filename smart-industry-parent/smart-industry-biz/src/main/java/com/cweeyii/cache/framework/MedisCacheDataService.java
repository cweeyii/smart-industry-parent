package com.cweeyii.cache.framework;

import com.cweeyii.cache.framework.annotation.CacheEntity;
import com.cweeyii.cache.framework.annotation.MedisDataType;
import com.cweeyii.cache.framework.manager.DataProviderManager;
import com.cweeyii.rabbitmq.framework.vo.InternalMessageFactory;
import com.cweeyii.cache.framework.redis.SmartRedis;
import com.cweeyii.util.EnterpriseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;


@Service
public abstract class MedisCacheDataService<K,V> extends AbstractDataReader<K,V>{

    private static final Logger LOGGER = LoggerFactory.getLogger(MedisCacheDataService.class);

    @Resource
    private DBDataReader dbDataProvider;

    @Resource
    private DataProviderManager dataProviderManager;

    private static final Empty EMPTY = Empty.empty();

    protected int getExpireSecond(){
        //随机时间过期，7天到30天的随机时间
        return EnterpriseUtil.random(7, 30) * 86400;
    }

    /**
     * key值组成
     * mtpoiop.StagePoiView.Map.K
     * @param key
     * @param viewClass
     * @return
     */
    protected String encodeCacheKey(K key, Class<?> viewClass){
        return genCacheKeyPrefix(viewClass) + key;
    }

    protected String genCacheKeyPrefix(Class<?> viewClass) {
        return viewClass.getSimpleName() + "." + dataProviderManager.getTypeByEntity(viewClass.getSimpleName()) + ".";
    }

    protected Map<K, V> getFromDataProvider(List<K> keys, List<String> fields, Class<?> object){
        Map<K,V> resultMap = new HashMap<>();
        //从缓存读取，并返回
        Map<K,V> cachedMap = getFromCache(keys,fields,object);
        if (!CollectionUtils.isEmpty(cachedMap)){
            resultMap.putAll(cachedMap);
        }
        Set<K> unCachedKeys = new HashSet<>();
        for (K key : keys){
            if (cachedMap.get(key) == null){
                unCachedKeys.add(key);
            }
        }
        //按字段对key进行缓存刷新并merge至object
        if(!CollectionUtils.isEmpty(unCachedKeys)){
            LOGGER.warn("Provider缓存穿透：id[" + unCachedKeys + "]");
            Map<K,V> dbMap = dbDataProvider.get(new ArrayList(unCachedKeys),fields,object);
            //refresh to cache
            for(Map.Entry<K,V> entry:dbMap.entrySet()){
//                V v = resultMap.get(entry.getKey());
                resultMap.put(entry.getKey(),entry.getValue());
                //refresh缓存
                put(entry.getKey(),entry.getValue(),fields,object);
            }
        }
        return resultMap;
    }

    public void refresh(K key, InternalMessageFactory.MessageType messageType, Class<?> object){
        Set<String> fields = dataProviderManager.getFieldsByMessageTypeAndClass(messageType, object);
        if(CollectionUtils.isEmpty(fields)){
            LOGGER.error("未给消息类型：" + messageType + "设置对应的fields");
            throw new RuntimeException("未给消息类型：" + messageType + "设置对应的fields");
        }
        assertCacheType(object);
        refresh(new ArrayList<>(Arrays.asList(key)), new ArrayList<>(fields), object);
    }

    protected void assertCacheType(Class<?> viewClass) {
        CacheEntity annotation = viewClass.getAnnotation(CacheEntity.class);
        if (annotation == null) {
            throw new AssertionError("Medis view should annotationed by CacheEntity");
        }
        MedisDataType dataType = annotation.value();
        if (dataType == null) {
            throw new AssertionError("Medis view 's annotation CacheEntity 's dataType is null");
        }
        if (this instanceof MapCacheDataService) {
            if (MedisDataType.Object.equals(dataType)) {
                throw new AssertionError("MapCacheDataService 's operation 's param is MedisDataType.Object");
            }
        } else if (this instanceof ObjectCacheDataService) {
            if (MedisDataType.Map.equals(dataType)) {
                throw new AssertionError("ObjectCacheDataService 's operation 's param is MedisDataType.Map");
            }
        }
    }

    public void delete(List<K> keys, Class<?> object) {
        assertCacheType(object);
        List<String> medisKeys = new ArrayList<>();
        for (K key : keys) {
            medisKeys.add(encodeCacheKey(key, object));
        }
        if (!medisKeys.isEmpty()) {
            getSmartRedis().multiDelete(medisKeys);
        }
    }

    public abstract SmartRedis getSmartRedis();

    public abstract void refresh(List<K> keys, List<String> fields, Class<?> object);

    public abstract void refresh(List<K> keys, List<String> fields, Class<?> object, boolean delFirst);

    protected abstract Map<K, V> getFromCache(List<K> keys, List<String> fields, Class<?> object);

    protected abstract void put(K key, V value, List<String> fields, Class<?> object);

    protected void changeNullAndEmpty(Map<String, Object> fieldValueMap){
        //null与Empty转换
        for (Map.Entry<String, Object> entry : fieldValueMap.entrySet()){
            if (entry.getValue() == null){
                fieldValueMap.put(entry.getKey(), EMPTY);
                continue;
            }
            //redis反序列化的对象已经不是原来set的对象，因此只能用class来判断{
            if (entry.getValue().getClass().equals(EMPTY.getClass())){
                fieldValueMap.put(entry.getKey(),null);
            }
        }
    }

    public static class Empty implements Serializable {
        private static final long serialVersionUID = 5595602013751955775L;
        private volatile static Empty empty;
        public static Empty empty() {
            if (empty == null) {
                synchronized (Empty.class) {
                    if (empty == null) {
                        empty = new Empty();
                    }
                }
            }
            return empty;
        }
    }
}
