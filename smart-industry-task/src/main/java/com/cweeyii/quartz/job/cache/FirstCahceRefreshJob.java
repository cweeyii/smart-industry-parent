package com.cweeyii.quartz.job.cache;

import com.cweeyii.baseinfo.core.service.EnterpriseBasicInfoService;
import com.cweeyii.cache.framework.MapCacheDataService;
import com.cweeyii.cache.framework.ObjectCacheDataService;
import com.cweeyii.cache.framework.vo.EnterpriseObjectView;
import com.cweeyii.cache.framework.vo.EnterpriseRichnessView;
import com.cweeyii.quartz.framework.AbstractQuartzJob;
import com.cweeyii.rabbitmq.framework.vo.InternalMessageFactory;
import com.google.common.collect.Lists;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by wenyi on 16/6/13.
 * Email:caowenyi@meituan.com
 */
@Service
public class FirstCahceRefreshJob extends AbstractQuartzJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(FirstCahceRefreshJob.class);
    @Resource
    private EnterpriseBasicInfoService enterpriseBasicInfoService;
    @Resource
    private MapCacheDataService mapCacheDataService;
    @Resource
    private ObjectCacheDataService<Long, EnterpriseRichnessView> objectCacheDataService;

    @Override
    public void action(JobExecutionContext context) throws Exception {
        LOGGER.info("开始运行" + getDescription());
        List<Long> ids = Lists.newArrayList(9028L, 9093L);
        Map<String, Object> beforeMap = mapCacheDataService.get(ids, Lists.newArrayList("cityName", "districtName"), EnterpriseRichnessView.class);

        //Thread.sleep(10000);
        Map<Long, EnterpriseRichnessView> objMap = objectCacheDataService.get(ids, Lists.newArrayList("id", "enterpriseName", "address", "phone", "businessCategory", "latitude", "longitude"), EnterpriseObjectView.class);

        mapCacheDataService.refresh(9028L, InternalMessageFactory.MessageType.POI_MODIFY_BASE, EnterpriseRichnessView.class);
        Map<String, Object> afterMap = mapCacheDataService.get(ids, Lists.newArrayList("cityName", "districtName"), EnterpriseRichnessView.class);
        LOGGER.info("运行结束" + getDescription());
    }

    @Override
    protected String getDescription() {
        return "redis缓存刷新";
    }
}
