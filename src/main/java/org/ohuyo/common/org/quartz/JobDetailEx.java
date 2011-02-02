package org.ohuyo.common.org.quartz;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.ApplicationContext;

/**
 * 
 * @author rabbit
 * 
 */
public class JobDetailEx extends JobDetail {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3405948240802070540L;
	/**
	 * 
	 */
	private ApplicationContext applicationContext;

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public JobDataMap getJobDataMap() {
		JobDataMap map = super.getJobDataMap();
		map.put(ApplicationContext.class.getName(), applicationContext);
		return map;
	}
}
