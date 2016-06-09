package com.cweeyii.richness.dao.martmapper;

import com.cweeyii.richness.domain.TradePoi;

import java.util.List;

public interface TradePoiMapper {

    void batchInsertOrUpdate(List<TradePoi> tradePois);

    List<TradePoi> findByPoiId(Long poiId);
}
