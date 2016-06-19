package com.cweeyii.cache.framework;

import com.cweeyii.rabbitmq.framework.vo.InternalMessageFactory;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gongdaoqi
 * Date: 15-1-12
 * Time: 上午10:29
 * To change this template use File | Settings | File Templates.
 */
public interface IDataReader<K, V> {

    Map<K, V> get(List<K> keys, List<String> fields, Class<?> object);

}
