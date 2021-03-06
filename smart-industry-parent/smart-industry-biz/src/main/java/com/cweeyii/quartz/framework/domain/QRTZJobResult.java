package com.cweeyii.quartz.framework.domain;

import java.util.Date;

/**
 * Created by wenyi on 16/2/27.
 * Email:caowenyi@meituan.com
 */
public class QRTZJobResult {

    private Long id;

    private String jobName;

    private String triggerName;

    private Date startTime;

    private Date endTime;

    //0执行中 1成功 2异常 3被中断
    private Integer result;

    private String hostName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[jobName="+jobName+"]"
                + "[triggerName="+triggerName+"]"
                + "[startTime="+startTime+"]"
                + "[endTime="+endTime+"]"
                + "[hostName="+hostName+"]");
        return sb.toString();
    }



}

