package com.cweeyii.quartz.framework;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.List;
import java.util.Properties;


public abstract class AbstractSchedulerFactoryBean extends SchedulerFactoryBean{
	
	protected Properties quartzProperties;
	
	protected abstract List<JobDetail> getJobDetails();
	
	protected abstract List<Trigger> getTriggers();
	
	protected abstract Logger getLogger();
	
	protected abstract boolean isTaskServer();
	
	protected void setProperties(){};
	
	@Override
	public void setQuartzProperties(Properties quartzProperties) {
		this.quartzProperties = quartzProperties;
		super.setQuartzProperties(quartzProperties);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		List<JobDetail> details = getJobDetails();
		if (details != null) {
			setJobDetails(details.toArray(new JobDetail[0]));
		}
		List<Trigger> trrigers = getTriggers();
		if (trrigers != null) {
			setTriggers(trrigers.toArray((new Trigger[0])));
		}		
		setProperties();
		super.setAutoStartup(isTaskServer());
		super.afterPropertiesSet();
		getLogger().info(super.isAutoStartup() + "");
		getLogger().info("注册Job和触发器成功[size=" + details.size() + "]");
	}

}
