package org.ohuyo.servlet.filters.etag;

import java.io.ByteArrayOutputStream;

/**
 * ���������
 * <p>
 * �ṩgetByteArray()����,�÷���ֱ�ӷ���{@link java.io.ByteArrayOutputStream
 * ByteArrayOutputStream}�ڲ�buf���󣬶�������
 * {@link java.io.ByteArrayOutputStream#toByteArray() toByteArray()}
 * ��������һ���ڲ�buf��copy
 * 
 * @author rabbit
 * 
 */
public class BufferOutputStream extends ByteArrayOutputStream {
	public BufferOutputStream(int initCapacity) {
		super(initCapacity);
	}

	/**
	 * ֱ�ӷ���buf���󣬲����и���
	 * 
	 * @see java.io.ByteArrayOutputStream#toByteArray
	 * @return
	 */
	public byte[] getByteArray() {
		return buf;
	}
}
