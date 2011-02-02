package org.ohuyo.common.id;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.ArrayUtils;
import org.ohuyo.common.codec.ByteArrayCodec;

/**
 * 唯一ID生成器,
 * 
 * @对于单机，每毫秒生成ID的数量小于2^31时，保证唯一
 * @对于多机，在IP,类第一次装载时间不同情况下，且满足单机条件时，保证唯一
 * 
 * @根据IP,类第一次装载时间，当前系统时间，计数器,生成唯一ID
 * 
 * @author rabbit
 * 
 */
public class OId {
	private final static long JVM = System.currentTimeMillis();

	private final static byte[] IP = getIP();

	private final static byte[] header = getbuf();

	private final static AtomicInteger count = new AtomicInteger();

	private OId() {
	}

	private static byte[] getbuf() {
		byte[] buf = new byte[IP.length + 8];
		System.arraycopy(IP, 0, buf, 0, IP.length);
		ByteArrayCodec.toBytes(JVM, buf, IP.length);

		char[] s = Hex.encodeHex(buf);
		System.out.println(new String(s));

		s = Hex.encodeHex(IP);
		System.out.println(new String(s));
		return buf;
	}

	static private byte[] getIP() {
		byte[] ip = null;
		InetAddress ia;
		try {
			ia = InetAddress.getLocalHost();
			ip = ia.getAddress();
		} catch (UnknownHostException e) {
			ip = new byte[4];
		}
		return ip;
	}

	public static byte[] gen() {
		byte[] buf = new byte[header.length + 8 + 4];
		System.arraycopy(header, 0, buf, 0, header.length);
		ByteArrayCodec.toBytes(System.currentTimeMillis(), buf, header.length);
		ByteArrayCodec.toBytes(count.getAndIncrement(), buf, header.length + 8);
		return buf;
	}

	public static void main(String[] args) {
		OId id = new OId();
		for (int i = 0; i < 10; i++) {
			test(id);

		}

		Long x = System.currentTimeMillis();
		Long.toHexString(x);
		System.out.println(Long.toHexString(x));
	}

	private static void test(OId id) {
		long t = System.currentTimeMillis();
		byte[] buf = null;
		for (int i = 0; i < 10000000; i++) {
			buf = id.gen();
			ArrayUtils.reverse(buf);
			char[] s = Hex.encodeHex(buf);
			// System.out.println(new String(s));
		}
		long t1 = System.currentTimeMillis();
		System.out.println(t1 - t);

		// System.out.println(Hex.encodeHex(buf));
		// System.out.println(Hex.encodeHex(buf));
	}

}
