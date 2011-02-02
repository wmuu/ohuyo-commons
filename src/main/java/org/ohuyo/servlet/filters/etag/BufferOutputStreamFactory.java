package org.ohuyo.servlet.filters.etag;

import java.lang.ref.SoftReference;

/**
 * 缓冲输出流工厂
 * 
 * @author rabbit
 * 
 */
public class BufferOutputStreamFactory {
	private final ThreadLocal<SoftReference<BufferOutputStream>> threadLocal = new ThreadLocal<SoftReference<BufferOutputStream>>();
	private final int initCapacity;
	private final static int KB = 1024;
	private final static int MB = KB * KB;

	public BufferOutputStreamFactory(int initCapacity) {
		if (initCapacity < KB) {
			throw new IllegalArgumentException(
					"initCapacity must large 1024 byte.");
		}
		if (initCapacity > MB * 2) {
			throw new IllegalArgumentException(
					"initCapacity must less 2M byte.");
		}
		this.initCapacity = initCapacity;
	}

	public BufferOutputStream get() {
		SoftReference<BufferOutputStream> sr = threadLocal.get();
		BufferOutputStream os = null;
		if (sr != null && (os = sr.get()) != null) {
			//清空流
			os.reset();
			return os;
		}
		os = new BufferOutputStream(initCapacity);
		sr = new SoftReference<BufferOutputStream>(os);
		threadLocal.set(sr);
		return os;
	}
}