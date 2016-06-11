
package com.cweeyii.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by cweeyii on 11/6/16.
 */

public interface RedisDataBase {
    <K, V> void setObject(K key, V value);

    <K, V> V getObject(K key, Class<V> vClass);

   <K, V> V getObject(K key);

    <K> Long deleteKey(K key);

    <K> void append(K key, String value);

    <K, V> List<V> getListObject(K key, Class<V> vClass);

    <K, V> Set<V> getSetObject(K key, Class<V> vClass);
}

