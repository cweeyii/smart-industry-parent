package com.cweeyii.elasticsearch.framework.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cweeyii.elasticsearch.framework.annotation.ElasticSearchParam;
import com.cweeyii.elasticsearch.framework.client.ESClientConfig;
import com.cweeyii.elasticsearch.framework.vo.ESAggrCondition;
import com.cweeyii.elasticsearch.framework.vo.ESCondition;
import com.cweeyii.util.StringUtil;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.SortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        /*Map<String, ClusterIndexHealth> indexHealthMap = ESClientConfig.getClient().admin().cluster().health(new ClusterHealthRequest(indexName)).actionGet().getIndices();
        boolean exists = indexHealthMap.containsKey(indexName);
        if (!exists) {
            ESClientConfig.getClient().admin().indices()
                    .create(new CreateIndexRequest(indexName)).actionGet();
            // waitForYellow
            ESClientConfig.getClient().admin()
                    .cluster()
                    .health(new ClusterHealthRequest(indexName)
                            .waitForYellowStatus()).actionGet();
            try {
                XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject(indexName)
                        .startObject("properties")
                        .startObject("id").field("type", "long").field("store", "yes").endObject()
                        .startObject("enterprise_name").field("type", "string").field("store", "yes").field("index", "not_analyzed").endObject()
                        .startObject("longitude").field("type", "double").field("store", "yes").endObject()
                        .startObject("latitude").field("type", "double").field("store", "yes").endObject()
                        .startObject("city_name").field("type", "string").field("store", "yes").field("index", "not_analyzed").endObject()
                        .startObject("district_name").field("type", "string").field("store", "yes").field("index", "not_analyzed").endObject()
                        .startObject("address").field("type", "string").field("store", "yes").field("indexAnalyzer", "ik").field("searchAnalyzer", "ik").endObject()
                        .startObject("phone").field("type", "string").field("store", "yes").field("index", "not_analyzed").endObject()
                        .startObject("business_category").field("type", "string").field("store", "yes").field("index", "not_analyzed").endObject()
                        .endObject().endObject().endObject();
                //PutMappingRequest putMappingRequest = new PutMappingRequest(indexName);
                //putMappingRequest.type(typeName).source(mapping);
                PutMappingRequest mappingRequest = Requests.putMappingRequest(indexName).type(indexName).source(mapping);
                ESClientConfig.getClient().admin().indices().putMapping(mappingRequest).actionGet();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ESClientConfig.getClient().prepareIndex(indexName, typeName, indexId)
                .setRefresh(true).setSource(JSON.toJSONString(obj)).execute().actionGet();*/
        LOGGER.info("刷新elasticsearch indexName=[" + indexName + "],typeName=[" + typeName + "],id=[" + indexId + "]");

    }

    public void batchBuildIndex(List<T> objList) {
        if (CollectionUtils.isEmpty(objList)) {
            LOGGER.warn("empty obj for index");
            return;
        }
        if (StringUtils.isEmpty(indexName) || StringUtils.isEmpty(typeName)) {
            LOGGER.warn("add elasticsearchparam to indexVo");
            return;
        }
        Client client = ESClientConfig.getClient();
        BulkRequestBuilder bulk = client.prepareBulk();
        for (T obj : objList) {
            bulk.add(client.prepareIndex(indexName, typeName, getIndexId(obj))
                    .setSource(JSON.toJSONString(obj)));
        }
        bulk.setRefresh(true).execute().actionGet();
        LOGGER.info("批量刷新elasticsearch indexName=[" + indexName + "],typeName=[" + typeName + "]");
    }

    public void deleteIndex(String indexId) {
        if (StringUtils.isEmpty(indexId)) {
            LOGGER.warn("empty indexId for delete elasticsearch");
            return;
        }
        Client client = ESClientConfig.getClient();
        client.prepareDelete(indexName, typeName, indexId).setRefresh(true)
                .execute().actionGet();
        LOGGER.info("删除elasticsearch indexName=[" + indexName + "],typeName=[" + typeName + "], indexId=" + indexId + "]");
    }


    public String getIndexId(T obj) {
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

    public String getIndexName() {
        return indexName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void close() {
        ESClientConfig.close();
    }

    private SearchRequestBuilder getRequestBuilder(ESCondition condition){
        if (StringUtil.isBlank(indexName) || StringUtil.isBlank(typeName)) {
            throw new IllegalArgumentException("非法的indexName=[" + indexName + "],typeName=[" + typeName + "]");
        }
        SearchRequestBuilder requestBuilder = ESClientConfig.getClient().prepareSearch(indexName).setTypes(typeName);
        if(condition.getQueryBuilder()!=null){
            requestBuilder.setQuery(condition.getQueryBuilder());
        }else {
            requestBuilder.setQuery(QueryBuilders.matchAllQuery());
        }
        if(condition.getFilterBuilder()!=null){
            requestBuilder.setPostFilter(condition.getFilterBuilder());
        }
        List<SortBuilder> sortBuilders=condition.getSortBuilders();
        if(!CollectionUtils.isEmpty(sortBuilders)){
            for(SortBuilder sortBuilder:sortBuilders){
                requestBuilder.addSort(sortBuilder);
            }
        }
        if(condition.getFrom()<0){
            throw new IllegalArgumentException("from必须大于等于0");
        }
        if(condition.getSize()<=0){
            throw new IllegalArgumentException("size必须大于0");
        }
        requestBuilder.setFrom(condition.getFrom()).setSize(condition.getSize());
        return requestBuilder;
    }

    public List<T> getListByCondition(ESCondition condition) {
        SearchRequestBuilder requestBuilder=getRequestBuilder(condition);
        SearchResponse searchResponse=requestBuilder.execute().actionGet();
        List<T> objects = new ArrayList<T>();
        if (searchResponse == null) {
            return objects;
        }
        SearchHits hits = searchResponse.getHits();
        for (SearchHit searchHit : hits) {
            T obj = getObjectFromJsonString(searchHit.getSourceAsString());
            if (obj == null) {
                continue;
            }
            objects.add(obj);
        }
        return objects;

    }

    public Map<Object, String> getMapByAggrCondition(ESAggrCondition condition) {
        SearchRequestBuilder requestBuilder=getRequestBuilder(condition);
        if(condition.getAggregationBuilder()==null || StringUtil.isBlank(condition.getAggrField())){
            throw new IllegalArgumentException("聚类搜索的聚类条件不能为空");
        }
        requestBuilder.addAggregation(condition.getAggregationBuilder());
        SearchResponse searchResponse=requestBuilder.execute().actionGet();
        Terms terms=searchResponse.getAggregations().get(condition.getAggrField());
        Map<Object, String> resultsMap = new HashMap<>();
        if (terms != null) {
            for (Terms.Bucket entry : terms.getBuckets()) {
                resultsMap.put(entry.getKey(),
                        String.valueOf(entry.getDocCount()));
            }
        }
        return resultsMap;
    }


    private T getObjectFromJsonString(String sourceString) {
        T obj = null;
        try {
            obj = JSONObject.parseObject(sourceString, clazz);
        } catch (Exception e) {
            LOGGER.error("build result mapping failed,", e);
        }
        return obj;
    }

}
