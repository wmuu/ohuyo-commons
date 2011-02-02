package com.cj.etag;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletOutputStream;

public class ETagResponseStream extends ServletOutputStream
{
  private boolean closed = false;
  private OutputStream stream = null;

  public ETagResponseStream(OutputStream paramOutputStream)
    throws IOException
  {
    this.stream = paramOutputStream;
  }

  public void close()
    throws IOException
  {
    if (!(this.closed))
    {
      this.stream.close();
      this.closed = true;
    }
  }

  public void flush()
    throws IOException
  {
    if (!(this.closed))
      this.stream.flush();
  }

  public void write(int paramInt)
    throws IOException
  {
    if (!(this.closed))
      this.stream.write((byte)paramInt);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (!(this.closed))
      this.stream.write(paramArrayOfByte, paramInt1, paramInt2);
  }

  public void write(byte[] paramArrayOfByte)
    throws IOException
  {
    write(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public boolean closed()
  {
    return this.closed;
  }

  public void reset()
  {
  }
}