package org.ohuyo.servlet.filters.etag;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ohuyo.servlet.filters.HttpFilterBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * etag过滤器。 对小于指定最大缓冲值的http内容生成etag,并和浏览器提交的etag进行对比 如果相同，返回304
 * <p>
 * 初始化参数
 * <p>
 * <li>algorithm 签名算法,支持jdk支持的所有签名算法，建议值MD5 or SHA-1。</li>
 * <li>bufInitCapacity 缓冲区初始大小，建议值16k</li>
 * <li>bufMaxCapacity 最大缓冲大小，建议值128k</li>
 * <p>
 * 注意
 * <li>该类提供的outputStream和writer的flush操作为空操作，不适合服务端需要直接刷新输出流的场合</li>
 * <li>
 * 当采用https的时候，即使服务器发送了etag给浏览器，ie8,opera,safari浏览器也不会发送etag的if-modify头给服务器</li>
 * <li>当采用apache作为前端时候，如果打开zip压缩，apache不会将j2ee容器的etag头发送给客户端</li>
 * 
 * @author rabbit
 * 
 */
public class ETagFilter extends HttpFilterBase {
	/**
	 * init param<br/>
	 * MD5 or SHA-1<br/>
	 * 建议值MD5
	 */
	private String algorithm = "MD5";
	/**
	 * 初始化缓存区大小<br/>
	 * 建议值16k
	 */
	private String bufInitCapacity = "16k";
	/**
	 * 最大缓冲值<br/>
	 * 当写入字节数大于改值时，不生成etag<br/>
	 * 建议值128k
	 */
	private String bufMaxCapacity = "128k";

	/**
	 * 1KB定义
	 */
	private final static int KB = 1024;
	/**
	 * 1MB定义
	 */
	private final static int MB = KB * KB;
	private Logger log = LoggerFactory.getLogger(ETagFilter.class);
	private int iBufInitCapacity;
	private int iBufMaxCapacity;
	/**
	 * 信息签名工厂
	 */
	private MessageDigestFactory messageDigestFactory;
	/**
	 * 缓存流工厂
	 */
	private BufferOutputStreamFactory byteArrayOutputStreamFactory;

	/**
	 * 
	 */

	public void destroy() {
		// NOOP
	}

	@Override
	public void doHttpFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		MessageDigest md = messageDigestFactory.getInstance();
		ETagServletOutputStream etagStream = new ETagServletOutputStream(
				response.getOutputStream(), byteArrayOutputStreamFactory.get(),
				md, iBufMaxCapacity);
		ETagResponseWrapper etagWrapper = new ETagResponseWrapper(response,
				etagStream);
		chain.doFilter(request, etagWrapper);
		// 将printWriter的内容写入etagStream
		etagWrapper.flushBuffer();
		// 是否已经提交
		if (etagWrapper.isCommitted()) {
			log.debug(request.getServletPath() + " had Committed with no ETag");
			etagStream.send();// 发送剩余的内容
			return;
		}
		// 状态是否是200或者0(还未设置)
		int status = etagWrapper.getStatus();
		if (status != 0 && status != HttpServletResponse.SC_OK) {
			log.debug(request.getServletPath() + " status is "
					+ etagWrapper.getStatus() + ", not ETag");
			etagStream.send();// 发送剩余的内容
			return;
		}
		// 处理etag
		String etag = '"' + etagStream.getETag() + '"';
		response.setHeader("ETag", etag);
		String oldEtag = request.getHeader("If-None-Match");
		if ((oldEtag != null) && (oldEtag.equals(etag))) {
			log.debug(request.getServletPath() + " ETag is same. ETag="
					+ oldEtag);
			response.sendError(304);
			response.setHeader("ETag", etag);
		} else {
			log.debug(request.getServletPath() + " ETag is not same. old="
					+ oldEtag + ", new=" + etag);
			etagStream.send();// 发送剩余的内容
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		algorithm = getPara("algorithm", filterConfig);
		bufInitCapacity = getPara("bufInitCapacity", filterConfig);
		bufMaxCapacity = getPara("bufMaxCapacity", filterConfig);

		try {
			messageDigestFactory = new MessageDigestFactory(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("no such algorithm \""
					+ algorithm + "\"", e);
		}

		iBufInitCapacity = bufSize(bufInitCapacity);
		iBufMaxCapacity = bufSize(bufMaxCapacity);

		byteArrayOutputStreamFactory = new BufferOutputStreamFactory(
				iBufInitCapacity);
		log.info("ETagFilter inited.\niBufInitCapacity=" + iBufInitCapacity
				+ "\niBufMaxCapacity=" + iBufMaxCapacity);
	}

	/**
	 * 计算缓冲区初始化大小
	 * 
	 * @param s
	 * @return
	 */
	private int bufSize(String s) {
		int n = 1;
		s = s.toLowerCase();
		if (s.endsWith("k")) {
			s = s.substring(0, s.length() - 1);
			n = KB;
		} else if (s.endsWith("m")) {
			s = s.substring(0, s.length() - 1);
			n = MB;
		}
		return Integer.parseInt(s) * n;
	}

}
