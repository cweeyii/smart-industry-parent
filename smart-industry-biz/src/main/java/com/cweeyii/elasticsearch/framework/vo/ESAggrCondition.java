package com.cweeyii.elasticsearch.framework.vo;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

/**
 * Created by cweeyii on 27/6/16 ${EMAIL}.
 */
public class ESAggrCondition extends ESCondition {
    private String aggrField;
    private AggregationBuilder aggregationBuilder;

    public void setAggregation(String field) {
        this.aggrField=field;
        aggregationBuilder = AggregationBuilders.terms(aggrField).size(Integer.MAX_VALUE).field(field);
    }

    public AggregationBuilder getAggregationBuilder() {
        return aggregationBuilder;
    }

    public void setAggregationBuilder(AggregationBuilder aggregationBuilder) {
        this.aggregationBuilder = aggregationBuilder;
    }

    public String getAggrField() {
        return aggrField;
    }
}
