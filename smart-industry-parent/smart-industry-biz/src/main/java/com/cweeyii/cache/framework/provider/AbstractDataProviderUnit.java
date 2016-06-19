package com.cweeyii.cache.framework.provider;

import java.util.List;
import java.util.Map;


public abstract class AbstractDataProviderUnit<K, V> {

    public String getProviderName() {
        return this.getClass().getSimpleName();
    }

    public abstract Map<K, V> getObjectFromDB(List<K> keys);

}
