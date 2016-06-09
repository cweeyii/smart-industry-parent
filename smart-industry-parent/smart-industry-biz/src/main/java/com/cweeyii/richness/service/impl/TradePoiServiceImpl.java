package com.cweeyii.richness.service.impl;

import com.cweeyii.richness.dao.martmapper.TradePoiMapper;
import com.cweeyii.richness.domain.TradePoi;
import com.cweeyii.richness.service.TradePoiService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by cweeyii on 9/6/16.
 */
@Service
public class TradePoiServiceImpl implements TradePoiService {
    @Resource
    private TradePoiMapper tradePoiMapper;

    @Override
    public void batchInsertOrUpdate(List<TradePoi> tradePois) {
        tradePoiMapper.batchInsertOrUpdate(tradePois);
    }

    @Override
    public List<TradePoi> findByPoiId(Long poiId) {
        return tradePoiMapper.findByPoiId(poiId);
    }
}
