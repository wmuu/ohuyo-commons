package org.springframework.samples.petclinic.web;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ETagResponseStream extends ServletOutputStream {
	private boolean closed = false;
	private OutputStream stream = null;

	public ETagResponseStream(HttpServletResponse response, OutputStream stream) throws IOException {
		super();
		this.stream = stream;
	}

	public void close() throws IOException {
		if (!closed) {
			stream.close();
			closed = true;
		}
	}

	public void flush() throws IOException {
		if (!closed) {
			stream.flush();
		}
	}

	public void write(int b) throws IOException {
		if (!closed) {
			stream.write((byte) b);
		}
		throw new RuntimeException("fuck jetty");
	}

	public void write(byte b[], int off, int len) throws IOException {
		if (!closed) {
			stream.write(b, off, len);
		}
	}

	public void write(byte b[]) throws IOException {
		write(b, 0, b.length);
	}

	public boolean closed() {
		return closed;
	}

	public void reset() {
		//noop
	}
}
