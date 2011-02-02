package org.ohuyo.servlet.filters.etag;

import java.io.IOException;
import java.security.MessageDigest;

import javax.servlet.ServletOutputStream;

import org.apache.commons.codec.binary.Hex;
import org.ohuyo.common.exception.ArgumentCheckUtils;

/**
 * etag ServletOutputStream
 * 
 * @author rabbit
 * 
 */
public class ETagServletOutputStream extends ServletOutputStream {

	/**
	 * 缓存流中的数据是否已经被发送到源输出流中
	 */
	private boolean isSend = false;
	/**
	 * 最大缓冲容量
	 */
	private final int maxCapacity;
	/**
	 * 源输出流
	 */
	private final ServletOutputStream originalStream;
	/**
	 * 缓存输出流
	 */
	private final BufferOutputStream bufStream;
	/**
	 * 摘要类
	 */
	private final MessageDigest messageDigest;

	/**
	 * 判断输入字节个数是否可以缓存
	 * 
	 * @param len
	 * @return
	 */
	private boolean isBufAble(int len) {
		// 已发送
		if (isSend) {
			return false;
		}
		// 超过容量
		long l = (long) bufStream.size() + len;
		return l <= maxCapacity;
	}

	public ETagServletOutputStream(ServletOutputStream originalStream,
			BufferOutputStream bufStream, MessageDigest messageDigest,
			int maxCapacity) throws IOException {
		super();
		ArgumentCheckUtils.notNull(bufStream, "bufStream");
		this.bufStream = bufStream;
		ArgumentCheckUtils.notNull(messageDigest, "messageDigest");
		this.messageDigest = messageDigest;
		ArgumentCheckUtils.notNull(originalStream, "originalStream");
		this.originalStream = originalStream;
		ArgumentCheckUtils.less(maxCapacity, 1024 * 1024 * 2, "maxCapacity");
		ArgumentCheckUtils.greater(maxCapacity, 0, "maxCapacity");
		this.maxCapacity = maxCapacity;
	}

	public String getETag() {
		byte[] buf = bufStream.getByteArray();
		int len = bufStream.size();
		messageDigest.update(buf, 0, len);
		buf = messageDigest.digest();
		char[] c = Hex.encodeHex(buf);
		if (c == null) {
			return null;
		}
		return new String(c);
	}

	public void close() throws IOException {
		// NOOP
		// if (!closed) {
		// send();
		// }
		// originalStream.close();
		// closed = true;
	}

	/**
	 * 将缓存的内容写到源输出流
	 * 
	 * @throws IOException
	 */
	public void send() throws IOException {
		if (!isSend) {
			bufStream.writeTo(originalStream);
			bufStream.reset();
			isSend = true;
		}
	}

	public boolean isSend() {
		return isSend;
	}

	/**
	 * 
	 */
	public void flush() throws IOException {
		// NOOP
	}

	public void write(int b) throws IOException {
		if (isBufAble(1)) {
			bufStream.write((byte) b);
		} else {
			send();
			originalStream.write((byte) b);
		}
	}

	public void write(byte b[], int off, int len) throws IOException {
		if (isBufAble(len)) {
			bufStream.write(b, off, len);
		} else {
			send();
			originalStream.write(b, off, len);
		}
	}

	public void write(byte b[]) throws IOException {
		write(b, 0, b.length);
	}

}
