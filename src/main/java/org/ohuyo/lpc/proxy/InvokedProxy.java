package org.ohuyo.lpc.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.ohuyo.common.classloader.SerializationUtils;

import a.TestServiceImpl;

/**
 * 被调用者代理(非主classloader)
 * 
 * @author rabbit
 * 
 */
public class InvokedProxy {

	/**
	 * 实际调用的对象
	 */
	private Object src = new TestServiceImpl();

	/**
	 * 方法cache
	 */
	private final Map<Method, Method> cacheMap = new HashMap<Method, Method>();

	public InvokedProxy() {
	}

	public InvokedProxy(Object src) {
		setSrc(src);
	}

	public void setSrc(Object src) {
		this.src = src;
	}

	public byte[] Invoke(Method method, Object[] args)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Method m = cacheMap.get(method);
		if (m == null) {
			Class<? extends Object> clz = src.getClass();
			Class<?>[] clazzes = (Class<?>[]) SerializationUtils.clone(method
					.getParameterTypes());
			m = clz.getMethod(method.getName(), clazzes);
			cacheMap.put(method, m);
		}
		args = (Object[]) SerializationUtils.clone(args);
		Object obj = m.invoke(src, args);
		return SerializationUtils.serialize((Serializable) obj);
	}

}
