package com.cweeyii.quartz.framework;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;
import javax.annotation.Resource;

import com.cweeyii.quartz.framework.dao.mapper.QRTZJobResultMapper;
import com.cweeyii.quartz.framework.domain.QRTZJobResult;
import com.cweeyii.util.DateUtil;
import com.cweeyii.util.EnterpriseUtil;
import com.cweeyii.util.SpringBeanUtil;
import com.cweeyii.util.StringUtil;
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


@DisallowConcurrentExecution//加这个参数主要是同一时间只运行同一个任务
public abstract class AbstractQuartzJob implements Job, Serializable,BeanNameAware, InitializingBean,DisposableBean,ApplicationContextAware,InterruptableJob{

    protected ApplicationContext applicationContext;

    private String beanName;

    private JobDetail jobDetail;

    private volatile Thread thisThread;

    private volatile boolean interruptFlg = false;

    private QRTZJobResult result = null;

    @Resource
    private QRTZJobResultMapper qRTZJobResultMapper;

    private  Logger logger = LoggerFactory.getLogger(this.getClass());

    //
    //private static final Integer[] MDC_RD_IDS = new Integer[]{21273,29290,31802,37775,29908,38999,32495};

    //fancong 2015/04/14 add
    public String getBeanName(){
        return beanName;
    }
    //end add
    /**
     * 持久化任务必须要序列化
     */
    private static final long serialVersionUID = 1L;

    //定义抽象方法，供子类实现
    protected abstract void action(JobExecutionContext context) throws Exception;

    protected abstract String getDescription();


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        thisThread = Thread.currentThread();
        if (context != null) {
            QRTZJobResult result = new QRTZJobResult();
            this.result = result;
            JobDetail jobDetail = context.getJobDetail();
            String jobName = jobDetail.getKey().getName();
            AbstractQuartzJob job = (AbstractQuartzJob) SpringBeanUtil.getBean(jobDetail.getJobClass());
            try {
                String hostName = InetAddress.getLocalHost().getHostName();
                long start = System.currentTimeMillis();
                Trigger trigger = context.getTrigger();
                result.setStartTime(DateUtil.nowDateTime());
                result.setTriggerName(trigger.getKey().getName());
                result.setJobName(jobName);
                result.setHostName(hostName);
                EnterpriseUtil.setDefault(result);
                logger.info("定时任务启动[" + result + "]");

                //得到最近的执行日志，如果是0，怎么则变成3
                this.qRTZJobResultMapper = job.getqRTZJobResultMapper();
                List<QRTZJobResult> lastestResults = job.getqRTZJobResultMapper().findLatestResultsByJobName(jobName,1);
                if (!CollectionUtils.isEmpty(lastestResults)) {
                    QRTZJobResult lastestResult = lastestResults.get(0);
                    if (lastestResult.getResult()== 0) {
                        lastestResult.setResult(3);
                        lastestResult.setEndTime(DateUtil.nowDateTime());
                        job.getqRTZJobResultMapper().update(lastestResult);
                    }
                }
                job.getqRTZJobResultMapper().insert(result);
                job.action(context);
                long end = System.currentTimeMillis();
                result.setEndTime(DateUtil.nowDateTime());
                StringBuilder buffer = new StringBuilder();
                buffer.append(jobDetail.getDescription()).append("(").append(jobDetail.getKey()).append(")").append("执行完成 , 耗时: ").append((end - start)).append(" ms");
                logger.info(buffer.toString());
                result.setResult(1);
            } catch (Exception e) {
                result.setEndTime(DateUtil.nowDateTime());
                if (interruptFlg) {
                    result.setResult(3);
                    logger.error("定时任务人为中断[result = " + result + "]",e);
                } else {
                    result.setResult(2);
                    logger.error("定时任务异常中断[result = " + result + "]", e);
                }
            } finally{
                try {
                    EnterpriseUtil.setDefault(result);
                    job.getqRTZJobResultMapper().update(result);
                } catch(Exception e) {
                    logger.error("定时任务出现异常[result = " + result + "]",e);
                }
            }
        } else {
            try {
                this.action(context);
            } catch (Exception e) {
                //发送邮件
                logger.error("定时任务出现异常", e);
            }
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
            //logger.error("["+this.getClass().getSimpleName()+"]没有定义description");
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
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;

    }

    public JobDetail getJobDetail(){
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
            result.setEndTime(DateUtil.nowDateTime());
            result.setResult(3);
            qRTZJobResultMapper.update(result);
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
