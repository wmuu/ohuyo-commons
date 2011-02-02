package org.ohuyo.common.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.ohuyo.common.id.OId;

/**
 * 采用AES加密方式加密数组<br/>
 * 对有生成规律的OID进行加密，使 加密后的内容难以预测<br/>
 * 为了保证无法用字典法破解，加密的数据长度应该大于256bit<br/>
 * 且1到n位为随机数，n+1到n+256位或者更长为需要加密的数据
 * 
 * @author rabbit
 * 
 */
public class AesCipher {
	private String key;
	private final static String ALGORITHM = "AES";
	private String iv;
	private Cipher encryptCipher;
	private String transformation;
	private Cipher decryptCipher;
	private static final String DEFAULT_TRANSFORMATION = "AES/CFB/NoPadding";

	public AesCipher() throws NoSuchAlgorithmException, DecoderException,
			InvalidKeyException, NoSuchPaddingException,
			InvalidAlgorithmParameterException {
		this(256);
	}

	public AesCipher(int keySize) throws NoSuchAlgorithmException,
			DecoderException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException {
		KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
		kgen.init(keySize); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		key = Hex.encodeHexString(raw);
		encryptCipher = Cipher.getInstance(DEFAULT_TRANSFORMATION);
		encryptCipher.init(Cipher.ENCRYPT_MODE, skey);
		raw = encryptCipher.getIV();
		IvParameterSpec ips = null;
		if (raw != null) {
			iv = Hex.encodeHexString(encryptCipher.getIV());
			ips = new IvParameterSpec(raw);
		}
		decryptCipher = Cipher.getInstance("AES/CBC/NoPadding");
		decryptCipher.init(Cipher.DECRYPT_MODE, skey, ips);
	}

	public AesCipher(String keyStr, String ivStr, String transformation)
			throws DecoderException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException {
		byte[] iv = Hex.decodeHex(ivStr.toCharArray());
		byte[] key = Hex.decodeHex(keyStr.toCharArray());
		this.transformation = transformation;
		SecretKeySpec skeySpec = new SecretKeySpec(key, ALGORITHM);
		IvParameterSpec ips = new IvParameterSpec(iv);
		encryptCipher = Cipher.getInstance(transformation);
		encryptCipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
	}

	public byte[] encrypt(byte[] buf) throws IllegalBlockSizeException,
			BadPaddingException {
		return encryptCipher.doFinal(buf);
	}

	public static void main(String[] args) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException, DecoderException,
			IllegalBlockSizeException, BadPaddingException,
			InvalidAlgorithmParameterException {
		AesCipher id = new AesCipher(256);
		byte[] raw = id.encrypt("fuck youfuck youfuck youfuck you".getBytes());
		System.out.println(Hex.encodeHex(raw));
		raw = id.encrypt("fuck youfuck you".getBytes());
		System.out.println(Hex.encodeHex(raw));
		raw = id.encrypt("fuck youfuck youfuck youfuck you".getBytes());
		System.out.println(Hex.encodeHex(raw));
		raw = id.encrypt("fuck youfuck yo".getBytes());
		System.out.println(Hex.encodeHex(raw));
		for (int i = 0; i < 1000; i++) {
			raw = id.encrypt("fuck youfuck youfuck youfuck you".getBytes());
		}
		for (int j = 0; j < 100; j++) {

			long t0 = System.currentTimeMillis();
			for (int i = 0; i < 1000; i++) {
				raw = id.encrypt(OId.gen());
			}
			long t = System.currentTimeMillis();
			System.out.println(t - t0);
		}
		raw = OId.gen();
		System.out.println(Hex.encodeHex(raw));
		// ArrayUtils.reverse(raw);
		raw = id.encrypt(raw);
		System.out.println(Hex.encodeHex(raw));
		raw = OId.gen();
		System.out.println(Hex.encodeHex(raw));
		// ArrayUtils.reverse(raw);
		raw = id.encrypt(raw);
		System.out.println(Hex.encodeHex(raw));
		raw = OId.gen();
		System.out.println(Hex.encodeHex(raw));
		// ArrayUtils.reverse(raw);
		raw = id.encrypt(raw);
		System.out.println(Hex.encodeHex(raw));
	}
}
