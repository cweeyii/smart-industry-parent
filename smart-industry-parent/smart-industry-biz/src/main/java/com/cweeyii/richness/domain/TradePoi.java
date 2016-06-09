package com.cweeyii.richness.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Created by cweeyii on 9/6/16.
 */
public class TradePoi {
    private Long poiId;
    private String source;

    public Long getPoiId() {
        return poiId;
    }

    public void setPoiId(Long poiId) {
        this.poiId = poiId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
