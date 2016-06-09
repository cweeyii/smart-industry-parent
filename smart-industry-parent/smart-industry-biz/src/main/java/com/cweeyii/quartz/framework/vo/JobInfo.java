package com.cweeyii.quartz.framework.vo;

import java.util.Date;


public class JobInfo {
     private String jobName;
     private String description;
     private String triggerName;
     private String triggerState;
     private String jobRunTime;
     private Date triggerStartTime;
     private Date triggerEndTime;
     private Date   triggerNextFireTime;
     private String jobGroup;
     private String cronEx;
     private String beanName;

    public JobInfo(String jobName, String de, String triggerName, String beanName, String triggerState, String jobRunTime, Date triggerStartTime, Date triggerEndTime, String cronEx)
    {
        this.jobName=jobName;
        this.description=de;
        this.triggerName=triggerName;
        this.beanName=beanName;
        this.triggerState=triggerState;
        this.jobRunTime=jobRunTime;
        this.triggerStartTime=triggerStartTime;
        this.triggerEndTime=triggerEndTime;
        this.cronEx=cronEx;
    }
    public String getJobName()
    {
        return jobName;
    }
    public String getDescription()
    {
        return description;
    }
    public String getTriggerState()
    {
        return triggerState;
    }
    public Date getTriggerStartTime()
    {
        return triggerStartTime;
    }
    public Date getTriggerEndTime()
    {
        return triggerEndTime;
    }
    public Date getTriggerNextFireTime()
    {
        return triggerNextFireTime;
    }
    public String getJobGroup()
    {
        return jobGroup;
    }
    public String getTriggerName()
    {
        return triggerName;
    }
    public String getJobRunTime()
    {
        return jobRunTime;
    }
    public void setJobName(String jobName)
    {
      this.jobName=jobName;
    }
    public void  setDescription(String description)
    {
        this.description=description;
    }
    public void setTriggerName(String triggerName)
    {
        this.triggerName=triggerName;
    }
    public void setTriggerState(String triggerState)
    {
        this.triggerState=triggerState;
    }
    public void setTriggerStartTime(Date startTime)
    {
        this.triggerStartTime=startTime;
    }
    public void setTriggerNextFireTime(Date triggerNextFireTime)
    {
        this.triggerNextFireTime=triggerNextFireTime;
    }
    public void setTriggerEndTime(Date triggerEndTime)
    {
        this.triggerEndTime=triggerEndTime;
    }
    public void setJobGroup(String jobGroup )
    {
        this.jobGroup=jobGroup;
    }


    public String getCronEx() {
        return cronEx;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
