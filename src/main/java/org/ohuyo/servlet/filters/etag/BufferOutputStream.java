package org.ohuyo.servlet.filters.etag;

import java.io.ByteArrayOutputStream;

/**
 * 缓冲输出流
 * <p>
 * 提供getByteArray()方法,该方法直接返回{@link java.io.ByteArrayOutputStream
 * ByteArrayOutputStream}内部buf对象，而不是像
 * {@link java.io.ByteArrayOutputStream#toByteArray() toByteArray()}
 * 方法返回一个内部buf的copy
 * 
 * @author rabbit
 * 
 */
public class BufferOutputStream extends ByteArrayOutputStream {
	public BufferOutputStream(int initCapacity) {
		super(initCapacity);
	}

	/**
	 * 直接返回buf对象，不进行复制
	 * 
	 * @see java.io.ByteArrayOutputStream#toByteArray
	 * @return
	 */
	public byte[] getByteArray() {
		return buf;
	}
}
