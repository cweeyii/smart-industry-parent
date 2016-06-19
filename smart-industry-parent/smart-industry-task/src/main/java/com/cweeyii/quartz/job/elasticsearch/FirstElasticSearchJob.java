package com.cweeyii.quartz.job.elasticsearch;

import com.cweeyii.baseinfo.core.domain.EnterpriseBasicInfo;
import com.cweeyii.baseinfo.core.service.EnterpriseBasicInfoService;
import com.cweeyii.elasticsearch.framework.index.EnterpriseESIndex;
import com.cweeyii.elasticsearch.framework.vo.EnterpriseIndexView;
import com.cweeyii.quartz.framework.AbstractQuartzJob;
import com.cweeyii.util.EnterpriseUtil;
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
public class FirstElasticSearchJob extends AbstractQuartzJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(FirstElasticSearchJob.class);
    @Resource
    private EnterpriseBasicInfoService enterpriseBasicInfoService;

    private EnterpriseESIndex enterpriseESIndex = new EnterpriseESIndex();

    @Override
    public void action(JobExecutionContext context) throws Exception {
        LOGGER.info("开始运行" + getDescription());
        List<Long> ids = Lists.newArrayList(9028L, 9093L);
        List<EnterpriseBasicInfo> basicInfos = enterpriseBasicInfoService.findByIds(ids);
        for (EnterpriseBasicInfo basicInfo : basicInfos) {
            EnterpriseIndexView enterpriseIndexView = new EnterpriseIndexView();
            EnterpriseUtil.copyProperties(basicInfo, enterpriseIndexView);
            enterpriseESIndex.buildIndex(enterpriseIndexView);
        }
        LOGGER.info("运行结束" + getDescription());
    }

    @Override
    protected String getDescription() {
        return "elasticsearch索引重建";
    }
}
