package com.cweeyii.quartz.job.redis;

import com.alibaba.fastjson.JSON;
import com.cweeyii.baseinfo.core.domain.StagePoi;
import com.cweeyii.baseinfo.core.service.StagePoiService;
import com.cweeyii.quartz.framework.AbstractQuartzJob;
import com.cweeyii.redis.impl.RedisCacheService;
import com.cweeyii.richness.domain.TradePoi;
import com.cweeyii.richness.service.TradePoiService;
import org.elasticsearch.common.collect.Lists;
import org.elasticsearch.common.collect.Sets;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by cweeyii on 9/6/16.
 */
@Service
public class FirstRedisJob extends AbstractQuartzJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(FirstRedisJob.class);
    @Resource
    private StagePoiService stagePoiService;
    @Resource
    private TradePoiService tradePoiService;
    @Resource
    private RedisCacheService redisCacheService;

    @Override
    public void action(JobExecutionContext context) throws Exception {
        LOGGER.info("开始运行" + getDescription());
        List<TradePoi> tradePois = tradePoiService.findByPoiId(11L);
        redisCacheService.setObject("trade.pois", tradePois);
        List<TradePoi> newTrades = (List<TradePoi>) redisCacheService.getListObject("trade.pois", TradePoi.class);
        LOGGER.info(newTrades.toString());
        StagePoi poi = stagePoiService.findById(11L);
        redisCacheService.setObject("single.poi", poi);
        List<StagePoi> pois = stagePoiService.findByIds(Lists.newArrayList(11L, 73L));
        redisCacheService.setObject("multi.poi", pois);
        StagePoi newPoi = redisCacheService.getObject("single.poi", StagePoi.class);
        LOGGER.info(newPoi.toString());
        List<StagePoi> newPois = redisCacheService.getListObject("multi.poi", StagePoi.class);
        LOGGER.info(newPois.toString());
        Set<StagePoi> set= Sets.newHashSet(poi,newPoi);
        redisCacheService.setObject("set.poi", set);
        Set<StagePoi> newSet=redisCacheService.getSetObject("set.poi", StagePoi.class);
        LOGGER.info(newSet.toString());
        Map<String, StagePoi> map = new HashMap<>();
        map.put("poi1", poi);
        map.put("po12", newPoi);
        redisCacheService.setObject("map.poi", map);
        Map<String, StagePoi> newMap = redisCacheService.getObject("map.poi");
        LOGGER.info(newMap.toString());
        LOGGER.info("运行结束" + getDescription());
    }

    @Override
    protected String getDescription() {
        return "第一個MQ定時任務";
    }
}
