package org.ohuyo.common.codec;

/**
 * java基本类型和byte[]数组之间互相转换
 * @author rabbit
 *
 */
public class ByteArrayCodec {

	public final static byte[] toBytes(short val) {
		return toBytes(val, new byte[2], 0);
	}

	public final static byte[] toBytes(short val, byte[] buf) {
		return toBytes(val, buf, 0);
	}

	public final static byte[] toBytes(short val, byte[] buf, int offset) {
		return toBytes(val, buf, offset, 2);
	}

	public final static byte[] toBytes(int val) {
		return toBytes(val, new byte[4], 0);
	}

	public final static byte[] toBytes(int val, byte[] buf) {
		return toBytes(val, buf, 0);
	}

	public final static byte[] toBytes(int val, byte[] buf, int offset) {
		return toBytes(val, buf, offset, 4);
	}

	public final static byte[] toBytes(long val) {
		return toBytes(val, new byte[8], 0);
	}

	public final static byte[] toBytes(long val, byte[] buf) {
		return toBytes(val, buf, 0);
	}

	public final static byte[] toBytes(long val, byte[] buf, int offset) {
		return toBytes(val, buf, offset, 8);
	}

	// ------------------------------
	public final static byte[] toBytes(short val, byte[] buf, int offset,
			int leng) {
		return toBytes(val | 0x0, buf, offset, leng);
	}

	public final static byte[] toBytes(int val, byte[] buf, int offset, int len) {
		int i = Integer.SIZE - 8;
		int j = offset + len;
		while (offset < j) {
			buf[offset++] = (byte) ((val >>> i) | 0x00);
			i -= 8;
		}
		return buf;
	}

	public final static byte[] toBytes(long val, byte[] buf, int offset, int len) {
		int i = Long.SIZE - 8;
		int j = offset + len;
		while (offset < j) {
			buf[offset++] = (byte) ((val >>> i) | 0x00);
			i -= 8;
		}
		return buf;
	}
}
