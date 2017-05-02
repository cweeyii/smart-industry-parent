package com.cweeyii.cache.framework;


import com.cweeyii.cache.framework.provider.AbstractDataRepertoryUnit;
import com.cweeyii.cache.framework.manager.DataRepertoryManager;
import com.cweeyii.util.EnterpriseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: gongdaoqi
 * Date: 15-1-12
 * Time: 上午10:40
 * To change this template use File | Settings | File Templates.
 */
@Service
public class DBDataReader<K,V> extends AbstractDataReader<K,V> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBDataReader.class);

    @Resource
    private DataRepertoryManager dataProviderManager;


    @SuppressWarnings("unchecked")
    @Override
    protected Map<K, V> getFromDataProvider(List<K> keys, List<String> fields, Class<?> object) {
        Map<K,V> resultMap = new HashMap<>();
        List<K> existKeys = new ArrayList<>(keys);

        //根据baseDataProvider对数据进行过滤
        AbstractDataRepertoryUnit baseDataProviderUnit = dataProviderManager.getBaseDataProviderUnitByType(object.getSimpleName());
        if(baseDataProviderUnit != null){
            resultMap = baseDataProviderUnit.getObjectFromDB(keys);
            //POI必须存在
            existKeys = new ArrayList<>(resultMap.keySet());
        }

        //获取数据提供单元
        Set<AbstractDataRepertoryUnit> providerUnits
                = dataProviderManager.getDataProviderUnitsByFieldsAndClass(fields,object.getSimpleName());
        if(CollectionUtils.isEmpty(providerUnits)){
            LOGGER.info("数据提供单元为BaseDataProvider；fields = " + fields);
            return resultMap;
        }
        for(AbstractDataRepertoryUnit providerUnit:providerUnits){
            //从数据库读取对应字段的值
            Map<K,V> dbMap = providerUnit.getObjectFromDB(existKeys);
            for(Map.Entry<K,V> entry:dbMap.entrySet()){
                if(resultMap.containsKey(entry.getKey())){
                    V v = resultMap.get(entry.getKey());
                    //按字段合并object
                    EnterpriseUtil.mergeObject(entry.getValue(), v, dataProviderManager.getFieldsByDataProviderUnit(providerUnit.getProviderName()));
                    resultMap.put(entry.getKey(),v);
                }else{
                    resultMap.put(entry.getKey(),entry.getValue());
                }
            }
        }
        return resultMap;
    }

}
