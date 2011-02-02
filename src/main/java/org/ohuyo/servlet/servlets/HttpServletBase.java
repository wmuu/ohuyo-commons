package org.ohuyo.servlet.servlets;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.ohuyo.servlet.filters.expire.IntrospectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpServlet基础抽象类
 * 
 * @author rabbit
 * 
 */
public abstract class HttpServletBase extends HttpServlet {
	private Logger log = LoggerFactory.getLogger(HttpServletBase.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = -1249486260569707871L;

	/**
	 * 显示属性
	 * 
	 * @param req
	 */
	protected void showAttributes(HttpServletRequest req) {
		Enumeration<?> list = req.getAttributeNames();
		for (; list.hasMoreElements();) {
			String key = (String) list.nextElement();
			System.out.println(key + " = " + req.getAttribute(key));
		}
	}

	/**
	 * 显示变量
	 * 
	 * @param req
	 */
	protected void showParameters(HttpServletRequest req) {
		Enumeration<?> list = req.getParameterNames();
		for (; list.hasMoreElements();) {
			String key = (String) list.nextElement();
			System.out.println(key + " = " + req.getParameter(key));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(ServletConfig config) throws ServletException {
		Enumeration<String> paramNames = config.getInitParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			if (!IntrospectionUtils.setProperty(this, paramName, config
					.getInitParameter(paramName))) {
				if (log.isWarnEnabled()) {
					log.warn(this.getClass().getName() + " no Such Property",
							paramName);
				}
			}
		}
		super.init(config);
	}
}
