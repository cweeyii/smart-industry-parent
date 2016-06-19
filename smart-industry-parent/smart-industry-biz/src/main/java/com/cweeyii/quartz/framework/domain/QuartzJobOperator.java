package com.cweeyii.quartz.framework.domain;


import com.cweeyii.quartz.framework.dao.mapper.CronTriggerMapper;
import com.cweeyii.quartz.framework.dao.mapper.QRTZJobResultMapper;
import com.cweeyii.quartz.framework.dao.mapper.QrtzJobDetailsMapper;
import com.cweeyii.quartz.framework.manger.QuartzTaskManager;
import com.cweeyii.quartz.framework.vo.CronTriggerVo;
import com.cweeyii.quartz.framework.vo.JobInfo;
import com.cweeyii.util.StringUtil;
import org.quartz.SchedulerException;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuartzJobOperator {
    @Resource
    private CronTriggerMapper cronTriggerMapper;
    @Resource
    private QrtzJobDetailsMapper qrtzJobDetailsMapper;
    @Resource
    private QRTZJobResultMapper qRTZJobResultMapper;
    @Resource
    private QuartzTaskManager quartzTaskManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzJobOperator.class);



    /**
     * 得到Job信息
     * @param searchPara 空查询全部；不为空，过滤job
     * @return 返回job信息列表
     */
    public List<JobInfo> search(String searchPara) {
      List<CronTriggerVo> CronTriggerList = cronTriggerMapper.getAllCronTriggers();
      Map<String, String> triggerMap = new HashMap<>(100);
      for (CronTriggerVo cronTriggerVo : CronTriggerList) {
          triggerMap.put(cronTriggerVo.getTriggerName(), cronTriggerVo.getCronExpression());
      }

      List<JobInfo> jobInfoList = new ArrayList<>(50);
      for (QrtzJobDetails job : qrtzJobDetailsMapper.getAllJobDetails()) {
          //不包含搜索信息
          if (!StringUtil.isBlank(searchPara)&& !job.getJobName().toLowerCase().contains(searchPara.toLowerCase())
                  && !job.getDescription().contains(searchPara))
              continue;
          //BeanName不是JobName
          String triggerName = job.getJobName().substring(0,job.getJobName().lastIndexOf("Detail")) + "CronTrigger";
          String jobName = job.getJobName();
          String description = job.getDescription();

          String cronEx = triggerMap.get(triggerName);
          String triggerState;
          int resultStates = -1;
          List<QRTZJobResult> qRTZJobResultList = qRTZJobResultMapper.findLatestResultsByJobName(jobName, 1);
          if (!CollectionUtils.isEmpty(qRTZJobResultList))
              resultStates = qRTZJobResultList.get(0).getResult();
          triggerState = parserResult2String(resultStates);
          JobInfo jobInfo = new JobInfo(jobName, description, triggerName, "", triggerState, null, null, null, cronEx);
          jobInfoList.add(jobInfo);

      }
      return jobInfoList;
    }
    
    /**
     * 任务结果表里的Result字段转化成String
     * @param resultStates int
     * @return 将整形转化成String
     */
    private String parserResult2String(int resultStates) {
        String triggerState;

        if (resultStates == 1) {
            triggerState = "成功";
        } else if (resultStates == 2) {
            triggerState = "失败";
        } else if (resultStates == 3) {
            triggerState = "被中断";
        } else if (resultStates == 0) {
            triggerState = "运行中";
        } else {
            triggerState = "";
        }

        return triggerState;
    }
    
    /**
     * 得到一个job的最近七条执行日志
     * @param jobName 任务名称
     * @return 任务执行情况列表
     */
    public List<QRTZJobResult> getTaskRunningLogsByJobName(String jobName) {
        return qRTZJobResultMapper.findLatestResultsByJobName(jobName, 7);
    }
    
	/**
	 * 立即执行job
	 * @param jobName 任务名称：带Detail的那个
	 * @param params 任务参数：json格式
	 * */
    public int startJob(String jobName, String params) throws SchedulerException {
        Map<String,String> args = new HashMap<>();
        args.put("param", params);
        return quartzTaskManager.startJob(jobName,args);
    }
    
	/**
	 * 删除cron
	 * @param triggerName 触发器名称
	 * */
    public int deleteTrigger(String triggerName) throws SchedulerException {
        return quartzTaskManager.deleteTrigger(triggerName);
    }
    
    /**
	 * 删除cron
	 * @param jobName 触发器名称
	 * */
    public int saveOrupdateTrigger(String jobName,String cronEx) throws SchedulerException {
        return quartzTaskManager.addTrigger(jobName, cronEx);
    }
    
    /**
     * 中断一个运行job
     * @return 返回状态，－1表示没有这个job，1表示成功
     */
    public boolean quitJob(String jobName) throws UnableToInterruptJobException {
        return quartzTaskManager.quitJob(jobName);
    }
	

}
