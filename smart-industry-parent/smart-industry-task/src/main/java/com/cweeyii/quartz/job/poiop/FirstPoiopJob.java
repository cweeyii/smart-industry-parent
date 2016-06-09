package com.cweeyii.quartz.job.poiop;

import com.cweeyii.baseinfo.core.domain.StagePoi;
import com.cweeyii.baseinfo.core.service.StagePoiService;
import com.cweeyii.quartz.framework.AbstractQuartzJob;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by cweeyii on 9/6/16.
 */
@Service
public class FirstPoiopJob extends AbstractQuartzJob {
    private static final Logger LOGGER= LoggerFactory.getLogger(FirstPoiopJob.class);
    @Resource
    private StagePoiService stagePoiService;

    @Override
    public void action(JobExecutionContext context) throws Exception {
        LOGGER.info("开始运行" + getDescription());
        StagePoi stagePoi=stagePoiService.findById(11L);
        LOGGER.info(stagePoi.toString());
        LOGGER.info("运行结束"+getDescription());
    }

    @Override
    protected String getDescription() {
        return "第一個POIOP定時任務";
    }
}
