package org.ohuyo.lpc.classloader;

/**
 * classloader工厂接口
 * 
 * @author rabbit
 * 
 */
public interface ClassLoaderFactory {
	/**
	 * 获取classLoader
	 * 
	 * @return
	 */
	ClassLoader getClassLoader();
}
