package org.ohuyo.servlet.servlets.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.ohuyo.servlet.servlets.HttpServletBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 静态资源处理
 * 
 * @author rabbit
 * 
 */
public class StaticResourceServlet extends HttpServletBase {
	private Logger log = LoggerFactory.getLogger(StaticResourceServlet.class);
	/**
	 * 静态资源文件路径,可以用绝对路径，或者相对路径
	 */
	private String resourcePath;

	/**
	 * 映射的url
	 */
	private String mappingUrl;

	/**
	 * 
	 */
	private static final long serialVersionUID = -6635476626943577084L;

	/**
	 * 
	 */
	private MimeType mimeType;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			mimeType = new MimeType();
		} catch (IOException e) {
			throw new ServletException(e);
		}
		if (StringUtils.isEmpty(getResourcePath())) {
			throw new IllegalArgumentException("resourcePath = " + getResourcePath());
		}
		if (StringUtils.isEmpty(getMappingUrl())) {
			throw new IllegalArgumentException("mappingUrl = " + getMappingUrl());
		}
		log.info("StaticResourceServlet inited.\nresourcePath=" + getResourcePath()
				+ "\nmappingUrl=" + getMappingUrl());
	}

	/**
	 * 只支持get
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		mimeType(req, resp);
		fill(resp, getRequestPath(req));
	}

	/**
	 * 重写service方法
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		showAttributes(req);
		System.out.println("--------------------------------------");
		showParameters(req);
		File file = new File(getRequestPath(req));
		// 判断文件是否存在
		if (file.exists() && file.isFile()) {
			super.service(req, resp);
		} else {
			resp.sendError(500);
		}
	}

	/**
	 * 取得请求资源的mime type
	 * 
	 * @param req
	 * @param resp
	 */
	private void mimeType(HttpServletRequest req, HttpServletResponse resp) {
		String x = req.getPathInfo();
		int index = x.lastIndexOf('.');
		String extension = null;
		if (index != -1 && index != x.length()) {
			extension = x.substring(index + 1, x.length());
		}
		String type = mimeType.mimeByExtension(extension);
		resp.setContentType(type);
		this.getLastModified(req);
	}

	/**
	 * 根据请求和映射关系，取得实际的文件路径
	 * 
	 * @param req
	 * @return
	 */
	private String getRequestPath(HttpServletRequest req) {
		String pathInfo = req.getPathInfo();
		return pathInfo.replace(getMappingUrl(), getResourcePath());
	}

	/**
	 * 将资源写入输出流
	 * 
	 * @param resp
	 * @param fileName
	 * @throws IOException
	 */
	private void fill(HttpServletResponse resp, String fileName)
			throws IOException {
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		try {
			ServletOutputStream os = resp.getOutputStream();
			int i = IOUtils.copy(fis, os);
			if (i != -1) {
				resp.setContentLength(i);
			}
		} finally {
			IOUtils.closeQuietly(fis);
		}
	}

	/**
	 * 取得更新时间
	 */
	@Override
	protected long getLastModified(HttpServletRequest req) {
		String fileName = getRequestPath(req);
		File file = new File(fileName);
		return file.lastModified();
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}

	public String getResourcePath() {
		return resourcePath;
	}

	public void setMappingUrl(String mappingUrl) {
		this.mappingUrl = mappingUrl;
	}

	public String getMappingUrl() {
		return mappingUrl;
	}

}
