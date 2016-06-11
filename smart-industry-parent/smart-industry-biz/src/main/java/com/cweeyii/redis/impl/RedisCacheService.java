package com.cweeyii.redis.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cweeyii.redis.RedisDataBase;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;
import java.util.*;


@SuppressWarnings("unused,unchecked")
@Service
public class RedisCacheService implements RedisDataBase {
    @Resource
    private ShardedJedisPool shardedJedisPool;

    @Override
    public <K, V> void setObject(K key, V value) {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String strKey = JSON.toJSONString(key);
        String strValue = JSON.toJSONString(value);
        shardedJedis.set(strKey, strValue);
    }

    @Override
    public <K, V> V getObject(K key, Class<V> vClass) {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String strKey = JSON.toJSONString(key);
        String strValue = shardedJedis.get(strKey);
        return JSON.parseObject(strValue, vClass);
    }

    @Override
    public <K, V> List<V> getListObject(K key, Class<V> vClass) {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String strKey = JSON.toJSONString(key);
        String strValue = shardedJedis.get(strKey);
        return JSON.parseArray(strValue, vClass);
    }

    @Override
    public <K, V> Set<V> getSetObject(K key, Class<V> vClass) {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String strKey = JSON.toJSONString(key);
        String strValue = shardedJedis.get(strKey);
        Set<JSONObject> set = JSON.parseObject(strValue, Set.class);
        Set<V> retSet = new HashSet<>();
        for (JSONObject object : set) {
            retSet.add(JSONObject.toJavaObject(object, vClass));
        }
        return retSet;
    }

    @Override
    public <K, V> V getObject(K key) {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String strKey = JSON.toJSONString(key);
        String strValue = shardedJedis.get(strKey);
        return (V) JSON.parse(strValue);
    }

    @Override
    public <K> Long deleteKey(K key) {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String strKey = JSON.toJSONString(key);
        return shardedJedis.del(strKey);
    }

    @Override
    public <K> void append(K key, String value) {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String strKey = JSON.toJSONString(key);
        String oldValue = getObject(key, String.class);
        String newValue = oldValue + value;
        String strValue = JSON.toJSONString(newValue);
        shardedJedis.set(strKey, strValue);
    }

}
