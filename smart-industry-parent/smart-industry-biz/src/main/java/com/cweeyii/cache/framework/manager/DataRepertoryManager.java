package com.cweeyii.cache.framework.manager;


import com.cweeyii.cache.framework.annotation.ClassDataRepertory;
import com.cweeyii.cache.framework.annotation.CacheDataType;
import com.cweeyii.cache.framework.annotation.CacheEntity;
import com.cweeyii.cache.framework.annotation.ItemProviderUnit;
import com.cweeyii.cache.framework.provider.AbstractDataRepertoryUnit;
import com.cweeyii.rabbitmq.framework.vo.InternalMessageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;


@Service
public class DataRepertoryManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataRepertoryManager.class);

    @Resource
    private List<AbstractDataRepertoryUnit> dataRepertoryUnits;

    @Autowired
    private ApplicationContext context;

    //Map<simpleClassName,Map<fieldName,Object>>，vo的class名称与providerUnit值map的map
    private static Map<String, Map<String, AbstractDataRepertoryUnit>> dataRepertoryMap = new HashMap<>();

    //providerUnit与字段list的对应map
    private static Map<String, List<String>> providerFieldsMap = new HashMap<>();

    //simpleClassName与dataType的对应map
    private static Map<String, CacheDataType> entityTypeMap = new HashMap<>();

    //providerUnit名称与值object的对应map
    private static Map<String, AbstractDataRepertoryUnit> providerNameMap = new HashMap<>();

    //消息类型与字段的对应
    private static Map<Class<?>, Map<InternalMessageFactory.MessageType,Set<String>>> messageTypeFieldsMap = new HashMap<>();

    //vo基本信息提供unit的对应map
    private static Map<String, String> classDataRepertoryMap = new HashMap<>();

    public Map<String, AbstractDataRepertoryUnit> getFieldProviderMapByClass(String simpleClassName) {
        return dataRepertoryMap.get(simpleClassName);
    }

    public Set<String> getFieldsByMessageTypeAndClass(InternalMessageFactory.MessageType type, Class<?> object) {
        Map<InternalMessageFactory.MessageType,Set<String>> typeFieldsMap = messageTypeFieldsMap.get(object);
        if (CollectionUtils.isEmpty(typeFieldsMap)){
            LOGGER.info("对象:" + object.getName() + "所有字段没有设置消息队列");
            return new HashSet<>();
        }
        return typeFieldsMap.get(type);
    }

    public List<String> getFieldsByDataProviderUnit(String dataProviderUnit){
        return providerFieldsMap.get(dataProviderUnit);
    }

    public Set<AbstractDataRepertoryUnit> getDataProviderUnitsByFieldsAndClass(List<String> fields, String simpleClassName) {
        Set<AbstractDataRepertoryUnit> units = new HashSet<>();
        Map<String, AbstractDataRepertoryUnit> fieldUnitMap = getFieldProviderMapByClass(simpleClassName);
        for (String field : fields) {
            if (fieldUnitMap.get(field) != null) {
                units.add(fieldUnitMap.get(field));
            }
        }
        //不需要包含BaseDataProviderUnit
        if (getBaseDataProviderUnitByType(simpleClassName) != null) {
            units.remove(getBaseDataProviderUnitByType(simpleClassName));
        }
        return units;
    }

    public CacheDataType getTypeByEntity(String entity) {
        return entityTypeMap.get(entity);
    }

    public AbstractDataRepertoryUnit getBaseDataProviderUnitByType(String simpleClassName) {
        if (classDataRepertoryMap.get(simpleClassName) != null) {
            return providerNameMap.get(classDataRepertoryMap.get(simpleClassName));
        } else {
            return null;
        }
    }

    @PostConstruct
    private void init() throws RuntimeException {

        long start = System.currentTimeMillis();
        for (AbstractDataRepertoryUnit providerUnit : dataRepertoryUnits) {
            providerNameMap.put(providerUnit.getProviderName(), providerUnit);
        }

        Map<String, Object> CacheEntityBeanMap = context.getBeansWithAnnotation(CacheEntity.class);
        for (Map.Entry<String, Object> entry : CacheEntityBeanMap.entrySet()) {
            Class<?> c = entry.getValue().getClass();
            Map<String, AbstractDataRepertoryUnit> fieldProviderMap = new HashMap<>();
            Map<InternalMessageFactory.MessageType,Set<String>> typeFieldsMap = new HashMap<>();
            //field-cacheName Map
            CacheEntity ce = c.getAnnotation(CacheEntity.class);
            if (ce == null) {
                continue;
            }
            entityTypeMap.put(c.getSimpleName(), ce.value());
            Field[] fields = c.getDeclaredFields();
            for (Field f : fields) {
                if (f.getName().equals("serialVersionUID")) {
                    continue;
                }
                ItemProviderUnit unit = f.getAnnotation(ItemProviderUnit.class);
                if (unit == null) {
                    LOGGER.error("未给字段：" + f.getName() + "设置@providerunit");
                    throw new RuntimeException("view类:" + c.getName() + "字段:" + f.getName() + "未设置@providerunit");
                } else {
                    if (providerNameMap.get(unit.dataProviderUnit()) == null) {
                        LOGGER.error("未给字段：" + f.getName() + "设置正确的@providerunit");
                        throw new RuntimeException("view类:" + c.getName() + "字段:" + f.getName() + "未设置正确的@providerunit");
                    }

                    fieldProviderMap.put(f.getName(), providerNameMap.get(unit.dataProviderUnit()));

                    //处理dataProviderUnit注解
                    dealWithProviderUnit(unit.dataProviderUnit(), f.getName(), providerFieldsMap);

                    //处理relatedMessageType注解
                    dealWithMessageType(unit.relatedMessageType(), f.getName(), typeFieldsMap);
                }
            }
            dataRepertoryMap.put(c.getSimpleName(), fieldProviderMap);
            messageTypeFieldsMap.put(c,typeFieldsMap);
        }
        dealWithBaseDataProvider();
        long end = System.currentTimeMillis();
        LOGGER.info("初始化DataProviderManager成功, use time=" + (end - start) + " ms");
    }

    //处理unitNameFieldsMap
    private void dealWithProviderUnit(String unitName, String fieldName, Map<String, List<String>> unitNameFieldsMap) {
        if (providerNameMap.get(unitName) == null) {
            LOGGER.error("未给字段：" + fieldName + "设置正确的@providerunit");
            throw new RuntimeException("未给字段：" + fieldName + "设置正确的@providerunit");
        }
        List<String> providerFields = new ArrayList<>();
        if (unitNameFieldsMap.get(unitName) != null) {
            providerFields = unitNameFieldsMap.get(unitName);
        }
        providerFields.add(fieldName);
        unitNameFieldsMap.put(unitName, providerFields);

    }

    //处理MessageTypeFieldsMap
    private void dealWithMessageType(InternalMessageFactory.MessageType[] types, String fieldName,
                                     Map<InternalMessageFactory.MessageType, Set<String>> msgTypeFieldsMap) {
        if (types == null) {
            LOGGER.warn("未给字段：" + fieldName + "设置@relatedMessageType值注解");
            throw new RuntimeException("未给字段：" + fieldName + "设置@relatedMessageType值注解");
        }
        Set<String> fields = new HashSet<>();
        for (InternalMessageFactory.MessageType type : types) {
            if (msgTypeFieldsMap.get(type) == null) {
                fields.add(fieldName);
                msgTypeFieldsMap.put(type, fields);
            } else {
                fields = msgTypeFieldsMap.get(type);
                fields.add(fieldName);
                msgTypeFieldsMap.put(type, fields);
            }
        }
    }

    private void dealWithBaseDataProvider() {
        Map<String, Object> cacheEntityBeanMap = context.getBeansWithAnnotation(ClassDataRepertory.class);
        if (CollectionUtils.isEmpty(cacheEntityBeanMap)) {
            LOGGER.warn("未设置ClassDataRepertory");
            return;
        }
        for (Map.Entry<String, Object> entry : cacheEntityBeanMap.entrySet()) {
            Class<?> c = entry.getValue().getClass();
            ClassDataRepertory bdp = c.getAnnotation(ClassDataRepertory.class);
            if (!providerNameMap.keySet().contains(bdp.value())) {
                LOGGER.error("ClassDataRepertory不存在");
                throw new RuntimeException("ClassDataRepertory:" + bdp.value() + "不存在");
            }
            classDataRepertoryMap.put(c.getSimpleName(), bdp.value());
        }
    }

}