package com.cweeyii.quartz.framework.vo;

import org.quartz.*;

import java.util.Date;
import java.util.Map;

/**
 * @author clive
 * @version 1.0
 * @date 15/8/16
 * @description
 */
public class ArgsJobExecutionContext implements JobExecutionContext {
    @Override
    public Scheduler getScheduler() {
        return null;
    }

    @Override
    public Trigger getTrigger() {
        return null;
    }

    @Override
    public Calendar getCalendar() {
        return null;
    }

    @Override
    public boolean isRecovering() {
        return false;
    }

    @Override
    public TriggerKey getRecoveringTriggerKey() throws IllegalStateException {
        return null;
    }

    @Override
    public int getRefireCount() {
        return 0;
    }

    @Override
    public JobDataMap getMergedJobDataMap() {
        return null;
    }

    @Override
    public JobDetail getJobDetail() {
        return null;
    }

    @Override
    public Job getJobInstance() {
        return null;
    }

    @Override
    public Date getFireTime() {
        return null;
    }

    @Override
    public Date getScheduledFireTime() {
        return null;
    }

    @Override
    public Date getPreviousFireTime() {
        return null;
    }

    @Override
    public Date getNextFireTime() {
        return null;
    }

    @Override
    public String getFireInstanceId() {
        return null;
    }

    @Override
    public Object getResult() {
        return null;
    }

    @Override
    public void setResult(Object result) {

    }

    @Override
    public long getJobRunTime() {
        return 0;
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public Object get(Object key) {
        return argMaps == null ? null : argMaps.get(key);
    }

    private Map<String,String> argMaps = null;

    public ArgsJobExecutionContext(Map<String, String> argMaps){
        this.argMaps = argMaps;
    }
}
