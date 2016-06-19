package com.cweeyii.cache.framework;

import com.cweeyii.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gongdaoqi
 * Date: 15-4-15
 * Time: 上午10:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractDataReader<K,V> implements IDataReader<K,V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDataReader.class);

    @Override
    public Map<K,V> get(List<K> keys, List<String> fields, Class<?> object){
        List<String> effectiveFields = preProcess(fields, object);
        if (CollectionUtils.isEmpty(effectiveFields)){
            LOGGER.warn("有效字段列表为空");
            return new HashMap<>();
        }
        return getFromDataProvider(keys, effectiveFields, object);
    }

    protected abstract Map<K,V> getFromDataProvider(List<K> keys, List<String> fields, Class<?> object);

    public List<String> preProcess(List<String> fields, Class<?> object){
        List<String> effectiveFields = new ArrayList<>();
        //去除空格和不存在的字段值
        List<String> objectFields = new ArrayList<>();
        for (Field field : object.getDeclaredFields()){
            objectFields.add(field.getName());
        }

        for (int i = fields.size()-1; i>=0 ;i--){
            String fieldName = StringUtil.alltrimWithWideSpace(fields.get(i));
            if (!objectFields.contains(fieldName)){
                LOGGER.warn("对象：" + object.getSimpleName() + ";字段：" + fieldName + "不存在");
            } else {
                effectiveFields.add(fieldName);
            }
        }
        return effectiveFields;
    }

}
