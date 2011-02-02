package org.ohuyo.servlet.filters.etag;

import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 线程安全信息摘要算法工厂类
 * 
 * @author rabbit
 * 
 */
public class MessageDigestFactory {
	private final ThreadLocal<SoftReference<MessageDigest>> threadLocal = new ThreadLocal<SoftReference<MessageDigest>>();
	private final String algorithm;

	/**
	 * 
	 * @param algorithm
	 *            算法
	 * @throws NoSuchAlgorithmException
	 */
	public MessageDigestFactory(String algorithm)
			throws NoSuchAlgorithmException {
		// 初始化时强制测试是否支持该算法
		MessageDigest.getInstance(algorithm);
		this.algorithm = algorithm;
	}

	/**
	 * 每个线程提供独立的信息摘要类对象
	 * 
	 * @return
	 */
	public MessageDigest getInstance() {
		try {
			SoftReference<MessageDigest> sr = threadLocal.get();
			MessageDigest md = null;
			if (sr != null && (md = sr.get()) != null) {
				md.reset();
				return md;
			}
			md = MessageDigest.getInstance(MessageDigestFactory.this.algorithm);
			sr = new SoftReference<MessageDigest>(md);
			threadLocal.set(sr);
			return md;
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("could not happen.", e);
		}
	}
}