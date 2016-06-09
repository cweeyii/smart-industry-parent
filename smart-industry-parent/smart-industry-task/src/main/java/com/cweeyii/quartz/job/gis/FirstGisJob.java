package com.cweeyii.quartz.job.gis;

import com.cweeyii.gis.domain.AdminDivisionAlias;
import com.cweeyii.gis.service.AdminDivisionAliasService;
import com.cweeyii.quartz.framework.AbstractQuartzJob;
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
public class FirstGisJob extends AbstractQuartzJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(FirstGisJob.class);
    @Resource
    private AdminDivisionAliasService adminDivisionAliasService;

    @Override
    public void action(JobExecutionContext context) throws Exception {
        LOGGER.info("开始运行" + getDescription());
        List<AdminDivisionAlias> adminDivisionAliases = adminDivisionAliasService.getADAliasByAdId(10154838);
        LOGGER.info(adminDivisionAliases.toString());
        LOGGER.info("运行结束" + getDescription());
    }

    @Override
    protected String getDescription() {
        return "第一個GIS定時任務";
    }
}
