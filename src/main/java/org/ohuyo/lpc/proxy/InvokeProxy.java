package org.ohuyo.lpc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 
 * @author rabbit
 * 
 */
public class InvokeProxy implements FactoryBean, InitializingBean {

	/**
	 * 
	 */
	Object invokedProxy;
	/**
	 * 
	 */
	InvocationHandler invocationHandler;
	/**
	 * 代理
	 */
	Object proxy;
	/**
	 * 接口
	 */
	Class<?> interfaze;

	public InvokeProxy() {
	}

	public InvokeProxy(Object src, Object invokedProxy) {
	}

	public void afterPropertiesSet() throws Exception {
		invocationHandler = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				final Method m2 = invokedProxy.getClass().getMethod("Invoke",
						new Class[] { Method.class, Object[].class });
				byte[] b = (byte[]) m2.invoke(invokedProxy, new Object[] {
						method, args });
				return SerializationUtils.deserialize(b);
			}
		};
		proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(),
				new Class[] { interfaze }, invocationHandler);
	}

	public Object getObject() throws Exception {
		return proxy;
	}

	public Class getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}

}
