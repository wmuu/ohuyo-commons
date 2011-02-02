package org.ohuyo.servlet.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Http������������
 * 
 * @author rabbit
 * 
 */
public abstract class HttpFilterBase extends FilterBase {
	/**
	 * ����HttpServletRequest
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	public abstract void doHttpFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException;

	/**
	 * �����Http����
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	public void doOriginalFilter(ServletRequest request,
			ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		chain.doFilter(request, response);
	}

	/**
	 * ���շ���
	 */
	final public void doFilter(ServletRequest request,
			ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		if ((request instanceof HttpServletRequest)
				&& (response instanceof HttpServletResponse)) {
			doHttpFilter((HttpServletRequest) request,
					(HttpServletResponse) response, chain);
		} else {
			doOriginalFilter(request, response, chain);
		}
	}
}
