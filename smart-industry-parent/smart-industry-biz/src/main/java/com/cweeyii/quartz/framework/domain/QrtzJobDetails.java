package com.cweeyii.quartz.framework.domain;

/**
 * Created by wenyi on 16/2/27.
 * Email:caowenyi@meituan.com
 */

public class QrtzJobDetails {
	
    private  String jobName;
    
    private  String jobGroup;
    
    private  String description;
    
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }


    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
