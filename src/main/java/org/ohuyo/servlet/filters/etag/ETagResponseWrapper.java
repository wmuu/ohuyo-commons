package org.ohuyo.servlet.filters.etag;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 
 * @author rabbit
 * 
 */
public class ETagResponseWrapper extends HttpServletResponseWrapper {
	private final ETagServletOutputStream stream;
	private PrintWriter writer = null;
	private final static String DEFAULT_CHARACTER_ENCODING = "UTF-8";
	private int status;

	@Override
	public void setStatus(int sc) {
		status = sc;
		super.setStatus(sc);
	}

	@Override
	public void setStatus(int sc, String sm) {
		status = sc;
		super.setStatus(sc, sm);
	}

	/**
	 * 重写isCommintted,防止struts2关闭输出流
	 */
	@Override
	public boolean isCommitted() {
		return stream.isSend();
	}

	public int getStatus() {
		return status;
	}

	public ETagResponseWrapper(HttpServletResponse response,
			ETagServletOutputStream stream) {
		super(response);
		this.stream = stream;
	}

	public ServletOutputStream getOutputStream() throws IOException {
		return stream;
	}

	public PrintWriter getWriter() throws IOException {
		if (writer == null) {
			String ce = this.getCharacterEncoding();
			if (ce == null) {
				ce = DEFAULT_CHARACTER_ENCODING;
				this.setCharacterEncoding(ce);
			}
			writer = new PrintWriter(new OutputStreamWriter(stream, ce));
		}
		return writer;
	}

	public void flushBuffer() throws IOException {
		if (writer != null) {
			writer.flush();
		}
		stream.flush();
	}
}
