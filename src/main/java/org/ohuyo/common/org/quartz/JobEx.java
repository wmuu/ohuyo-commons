package org.ohuyo.common.org.quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * 
 * @author rabbit
 * 
 */
public abstract class JobEx implements Job {

	private ApplicationContext applicationContext;

	/**
	 * 由子类实现具体的动作
	 * 
	 * @param context
	 * @throws JobExecutionException
	 */
	abstract protected void executeEx(JobExecutionContext context)
			throws JobExecutionException;

	public final void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDataMap map = context.getMergedJobDataMap();
		applicationContext = (ApplicationContext) map
				.get(ApplicationContext.class.getName());
		executeEx(context);
	}

	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
