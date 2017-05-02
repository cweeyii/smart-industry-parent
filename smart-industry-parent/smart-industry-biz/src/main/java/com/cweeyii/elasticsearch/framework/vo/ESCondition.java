package com.cweeyii.elasticsearch.framework.vo;


import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cweeyii on 20/6/16 ${EMAIL}.
 */
public class ESCondition {
    private BoolQueryBuilder queryBuilder;
    private BoolQueryBuilder filterBuilder;
    private List<SortBuilder> sortBuilders = new ArrayList<>();
    private int from;
    private int size;

    public enum BooleanOperate {
        MUST("must"), MUST_NOT("must_not"), SHOULD("should");
        private String operate;

        BooleanOperate(String operate) {
            this.operate = operate;
        }

        public String getOperate() {
            return operate;
        }
    }

    public void setQueryPair(String field, Object value) {
        setQueryPair(field, value, BooleanOperate.MUST);
    }

    public void setQueryPair(String field, Object value, BooleanOperate booleanOperate) {
        if (queryBuilder == null) {
            queryBuilder = QueryBuilders.boolQuery();
        }
        switch (booleanOperate) {
            case MUST:
                queryBuilder.must(QueryBuilders.matchQuery(field, value));
                break;
            case SHOULD:
                queryBuilder.should(QueryBuilders.matchQuery(field, value));
                break;
            case MUST_NOT:
                queryBuilder.mustNot(QueryBuilders.matchQuery(field, value));
                break;
            default:
                queryBuilder.must(QueryBuilders.matchQuery(field, value));
        }
    }

    public void setQueryString(String queryString) {
        if (queryBuilder == null) {
            queryBuilder = QueryBuilders.boolQuery();
        }
        queryBuilder.must(QueryBuilders.queryStringQuery(queryString));
    }

    private void setRangeQueryBuilder(BoolQueryBuilder queryBuilder, String field, Object minObj, Object maxObj, BooleanOperate booleanOperate) {
        switch (booleanOperate) {
            case MUST:
                queryBuilder.must(QueryBuilders.rangeQuery(field).gt(minObj).lt(maxObj));
                break;
            case SHOULD:
                queryBuilder.should(QueryBuilders.rangeQuery(field).gt(minObj).lt(maxObj));
                break;
            case MUST_NOT:
                queryBuilder.mustNot(QueryBuilders.rangeQuery(field).gt(minObj).lt(maxObj));
                break;
            default:
                queryBuilder.must(QueryBuilders.rangeQuery(field).gt(minObj).lt(maxObj));
        }
    }

    public void setRangeQuery(String field, Object minObj, Object maxObj) {
        setRangeQuery(field, minObj, maxObj, BooleanOperate.MUST);
    }

    public void setRangeQuery(String field, Object minObj, Object maxObj, BooleanOperate booleanOperate) {
        if (queryBuilder == null) {
            queryBuilder = QueryBuilders.boolQuery();
        }
        setRangeQueryBuilder(queryBuilder, field, minObj, maxObj, booleanOperate);
    }

    public void setRangeFilter(String field, Object minObj, Object maxObj) {
        if (filterBuilder == null) {
            filterBuilder = QueryBuilders.boolQuery();
        }
        setRangeQueryBuilder(filterBuilder, field, minObj, maxObj, BooleanOperate.MUST);
    }

    public void setRangeFilter(String field, Object minObj, Object maxObj, BooleanOperate booleanOperate) {
        setRangeQueryBuilder(filterBuilder, field, minObj, maxObj, booleanOperate);
    }

    public void setFilterPair(String field, Object value) {
        setFilterPair(field, value, BooleanOperate.MUST);
    }

    public void setFilterPair(String field, Object value, BooleanOperate booleanOperate) {
        if (queryBuilder == null) {
            queryBuilder = QueryBuilders.boolQuery();
        }
        switch (booleanOperate) {
            case MUST:
                queryBuilder.must(QueryBuilders.termQuery(field, value));
                break;
            case SHOULD:
                queryBuilder.should(QueryBuilders.termQuery(field, value));
                break;
            case MUST_NOT:
                queryBuilder.mustNot(QueryBuilders.termQuery(field, value));
                break;
            default:
                queryBuilder.must(QueryBuilders.termQuery(field, value));
        }
    }

    public QueryBuilder getQueryBuilder() {
        return queryBuilder;
    }

    public void setQueryBuilder(BoolQueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    public QueryBuilder getFilterBuilder() {
        return filterBuilder;
    }

    public void setFilterBuilder(BoolQueryBuilder filterBuilder) {
        this.filterBuilder = filterBuilder;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setSortField(String field, SortOrder sortOrder) {
        SortBuilder sortBuilder = SortBuilders.fieldSort(field).order(sortOrder);
        sortBuilders.add(sortBuilder);
    }

    public void setSortField(String field) {
        setSortField(field, SortOrder.ASC);
    }

    public List<SortBuilder> getSortBuilders() {
        return sortBuilders;
    }

    public void setSortBuilders(List<SortBuilder> sortBuilders) {
        this.sortBuilders = sortBuilders;
    }
}
