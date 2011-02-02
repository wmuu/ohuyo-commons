package com.cj.etag;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ETagResponseWrapper extends HttpServletResponseWrapper
{
  private HttpServletResponse response = null;
  private ServletOutputStream stream = null;
  private PrintWriter writer = null;
  private OutputStream buffer = null;
  private int contentLength;
  private String contentType;

  public ETagResponseWrapper(HttpServletResponse paramHttpServletResponse, OutputStream paramOutputStream)
  {
    super(paramHttpServletResponse);
    this.response = paramHttpServletResponse;
    this.buffer = paramOutputStream;
  }

  public ServletOutputStream getOutputStream()
    throws IOException
  {
    if (this.stream == null)
      this.stream = new ETagResponseStream(this.buffer);
    return this.stream;
  }

  public PrintWriter getWriter()
    throws IOException
  {
    if (this.writer == null)
      this.writer = new PrintWriter(new OutputStreamWriter(getOutputStream(), "UTF-8"));
    return this.writer;
  }

  public void flushBuffer()
    throws IOException
  {
    this.stream.flush();
  }

  public void setContentLength(int paramInt)
  {
    this.contentLength = paramInt;
    super.setContentLength(paramInt);
  }

  public int getContentLength()
  {
    return this.contentLength;
  }

  public void setContentType(String paramString)
  {
    this.contentType = paramString;
    super.setContentType(paramString);
  }

  public String getContentType()
  {
    return this.contentType;
  }
}