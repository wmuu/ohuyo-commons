package org.ohuyo.lpc.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import org.ohuyo.common.classloader.SerializationUtils;

/**
 * 代理类-负责2个classloader直接对象的转换
 * 
 * @author rabbit
 * 
 */
public class Proxy {

	private Map<Method, Method> cacheMap = new HashMap<Method, Method>();
	private Map<Method, FastMethod> cacheMap2 = new HashMap<Method, FastMethod>();

	public byte[] getMethod1(Object proxy, Method method, Object[] args)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Method m = cacheMap.get(method);
		if (m == null) {
			Class<? extends Object> clz = proxy.getClass();
			Class<?>[] clazzes = (Class<?>[]) SerializationUtils.clone(method
					.getParameterTypes());
			m = clz.getMethod(method.getName(), clazzes);
			cacheMap.put(method, m);
		}
		args = (Object[]) SerializationUtils.clone(args);
		return SerializationUtils.serialize((Serializable) m
				.invoke(proxy, args));
	}

	public byte[] getMethod(Object proxy, Method method, Object[] args)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Class<? extends Object> clz = proxy.getClass();
		FastMethod m = cacheMap2.get(method);

		FastClass fc = FastClass.create(clz);
		if (m == null) {
			Class<?>[] clazzes = (Class<?>[]) SerializationUtils.clone(method
					.getParameterTypes());
			m = fc.getMethod(method.getName(), clazzes);
			cacheMap2.put(method, m);
		}
		args = (Object[]) SerializationUtils.clone(args);
		return SerializationUtils.serialize((Serializable) m
				.invoke(proxy, args));
	}

	class TestMethodInterceptorImpl implements MethodInterceptor {
		public Object intercept(Object obj, Method method, Object[] args,
				MethodProxy proxy) throws Throwable {
			return proxy.invokeSuper(obj, args);
		}
	}

}
