package com.cweeyii.richness.service;

import com.cweeyii.richness.domain.TradePoi;

import java.util.List;

/**
 * Created by cweeyii on 9/6/16.
 */
public interface TradePoiService {
    void batchInsertOrUpdate(List<TradePoi> tradePois);

    List<TradePoi> findByPoiId(Long poiId);
}
