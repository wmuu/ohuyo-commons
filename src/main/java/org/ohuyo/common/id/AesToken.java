package org.ohuyo.common.id;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.ArrayUtils;
import org.ohuyo.common.crypto.AesCipher;

/**
 * ����AES���ܷ�ʽ�������ɹ��ɵ�OID���м��ܺ�����token�����ɵ�tokenֵ����Ԥ��
 * 
 * @author rabbit
 * 
 */
public class AesToken implements Token {
	private AesCipher aesCipher;

	public AesToken() throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			DecoderException {
		aesCipher = new AesCipher();
	}

	public AesToken(int keySize) throws InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, DecoderException {
		aesCipher = new AesCipher(keySize);
	}

	public AesToken(String keyStr, String ivStr, String transformation)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			DecoderException {
		aesCipher = new AesCipher(keyStr, ivStr, transformation);
	}

	public String genTokenString() throws IllegalBlockSizeException,
			BadPaddingException {
		return Hex.encodeHexString(genTokenBytes());
	}

	public byte[] genTokenBytes() throws IllegalBlockSizeException,
			BadPaddingException {
		byte[] raw = OId.gen();
		// ��OID�����׷����仯�Ĳ��ַ�����ǰ�棬��֤ÿ�����ɵ�toaken���켫��
		ArrayUtils.reverse(raw);
		return aesCipher.encrypt(raw);
	}

}
