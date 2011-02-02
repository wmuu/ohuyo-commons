package org.ohuyo.servlet.filters.token;

import java.util.Set;

public class Token {
	/**
	 * 当前的使用的
	 */
	String token;

	/**
	 * 未使用过的token集合
	 */
	Set<String> tokenSet;

	public Token() {

	}

}
