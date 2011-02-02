package com.cj.etag;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ETagFilter
  implements Filter
{
  private FilterConfig config;
  private boolean no_init = true;
  private String excluded;
  private static final String EXCLUDE = "exclude";

  public void init(FilterConfig paramFilterConfig)
    throws ServletException
  {
    this.no_init = false;
    this.config = paramFilterConfig;
    if ((this.excluded = paramFilterConfig.getInitParameter("exclude")) != null)
      this.excluded += ",";
  }

  public void destroy()
  {
    this.config = null;
  }

  public FilterConfig getFilterConfig()
  {
    return this.config;
  }

  public void setFilterConfig(FilterConfig paramFilterConfig)
  {
    if (this.no_init)
    {
      this.no_init = false;
      this.config = paramFilterConfig;
      if ((this.excluded = paramFilterConfig.getInitParameter("exclude")) != null)
        this.excluded += ",";
    }
  }

  public void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse, FilterChain paramFilterChain)
    throws IOException, ServletException
  {
    HttpServletRequest localHttpServletRequest = (HttpServletRequest)paramServletRequest;
    HttpServletResponse localHttpServletResponse = (HttpServletResponse)paramServletResponse;
    String str1 = localHttpServletRequest.getRequestURI();
    if (excluded(str1))
    {
      paramFilterChain.doFilter(paramServletRequest, paramServletResponse);
    }
    else
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      ETagResponseWrapper localETagResponseWrapper = new ETagResponseWrapper(localHttpServletResponse, localByteArrayOutputStream);
      paramFilterChain.doFilter(localHttpServletRequest, localETagResponseWrapper);
      byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
      if (arrayOfByte == null)
        return;
      if (arrayOfByte.length == 0)
        return;
      String str2 = localETagResponseWrapper.getContentType();
      String str3 = '"' + getMd5Digest(arrayOfByte) + '"';
      localHttpServletResponse.setHeader("ETag", str3);
      String str4 = localHttpServletRequest.getHeader("If-None-Match");
      if ((str4 != null) && (str4.equals(str3)))
      {
        localHttpServletResponse.sendError(304);
        localHttpServletResponse.setHeader("Last-Modified", localHttpServletRequest.getHeader("If-Modified-Since"));
      }
      else
      {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.set(14, 0);
        Date localDate = localCalendar.getTime();
        localHttpServletResponse.setDateHeader("Last-Modified", localDate.getTime());
        if (str2 != null)
          localHttpServletResponse.setContentType(str2);
        localHttpServletResponse.setContentLength(arrayOfByte.length);
        ServletOutputStream localServletOutputStream = localHttpServletResponse.getOutputStream();
        localServletOutputStream.write(arrayOfByte);
        localServletOutputStream.flush();
        localServletOutputStream.close();
      }
    }
  }

  private boolean excluded(String paramString)
  {
    if ((paramString == null) || (this.excluded == null))
      return false;
    return (this.excluded.indexOf(paramString + ",") >= 0);
  }

  private String getMd5Digest(byte[] paramArrayOfByte)
  {
    MessageDigest localMessageDigest;
    try
    {
      localMessageDigest = MessageDigest.getInstance("MD5");
    }
    catch (Exception localException)
    {
      throw new RuntimeException("MD5 cryptographic algorithm is not available.", localException);
    }
    byte[] arrayOfByte = localMessageDigest.digest(paramArrayOfByte);
    BigInteger localBigInteger = new BigInteger(1, arrayOfByte);
    StringBuffer localStringBuffer = new StringBuffer(48);
    localStringBuffer.append(localBigInteger.toString(16));
    return localStringBuffer.toString();
  }
}