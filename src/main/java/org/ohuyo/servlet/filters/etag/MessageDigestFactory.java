package org.ohuyo.servlet.filters.etag;

import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * �̰߳�ȫ��ϢժҪ�㷨������
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
	 *            �㷨
	 * @throws NoSuchAlgorithmException
	 */
	public MessageDigestFactory(String algorithm)
			throws NoSuchAlgorithmException {
		// ��ʼ��ʱǿ�Ʋ����Ƿ�֧�ָ��㷨
		MessageDigest.getInstance(algorithm);
		this.algorithm = algorithm;
	}

	/**
	 * ÿ���߳��ṩ��������ϢժҪ�����
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