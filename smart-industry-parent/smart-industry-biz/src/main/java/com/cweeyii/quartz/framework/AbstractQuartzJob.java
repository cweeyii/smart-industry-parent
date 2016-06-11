package com.cweeyii.quartz.framework;


import com.cweeyii.quartz.framework.dao.mapper.QRTZJobResultMapper;
import com.cweeyii.quartz.framework.domain.QRTZJobResult;
import com.cweeyii.quartz.framework.vo.ArgsJobExecutionContext;
import com.cweeyii.util.*;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wenyi on 16/2/27.
 * Email:caowenyi@meituan.com
 */

public abstract class AbstractQuartzJob implements Job, Serializable, BeanNameAware, InitializingBean, DisposableBean, ApplicationContextAware, InterruptableJob {

    protected ApplicationContext applicationContext;

    private String beanName;

    private JobDetail jobDetail;

    private volatile Thread thisThread;

    private volatile boolean interruptFlg = false;

    private QRTZJobResult result = null;

    @Resource
    private QRTZJobResultMapper qRTZJobResultMapper;

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractQuartzJob.class);

    //
    //private static final Integer[] MDC_RD_IDS = new Integer[]{21273,29290,31802,37775,29908,38999,32495};

    //fancong 2015/04/14 add
    public String getBeanName() {
        return beanName;
    }
    //end add
    /**
     * 持久化任务必须要序列化
     */
    private static final long serialVersionUID = 1L;

    //定义抽象方法，供子类实现
    public abstract void action(JobExecutionContext context) throws Exception;

    protected abstract String getDescription();

    protected void afterJobInitialized() {
        //do nothing defaut
    }

    /**
     * 是否记录日志
     */
    protected boolean logToDB() {
        return true;
    }

    /**
     * 获取quartz传递的参数
     *
     * @return
     */
    protected String getParam(JobExecutionContext context) {
        if (context == null) {
            return null;
        }
        if (context instanceof ArgsJobExecutionContext) {
            return (String) context.get("param");
        }
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        return jobDataMap == null ? null : jobDataMap.getString("param");
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        thisThread = Thread.currentThread();
        if (context != null && !(context instanceof ArgsJobExecutionContext)) {
            QRTZJobResult result = new QRTZJobResult();
            this.result = result;
            JobDetail jobDetail = context.getJobDetail();
            String jobName = jobDetail.getKey().getName();
            AbstractQuartzJob job = (AbstractQuartzJob) SpringBeanUtil.getBean(jobDetail.getJobClass());
            try {
                String hostName = ServerUtil.getHostName();
                long start = System.currentTimeMillis();
                Trigger trigger = context.getTrigger();
                result.setStartTime(start);
                result.setTriggerName(trigger.getKey().getName());
                result.setJobName(jobName);
                result.setHostName(hostName);
                PoiUtil.setDefault(result);
                LOGGER.info("定时任务启动[" + result + "]");
                if (logToDB()) {
                    //得到最近的执行日志，如果是0，怎么则变成3
                    this.qRTZJobResultMapper = job.getqRTZJobResultMapper();
                    List<QRTZJobResult> lastestResults = job.getqRTZJobResultMapper().findLatestResultsByJobName(jobName, 1);
                    if (!CollectionUtils.isEmpty(lastestResults)) {
                        QRTZJobResult lastestResult = lastestResults.get(0);
                        if (lastestResult.getResult().intValue() == 0) {
                            lastestResult.setResult(3);
                            lastestResult.setEndTime(System.currentTimeMillis());
                            job.getqRTZJobResultMapper().update(lastestResult);
                        }
                    }
                    job.getqRTZJobResultMapper().insert(result);
                }
                job.action(context);
                long end = System.currentTimeMillis();
                result.setEndTime(end);
                StringBuilder buffer = new StringBuilder();
                buffer.append(jobDetail.getDescription() + "(" + jobDetail.getKey() + ")").append("执行完成 , 耗时: ").append((end - start)).append(" ms");
                LOGGER.info(buffer.toString());
                result.setResult(1);
            } catch (Exception e) {
                result.setEndTime(System.currentTimeMillis());
                if (interruptFlg) {
                    result.setResult(3);
                    LOGGER.error("定时任务人为中断[result = " + result + "]", e);
                } else {
                    result.setResult(2);
                    LOGGER.error("定时任务异常中断[result = " + result + "]", e);
                    doResolveException(e, context);
                }
            } finally {
                try {
                    PoiUtil.setDefault(result);
                    if (logToDB()) {
                        job.getqRTZJobResultMapper().update(result);
                        //QRTZJobResult i=job.getqRTZJobResultMapper().select();
                        // List<QRTZJobResult> res=job.getqRTZJobResultMapper().select();
                        //System.out.print(res.get(0).getId());
                    } else {
                        LOGGER.info(result.toString());
                    }
                } catch (Exception e) {
                    LOGGER.error("定时任务出现异常[result = " + result + "]", e);
                }
            }
        } else {
            try {
                this.action(context);
            } catch (Exception e) {
                //发送邮件
                doResolveException(e, context);
                LOGGER.error("定时任务出现异常", e);
            }
        }
    }

    private void doResolveException(Throwable e, JobExecutionContext context) {
        //线下不发送报警
        String environment = System.getProperty("environment");
        if (!"online".equals(environment)) {
            LOGGER.error("线下环境不发送报警[environment=" + environment + "]");
            return;
        }
        try {
            String jobName = this.getClass().getSimpleName();
            String stackTrace = ExceptionUtils.getStackTrace(e);
            Map<String, Object> data = new HashMap<>();
            data.put("jobName", jobName);
            data.put("stackTrace", stackTrace);
            data.put("time", DateUtil.Date2StringSec(new Date()));
            data.put("hostname", ServerUtil.getHostName());
            data.put("message", e.getMessage());
            if (!(context instanceof ArgsJobExecutionContext) && context != null) {
                JobDetail jobDetail = context.getJobDetail();
                Trigger trigger = context.getTrigger();
                data.put("description", jobDetail.getDescription());
                data.put("jobDetail", jobDetail.getKey().toString());
                data.put("trigger", trigger.getKey().toString());
            }
            LOGGER.error(data.toString());
        } catch (Exception e2) {
            LOGGER.error("发送定时任务失败通知失败", e2);
        }


    }

    private QRTZJobResultMapper getqRTZJobResultMapper() {
        return qRTZJobResultMapper;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtil.isBlank(beanName)) {
            throw new IllegalArgumentException("beanName不能为空");
        }
        String description = getDescription();
        if (StringUtil.isBlank(description)) {
            throw new IllegalArgumentException("[class=" + this.getClass().getName() + "]description不能为空");
            //LOGGER.error("["+this.getClass().getSimpleName()+"]没有定义description");
        }
        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        String jobDetailName = beanName + "Detail";
        JobDetailFactoryBean jobFactoryBean = new JobDetailFactoryBean();
        jobFactoryBean.setName(jobDetailName);
        jobFactoryBean.setDescription(description);
        jobFactoryBean.setDurability(true);
        jobFactoryBean.setRequestsRecovery(true);
        jobFactoryBean.setJobClass(this.getClass());
        autowireCapableBeanFactory.autowireBean(jobFactoryBean);
        autowireCapableBeanFactory.initializeBean(jobFactoryBean, jobDetailName + "Factory");
        jobDetail = jobFactoryBean.getObject();
        afterJobInitialized();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;

    }

//	@Override
//	 protected  void execute() throws Throwable {
//		action(null);
//	 }

    public JobDetail getJobDetail() {
        return jobDetail;
    }

    @Override
    public void destroy() throws Exception {
        stopThread();
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        stopThread();
        if (thisThread != null && result != null) {
            result.setEndTime(System.currentTimeMillis());
            result.setResult(3);
            qRTZJobResultMapper.update(result);
            String jobName = result.getJobName();
            result = null;
        }
    }

    private void stopThread() {
        if (thisThread != null) {
            interruptFlg = true;
            thisThread.stop();
        }
    }

}

