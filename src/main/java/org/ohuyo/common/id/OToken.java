package org.ohuyo.common.id;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.apache.commons.codec.binary.Hex;

/**
 * 
 * @author rabbit
 * 
 */
public class OToken implements Token {

	public byte[] genTokenBytes() throws IllegalBlockSizeException,
			BadPaddingException {
		return OId.gen();
	}

	public String genTokenString() throws IllegalBlockSizeException,
			BadPaddingException {
		return Hex.encodeHexString(genTokenBytes());
	}

}
