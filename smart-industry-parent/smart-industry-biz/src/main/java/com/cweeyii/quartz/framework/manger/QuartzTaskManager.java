package com.cweeyii.quartz.framework.manger;

import com.cweeyii.quartz.framework.AbstractQuartzJob;
import com.cweeyii.quartz.framework.ClusteredSchedulerFactoryBean;
import com.cweeyii.quartz.framework.vo.ArgsJobExecutionContext;
import com.cweeyii.util.StringUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Created by fancong on 15/4/16.
 */
@Service
public class QuartzTaskManager {
    @Resource
    private ClusteredSchedulerFactoryBean mtClusteredSchedulerFactoryBean;
    
    private static Set<String> RUNNING_JOB = new HashSet<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzTaskManager.class);
    private static final  String TRIGGER_GROUP_NAME = "DEFAULT";



    /**
     * 立即启动一个job
     * @param jobName 任务名称
     * @return 执行状态 -2表示不在这个job，1表示运行成功，－3表示已经在运行
     * @throws SchedulerException
     */
    public Integer startJob(final String jobName, final Map<String, String> args) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, "DEFAULT");
        if (StringUtil.isBlank(jobName) || mtClusteredSchedulerFactoryBean.getScheduler().getJobDetail(jobKey) == null) {
            return -2;
        }
    	//是否线下环境
    	String environment = System.getProperty("environment");
        synchronized (mtClusteredSchedulerFactoryBean) {
        	if ("online".equals(environment)) {
        		Scheduler scheduler = mtClusteredSchedulerFactoryBean.getScheduler();
                boolean jobState = false;
                //加锁控制并发问题？？
                for (JobExecutionContext jobExeContext : scheduler.getCurrentlyExecutingJobs()) {
                    if (jobExeContext.getJobDetail().getKey().equals(jobKey))
                        jobState = true;
                }
                if (!jobState) {
                    JobDataMap jobDataMap = new JobDataMap();
                    if(args != null) {
                        for(Map.Entry<String,String> entry: args.entrySet()) {
                            jobDataMap.put(entry.getKey(), entry.getValue());
                        }
                    }
                    scheduler.triggerJob(jobKey, jobDataMap);
                    return 1;
                } else
                    return -3;
        	} else {
        		if (RUNNING_JOB.contains(jobName)) {
        			return -3;
        		}
        		RUNNING_JOB.add(jobName);
        		final AbstractQuartzJob job = getQuartzJobByName(jobName);
        		if (job == null) {
        			return -2;
        		}
        		//如果是线下环境直接开一个线程来执行job
        		new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							long t1 = System.currentTimeMillis();
							LOGGER.info("[jobName=" + jobName + "]任务开始执行");
                            ArgsJobExecutionContext ctx = new ArgsJobExecutionContext(args);
							job.action(ctx);
							long t2 = System.currentTimeMillis();
							LOGGER.info("[jobName=" + jobName + "]任务执行成功(cost="+ (t2 - t1) +")");
						} catch (Exception e) {
							LOGGER.error("[jobName=" + jobName + "]执行失败", e);
						}finally{
							RUNNING_JOB.remove(jobName);
						}
					}
				}).start();
        		return 1;
        	}
        }
    }
    
    
    private AbstractQuartzJob getQuartzJobByName(String jobName){
    	List<AbstractQuartzJob> abstractQuartzJobs = mtClusteredSchedulerFactoryBean.getJobs();
    	for (AbstractQuartzJob _job : abstractQuartzJobs) {
    		if (_job.getJobDetail().getKey().getName().equals(jobName)) {
    			return _job;
    		}
    	}
    	return null;
    }


    /**
     * 判断时间表达式的合法性
     * @param cronExpression 时间表达式
     * @return 执行状态，1表示Cron合法，－1表示不合法
     */
    private Integer isNormal(String cronExpression) {
        try {
            cronSchedule(cronExpression);
            return 1;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     *  给job添加一个触发器
     * @param jobName 任务名称
     * @param cronExpression
     * @return －1表示不存在这个job，－3表示这个job没有trigger，－5表示Cron不合法，1表示修改成功
     * @throws SchedulerException
     */
    public Integer addTrigger(String jobName, String cronExpression) throws SchedulerException {
        //判断参数是否合法？？？？？？？
        if (StringUtil.isBlank(jobName)) {
            return -1;
        }
        JobKey jobKey = new JobKey(jobName, "DEFAULT");
        Scheduler scheduler = mtClusteredSchedulerFactoryBean.getScheduler();
        if (scheduler.getJobDetail(jobKey) == null) {
            return -3;
        }
        if (cronExpression == null || cronExpression.equals("")) {
            return -4;
        }
        if (isNormal(cronExpression) == -1) {
            return -5;
        }

        String name = scheduler.getJobDetail(jobKey).getKey().getName();
        String triggerName =name.substring(0, name.lastIndexOf("Detail")) + "CronTrigger";
        //String triggerName = scheduler.getJobDetail(jobKey).getKey().getName().split("Detail")[0] + "CronTrigger";
        //如果存在DEFAULT的触发器则先删除后添加
        for (Trigger trigger : scheduler.getTriggersOfJob(jobKey)) {
            if (trigger.getKey().getName().equals(triggerName)) {
                scheduler.pauseTrigger(trigger.getKey());
                scheduler.unscheduleJob(trigger.getKey());
            }
        }
        CronTrigger trigger = newTrigger().withIdentity(triggerName, TRIGGER_GROUP_NAME)
                .withSchedule(cronSchedule(cronExpression))
                .forJob(jobKey)
                .build();
        scheduler.scheduleJob(trigger);
        return 1;
    }


    /**
     * 删除一个触发器
     * @param triggerName
     * @return 返回删除状态 －1表示删除失败，1表示删除成功
     * @throws SchedulerException
     */
    public Integer deleteTrigger(String triggerName) throws SchedulerException {
        if (StringUtil.isBlank(triggerName)) {
            return -1;
        }
        Scheduler scheduler = mtClusteredSchedulerFactoryBean.getScheduler();

        //删除所有触发器，不删除JobDetail信息
        String jobName = triggerName.split("CronTrigger")[0] + "Detail";
        List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(new JobKey(jobName, "DEFAULT"));
        if (CollectionUtils.isEmpty(triggersOfJob))
            return -1;
        //TODO scheduler.interrupt只能kill本地线程，远程没法kill
        scheduler.interrupt(new JobKey(jobName, "DEFAULT"));
        for (Trigger trigger : triggersOfJob) {
            scheduler.pauseTrigger(trigger.getKey());
            scheduler.unscheduleJob(trigger.getKey());
            // scheduler.deleteJob(jobKey);//并没有终止执行过程，只是这个job以后实效
        }
        return 1;
    }

    public boolean quitJob(String jobName) throws UnableToInterruptJobException {
        Scheduler scheduler = mtClusteredSchedulerFactoryBean.getScheduler();
        JobKey key=new JobKey(jobName, "DEFAULT");
        return scheduler.interrupt(key);
    }

}
