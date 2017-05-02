package com.cweeyii.quartz.framework;


import com.cweeyii.quartz.framework.dao.mapper.QrtzJobDetailsMapper;
import com.cweeyii.quartz.framework.domain.QrtzJobDetails;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ClusteredSchedulerFactoryBean extends AbstractSchedulerFactoryBean {
    @Resource
    private List<AbstractQuartzJob> quartzJobs;
    @Resource
    private QrtzJobDetailsMapper qrtzJobDetailsMapper;


    private static final Logger LOGGER = LoggerFactory.getLogger(ClusteredSchedulerFactoryBean.class);

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected boolean isTaskServer() {
        return true;
    }

    public List<AbstractQuartzJob> getJobs() {
        List<AbstractQuartzJob> jobs = new ArrayList<>();
        for (AbstractQuartzJob job : quartzJobs) {
            jobs.add(job);
        }
        return jobs;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (isTaskServer()) {
            filterUnnecessaryJobs();
        }
    }

    private void filterUnnecessaryJobs() {
        List<QrtzJobDetails> jobDetailInfos = qrtzJobDetailsMapper.getAllJobDetails();
        Set<String> jobNames = new HashSet<>();
        for (AbstractQuartzJob job : this.getJobs()) {
            jobNames.add(job.getJobDetail().getKey().getName());
        }
        for (QrtzJobDetails jobDetailInfo : jobDetailInfos) {
            if (!jobNames.contains(jobDetailInfo.getJobName())) {
                try {
                    Scheduler scheduler = this.getScheduler();
                    scheduler.deleteJob(new JobKey(jobDetailInfo.getJobName(), "DEFAULT"));//并没有终止执行过程，只是这个job以后实效
                    LOGGER.info("过滤不必要job信息, [JobName＝" + jobDetailInfo.getJobName() + "]");
                } catch (Exception e) {
                    LOGGER.error("过滤job信息出错[jobName=" + jobDetailInfo.getJobName() + "]", e);
                }
            }
        }

    }

    @Override
    protected List<JobDetail> getJobDetails() {
        List<JobDetail> jobDetails = new ArrayList<>();
        for (AbstractQuartzJob job : getJobs()) {
            if (job.getJobDetail() == null) {
                throw new RuntimeException("[" + job.getBeanName() + "]jobDetail为空");
            }
            jobDetails.add(job.getJobDetail());
        }
        return jobDetails;
    }

    @Override
    protected List<Trigger> getTriggers() {
        List<Trigger> triggers = new ArrayList<>();
        return triggers;
    }
}
