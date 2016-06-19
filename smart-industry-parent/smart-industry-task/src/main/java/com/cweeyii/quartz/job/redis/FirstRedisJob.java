package com.cweeyii.quartz.job.redis;

import com.cweeyii.baseinfo.core.domain.EnterpriseBasicInfo;
import com.cweeyii.baseinfo.core.service.EnterpriseBasicInfoService;
import com.cweeyii.quartz.framework.AbstractQuartzJob;
import com.cweeyii.cache.framework.redis.SmartRedis;

import com.google.common.collect.Lists;
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
    private EnterpriseBasicInfoService enterpriseBasicInfoService;
    @Resource
    private SmartRedis medisPool;

    @Override
    public void action(JobExecutionContext context) throws Exception {
        LOGGER.info("开始运行" + getDescription());
        List<Long> poiIds = Lists.newArrayList(11L, 63L, 65L, 71L, 73L, 83L, 85L, 91L, 93L);
        List<EnterpriseBasicInfo> basicInfos = enterpriseBasicInfoService.findByIds(poiIds);
        for (EnterpriseBasicInfo basicInfo : basicInfos) {
            medisPool.setObject("poi." + basicInfo.getId().toString(), 600, basicInfo);
        }
        Iterator keysIterator = medisPool.scan("poi.*", 3);
        while (keysIterator.hasNext()) {
            List<String> keys = (List<String>) keysIterator.next();
            if (keys != null && !keys.isEmpty()) {
                LOGGER.info(keys.toString());
            }
        }
        LOGGER.info("运行结束" + getDescription());
    }

    @Override
    protected String getDescription() {
        return "第一個MQ定時任務";
    }
}
