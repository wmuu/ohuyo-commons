package org.ohuyo.lpc.proxy;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;

import a.TestService;

public class Test {
	public static void main(String[] args) throws Exception {
		URL url = new File("target/classes").toURL();
		URL url2 = new File(
				"C:/Users/rabbit/.m2/repository/commons-lang/commons-lang/2.5/commons-lang-2.5.jar")
				.toURL();
		URLClassLoader ucl = new URLClassLoader(new URL[] { url, url2 }, Thread
				.currentThread().getContextClassLoader().getParent());
		Class<?> clz = ucl.loadClass("a.TestServiceImpl");
		Class<?> clz2 = ucl.loadClass("org.ohuyo.lpc.proxy.InvokedProxy");
		Constructor<?> c = clz2.getConstructor(null);
		Object obj = c.newInstance(null);
		InvokeProxy proxy = new InvokeProxy();
		proxy.interfaze = TestService.class;
		proxy.invokedProxy = obj;
		proxy.afterPropertiesSet();
		TestService ts = (TestService) proxy.getObject();
		long t = System.currentTimeMillis();
		byte[] buf = new byte[4048];
		int m = 100000;
		for (int i = 0; i < m; i++) {
			ts.doSomething2(buf);
		}
		long t1 = System.currentTimeMillis() - t;
		System.out.println(t1);
		System.out.println((double) m*1000 / t1);
		System.out.println(clz.hashCode());
		System.out.println(clz2.hashCode());
		System.out.println(a.TestServiceImpl.class.hashCode());
		System.out.println(org.ohuyo.lpc.proxy.InvokedProxy.class.hashCode());
	}
}
