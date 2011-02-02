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
 * etag�������� ��С��ָ����󻺳�ֵ��http��������etag,����������ύ��etag���жԱ� �����ͬ������304
 * <p>
 * ��ʼ������
 * <p>
 * <li>algorithm ǩ���㷨,֧��jdk֧�ֵ�����ǩ���㷨������ֵMD5 or SHA-1��</li>
 * <li>bufInitCapacity ��������ʼ��С������ֵ16k</li>
 * <li>bufMaxCapacity ��󻺳��С������ֵ128k</li>
 * <p>
 * ע��
 * <li>�����ṩ��outputStream��writer��flush����Ϊ�ղ��������ʺϷ������Ҫֱ��ˢ��������ĳ���</li>
 * <li>
 * ������https��ʱ�򣬼�ʹ������������etag���������ie8,opera,safari�����Ҳ���ᷢ��etag��if-modifyͷ��������</li>
 * <li>������apache��Ϊǰ��ʱ�������zipѹ����apache���Ὣj2ee������etagͷ���͸��ͻ���</li>
 * 
 * @author rabbit
 * 
 */
public class ETagFilter extends HttpFilterBase {
	/**
	 * init param<br/>
	 * MD5 or SHA-1<br/>
	 * ����ֵMD5
	 */
	private String algorithm = "MD5";
	/**
	 * ��ʼ����������С<br/>
	 * ����ֵ16k
	 */
	private String bufInitCapacity = "16k";
	/**
	 * ��󻺳�ֵ<br/>
	 * ��д���ֽ������ڸ�ֵʱ��������etag<br/>
	 * ����ֵ128k
	 */
	private String bufMaxCapacity = "128k";

	/**
	 * 1KB����
	 */
	private final static int KB = 1024;
	/**
	 * 1MB����
	 */
	private final static int MB = KB * KB;
	private Logger log = LoggerFactory.getLogger(ETagFilter.class);
	private int iBufInitCapacity;
	private int iBufMaxCapacity;
	/**
	 * ��Ϣǩ������
	 */
	private MessageDigestFactory messageDigestFactory;
	/**
	 * ����������
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
		// ��printWriter������д��etagStream
		etagWrapper.flushBuffer();
		// �Ƿ��Ѿ��ύ
		if (etagWrapper.isCommitted()) {
			log.debug(request.getServletPath() + " had Committed with no ETag");
			etagStream.send();// ����ʣ�������
			return;
		}
		// ״̬�Ƿ���200����0(��δ����)
		int status = etagWrapper.getStatus();
		if (status != 0 && status != HttpServletResponse.SC_OK) {
			log.debug(request.getServletPath() + " status is "
					+ etagWrapper.getStatus() + ", not ETag");
			etagStream.send();// ����ʣ�������
			return;
		}
		// ����etag
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
			etagStream.send();// ����ʣ�������
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
	 * ���㻺������ʼ����С
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
