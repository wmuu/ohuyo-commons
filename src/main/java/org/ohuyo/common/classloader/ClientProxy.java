package org.ohuyo.common.classloader;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.lang.SerializationUtils;

import a.Para;
import a.TestService;
import a.TestServiceImpl;

/**
 * 
 * @author rabbit
 * 
 */
public class ClientProxy {
	Object toProxy;
	Object _proxy;
	InvocationHandler invocationHandler;
	Object toProxy2 = new TestServiceImpl();

	public ClientProxy() throws MalformedURLException, ClassNotFoundException,
			IllegalArgumentException, SecurityException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		URL url = new File("target/classes").toURL();
		URL url2 = new File(
				"C:/Users/rabbit/.m2/repository/commons-lang/commons-lang/2.5/commons-lang-2.5.jar")
				.toURL();
		URL url3 = new File(
				"C:/Users/rabbit/.m2/repository/cglib/cglib/2.2/cglib-2.2.jar")
				.toURL();
		URL url4 = new File(
				"C:/Users/rabbit/.m2/repository/asm/asm/3.1/asm-3.1.jar")
				.toURL();

		URLClassLoader ucl = new URLClassLoader(new URL[] { url, url2, url3,
				url4 }, Thread.currentThread().getContextClassLoader()
				.getParent());
		Class<?> clz = ucl.loadClass("a.TestServiceImpl");
		toProxy = clz.getConstructor(null).newInstance(null);
		Class<?> clz2 = ucl.loadClass("a.Proxy");
		_proxy = clz2.getConstructor(null).newInstance(null);
		final Method m2 = _proxy.getClass().getMethod("getMethod1",
				new Class[] { Object.class, Method.class, Object[].class });
		invocationHandler = new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
//				return method.invoke(toProxy2, args);
				 Class<? extends Object> clz = toProxy.getClass();
				 byte[] b = (byte[]) m2.invoke(_proxy, new Object[] { toProxy,
				 method, args });
				 return SerializationUtils.deserialize(b);
			}
		};
	}

	public static void main(String[] args) throws MalformedURLException,
			IllegalArgumentException, SecurityException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		ClientProxy cp = new ClientProxy();
		TestService ts = (TestService) Proxy.newProxyInstance(ClassLoader
				.getSystemClassLoader(), new Class[] { TestService.class },
				cp.invocationHandler);
		Para p = new Para();
		p.i = 2;
		p.l = 3;
		p.str = "fuck";
		long t = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			Para x = ts.doSomething(p);
		}
		long t1 = System.currentTimeMillis();
		System.out.println("use time=" + (t1 - t));

		t = System.currentTimeMillis();
		for (int i = 0; i < 10000; i++) {
			Para x = ts.doSomething(p);
		}
		t1 = System.currentTimeMillis();
		System.out.println("use time=" + (t1 - t));

//		t = System.currentTimeMillis();
//		p.buf = new byte[1000];
//		for (int i = 0; i < 100000; i++) {
//			SerializationUtils.clone(p);
//		}
//		t1 = System.currentTimeMillis();
//		System.out.println("use time=" + (t1 - t));
//
//		t = System.currentTimeMillis();
//		p.buf = new byte[1000];
//		for (int i = 0; i < 1000000; i++) {
//			SerializationUtils.clone(p);
//		}
//		t1 = System.currentTimeMillis();
//		System.out.println("use time=" + (t1 - t));
	}
}
