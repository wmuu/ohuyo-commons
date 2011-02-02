package org.ohuyo.common.classloader;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.RMISecurityManager;

import org.apache.commons.lang.SerializationUtils;

import a.LoaderSample1;
import a.Para;

public class Test {
	public static void main(String[] args) throws MalformedURLException,
			ClassNotFoundException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
		Class c;
		ClassLoader cl;
		cl = ClassLoader.getSystemClassLoader();
		System.out.println(cl);
		while (cl != null) {
			cl = cl.getParent();
			System.out.println(cl);
		}
		try {
			c = Class.forName("java.lang.Object");
			cl = c.getClassLoader();
			System.out.println(" java.lang.Object's loader is  " + cl);
			c = Class.forName("org.ohuyo.common.classloader.Test");
			cl = c.getClassLoader();
			System.out.println(" LoaderSample1's loader is  " + cl);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SecurityManager sm = System.getSecurityManager();
		System.out.println(sm);
		RMISecurityManager x;
		cl = Thread.currentThread().getContextClassLoader();
		System.out.println(cl);
		cl = cl.getParent();
		System.out.println(cl);
		System.out.println(cl.getParent());
		URL url = new File("target.classes").toURL();
		URL url2 = new File(
				"C:/Users/rabbit/.m2/repository/commons-lang/commons-lang/2.5/commons-lang-2.5.jar")
				.toURL();

		URLClassLoader ucl = new URLClassLoader(new URL[] { url, url2 }, Thread
				.currentThread().getContextClassLoader().getParent());
		Class<?> clz = ucl.loadClass("a.LoaderSample1");
		Object obj = clz.getConstructor(null).newInstance(null);
		LoaderSample1 ls = new LoaderSample1();
		System.out.println(clz.hashCode());
		System.out.println(a.LoaderSample1.class.hashCode());
		// ------------------------
		clz = ucl.loadClass("a.TestService");
		Class<?> clz2 = ucl.loadClass("a.Para");
		obj = clz.getConstructor(null).newInstance(null);
		Method[] ms = clz.getMethods();
		for (int i = 0; i < ms.length; i++) {
			System.out.println(ms[i]);
			System.out.println(ms[i].getName());
		}
		System.out.println("getClassLoader=" + Thread.class.getClassLoader());
		Method m = clz.getMethod("doSomething", clz2);
		// Method m = clz.getMethod("doSomething", Para.class);
		Method m2 = clz.getMethod("doSomething", byte[].class);
		Para p = new Para();
		byte[] buf = SerializationUtils.serialize(p);
		Thread.currentThread().setContextClassLoader(ucl);
		Para p3 = (Para) SerializationUtils.deserialize(buf);
		Para p4 = (Para) m2.invoke(obj, buf);
		Para p2 = (Para) m.invoke(obj, p);

	}
}
