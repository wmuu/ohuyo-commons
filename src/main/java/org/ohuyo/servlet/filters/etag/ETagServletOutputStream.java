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
	 * �������е������Ƿ��Ѿ������͵�Դ�������
	 */
	private boolean isSend = false;
	/**
	 * ��󻺳�����
	 */
	private final int maxCapacity;
	/**
	 * Դ�����
	 */
	private final ServletOutputStream originalStream;
	/**
	 * ���������
	 */
	private final BufferOutputStream bufStream;
	/**
	 * ժҪ��
	 */
	private final MessageDigest messageDigest;

	/**
	 * �ж������ֽڸ����Ƿ���Ի���
	 * 
	 * @param len
	 * @return
	 */
	private boolean isBufAble(int len) {
		// �ѷ���
		if (isSend) {
			return false;
		}
		// ��������
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
	 * �����������д��Դ�����
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
