package org.ohuyo.lpc.proxy;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.FactoryBean;

public class LpcProxyFactoryBean implements FactoryBean, BeanClassLoaderAware {
	private Class serviceInterface;
	private ClassLoader beanClassLoader;

	public Object getObject() throws Exception {

		// TODO Auto-generated method stub
		return null;
	}

	public Class getObjectType() {
		return serviceInterface;
	}

	public boolean isSingleton() {
		return true;
	}

	public void setBeanClassLoader(ClassLoader classLoader) {
		// TODO Auto-generated method stub

	}
}
