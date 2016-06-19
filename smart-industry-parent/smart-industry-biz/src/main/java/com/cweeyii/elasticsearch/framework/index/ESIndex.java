package com.cweeyii.elasticsearch.framework.index;

import com.alibaba.fastjson.JSON;
import com.cweeyii.elasticsearch.framework.annotation.ElasticSearchParam;
import com.cweeyii.elasticsearch.framework.client.ESClientConfig;
import jdk.nashorn.internal.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

/**
 * Created by wenyi on 16/6/19.
 * Email:caowenyi@meituan.com
 */
public class ESIndex<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ESIndex.class);
    private Class<T> clazz;
    private String indexName;
    private String typeName;

    public ESIndex() {
        this.clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (!clazz.isAnnotationPresent(ElasticSearchParam.class)
                || StringUtils.isEmpty(clazz.getAnnotation(ElasticSearchParam.class).indexName())
                || StringUtils.isEmpty(clazz.getAnnotation(ElasticSearchParam.class).typeName())) {
            throw new IllegalArgumentException("illegal index name");
        }
        indexName = clazz.getAnnotation(ElasticSearchParam.class).indexName();
        typeName = clazz.getAnnotation(ElasticSearchParam.class).typeName();
    }

    public void buildIndex(T obj) {
        String indexId = getIndexId(obj);
        if (StringUtils.isEmpty(indexName) || StringUtils.isEmpty(typeName) || StringUtils.isEmpty(indexId)) {
            throw new IllegalArgumentException("illegal index name");
        }
        ESClientConfig.getClient().prepareIndex(indexName, typeName, indexId)
                .setRefresh(true).setSource(JSON.toJSONString(obj)).execute().actionGet();
        LOGGER.info("刷新es refresh,indexName=[" + indexName + "],typeName=[" + typeName + "]");

    }

    protected String getIndexId(T obj) {
        String indexId = null;
        Class<T> c = (Class<T>) obj.getClass();
        try {
            Method method = c.getMethod("getId", new Class[]{});
            indexId = method.invoke(obj, new Object[]{}).toString();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.warn("getId not exist to generate index id");
        }
        return indexId;
    }

    public void close() {
        ESClientConfig.close();
    }

}
