package org.ohuyo.servlet.servlets.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.IOUtils;

/**
 * MIME 类型处理类
 * 
 * @author rabbit
 * 
 */
public class MimeType {
	/**
	 * 默认MIME类型
	 */
	private final static String DEFAULT_MIME = "application/octet-stream";

	/**
	 * 
	 */
	private Properties mimeProperties;

	public MimeType() throws IOException {
		URL url = MimeType.class.getResource("mime.properties");
		mimeProperties = new Properties();
		InputStream in = null;
		try {
			in = url.openStream();
			mimeProperties.load(in);
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	/**
	 * 根据文件扩展名获取MIME <br/>
	 * 如果获取不到，默认返回"application/octet-stream"
	 * 
	 * @param fileNameExtension
	 * @return
	 */
	public String mimeByExtension(String fileNameExtension) {
		if (fileNameExtension == null) {
			return DEFAULT_MIME;
		}
		String mime = mimeProperties.getProperty(fileNameExtension);
		if (mime == null) {
			return DEFAULT_MIME;
		}
		return mime;
	}

	public static void main(String[] args) throws IOException,
			ConfigurationException {
		MimeType m = new MimeType();
		for (Entry<Object, Object> e : m.mimeProperties.entrySet()) {
			System.out.println(e.getKey() + " = " + e.getValue());
		}
	}
}
