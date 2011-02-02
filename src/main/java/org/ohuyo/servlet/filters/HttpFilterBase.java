package org.ohuyo.servlet.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Http过滤器抽象类
 * 
 * @author rabbit
 * 
 */
public abstract class HttpFilterBase extends FilterBase {
	/**
	 * 处理HttpServletRequest
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
	 * 处理非Http请求
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
	 * 最终方法
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
