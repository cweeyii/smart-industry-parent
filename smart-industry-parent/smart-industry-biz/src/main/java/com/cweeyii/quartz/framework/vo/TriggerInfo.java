package com.cweeyii.quartz.framework.vo;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TriggerInfo {

    private String jobName;
    private String cronEx;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCronEx() {
        return cronEx;
    }

    public void setCronEx(String cronEx) {
        this.cronEx = cronEx;
    }
}