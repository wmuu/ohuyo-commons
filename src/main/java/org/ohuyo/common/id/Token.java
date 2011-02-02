package org.ohuyo.common.id;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

/**
 * 
 * @author rabbit
 * 
 */
public interface Token {
	byte[] genTokenBytes() throws IllegalBlockSizeException, BadPaddingException;

	String genTokenString() throws IllegalBlockSizeException, BadPaddingException;
}
