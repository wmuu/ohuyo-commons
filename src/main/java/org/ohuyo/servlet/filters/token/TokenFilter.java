package org.ohuyo.servlet.filters.token;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionListener;

import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;
import org.ohuyo.common.id.AesToken;
import org.ohuyo.servlet.filters.HttpFilterBase;
import org.ohuyo.servlet.filters.etag.ETagFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Token过滤器
 * 
 * @规则1 每次请求需要带token_old,返回新token_new.
 * @规则2 如果使用了token_new验证成功.那么token_old将失效.
 * @规则3 每次请求都生成token_new.
 * @规则4 如果请求使用的token匹配token_old或者token_new，验证成功。否则验证失败，本session作废。
 * 
 * 
 * @author rabbit
 * 
 */
public class TokenFilter extends HttpFilterBase {
	private Logger log = LoggerFactory.getLogger(TokenFilter.class);
	private final static String TOKEN_INTEM_NAME = TokenItem.class
			.getCanonicalName();
	private final static String TOKEN_NAME = TokenFilter.class
			.getCanonicalName()
			+ ".token";
	private AesToken aesToken;

	public static class TokenItem implements Serializable {
		private static final long serialVersionUID = -2773918995105006567L;
		private String preToken;
		private String token;
	}

	private String genToken() throws ServletException {
		try {
			return aesToken.genTokenString();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			aesToken = new AesToken();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	HttpSessionListener x;

	@Override
	public void doHttpFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpSession session = request.getSession(true);
		TokenItem token = null;
		if (session.isNew()) {
			token = createTokenItem();
		} else {
			token = (TokenItem) session.getAttribute(TOKEN_INTEM_NAME);
			Cookie[] cookies = request.getCookies();

			if (!verifyToken(cookies, token)) {
				// session.invalidate();
				// request.getSession();
				token = createTokenItem();
			}
		}
		Cookie cookie = new Cookie(TOKEN_NAME, token.token);
		response.addCookie(cookie);
		session.setAttribute(TOKEN_INTEM_NAME, token);
		chain.doFilter(request, response);
	}

	private TokenItem createTokenItem() throws ServletException {
		TokenItem item = null;
		item = new TokenItem();
		item.token = genToken();
		return item;
	}

	private boolean verifyToken(final Cookie[] cookies, TokenItem item)
			throws ServletException {
		if (cookies == null) {
			return false;
		}
		if (item == null) {
			return false;
		}
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals(TOKEN_NAME)) {
				String val = cookie.getValue();
				log.debug("old toke=" + val);
				if (val == null) {
					continue;
				}
				if (val.equals(item.token)) {
					item.preToken = item.token;
					item.token = genToken();
					return true;
				}
				if (val.equals(item.preToken)) {
					item.token = genToken();
					return true;
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {
		Properties props = new Properties();
		props.setProperty("separator", "/");
		IdentifierGenerator gen = new UUIDHexGenerator();
		// ( (Configurable) gen ).configure(Hibernate.STRING, props, null);
		IdentifierGenerator gen2 = new UUIDHexGenerator();
		// ( (Configurable) gen2 ).configure(Hibernate.STRING, props, null);

		for (int i = 0; i < 10; i++) {
			String id = (String) gen.generate(null, null);
			System.out.println(id);
			String id2 = (String) gen2.generate(null, null);
			System.out.println(id2);
		}

	}
}
