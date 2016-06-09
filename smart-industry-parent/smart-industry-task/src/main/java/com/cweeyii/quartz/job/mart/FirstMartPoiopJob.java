package com.cweeyii.quartz.job.mart;

import com.cweeyii.quartz.framework.AbstractQuartzJob;
import com.cweeyii.richness.domain.TradePoi;
import com.cweeyii.richness.service.TradePoiService;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by cweeyii on 9/6/16.
 */
@Service
public class FirstMartPoiopJob extends AbstractQuartzJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(FirstMartPoiopJob.class);
    @Resource
    private TradePoiService tradePoiService;

    @Override
    public void action(JobExecutionContext context) throws Exception {
        LOGGER.info("开始运行" + getDescription());
        List<TradePoi> tradePois = tradePoiService.findByPoiId(11L);
        LOGGER.info(tradePois.toString());
        LOGGER.info("运行结束" + getDescription());
    }

    @Override
    protected String getDescription() {
        return "第一個MART定時任務";
    }
}
