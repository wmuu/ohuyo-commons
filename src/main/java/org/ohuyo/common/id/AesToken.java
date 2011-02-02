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
 * 采用AES加密方式对有生成规律的OID进行加密后生成token，生成的token值不可预测
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
		// 将OID最容易发生变化的部分放在最前面，保证每次生成的toaken差异极大
		ArrayUtils.reverse(raw);
		return aesCipher.encrypt(raw);
	}

}
