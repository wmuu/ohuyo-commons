package org.ohuyo.lpc.context;

import java.lang.reflect.Constructor;

import org.ohuyo.lpc.classloader.ClassLoaderFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;

/**
 * 
 * @author rabbit
 * 
 */
public class LpcApplicationContextFactory implements ApplicationContextFactory,
		InitializingBean {

	private ApplicationContext applicationContext;

	private String configLocation;

	private ClassLoaderFactory classLoaderFactory;

	private Object proxy;

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void afterPropertiesSet() throws Exception {
		ClassLoader cl = classLoaderFactory.getClassLoader();
		Class<?> clz = cl
				.loadClass("org.springframework.context.support.ClassPathXmlApplicationContext");
		Constructor<?> c = clz.getConstructor(new Class<?>[] { String.class });
		proxy = c.newInstance(new Object[] { configLocation });

	}
}
