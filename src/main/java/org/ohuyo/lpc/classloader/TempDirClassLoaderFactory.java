package org.ohuyo.lpc.classloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * ��ʱĿ¼ClassLoader����
 * 
 * @author rabbit
 * 
 */
public class TempDirClassLoaderFactory implements InitializingBean,
		ClassLoaderFactory {
	/**
	 * Ӧ�ð��Ļ���Ŀ¼
	 */
	private String base = "free-0.0.1-SNAPSHOT";
	/**
	 * libĿ¼
	 */
	private String libPath = "/lib";
	/**
	 * confĿ¼
	 */
	private String confPath = "/conf";
	/**
	 * classesĿ¼
	 */
	private String clazzzPath = "/classes";
	/**
	 * ��ѹ����ʱĿ¼
	 */
	private String tmpPath = null;

	/**
	 * Ӧ�ð�
	 */
	private File packageFile;

	/**
	 * ��һ��classLoader
	 */
	private ClassLoader parent;

	/**
	 * classLoader
	 */
	private URLClassLoader classLoader;

	/**
	 * 
	 * @param packageFile
	 *            javaӦ�ð�
	 * @param tmpPath
	 *            ��ѹ�õ���ʱĿ¼
	 * @param parent
	 *            ��ClassLoader
	 * @throws IOException
	 */
	public TempDirClassLoaderFactory(File packageFile, String tmpPath,
			ClassLoader parent) {
		this.tmpPath = tmpPath;
		this.packageFile = packageFile;
		this.parent = parent;
	}

	/**
	 * ȡ�Ľ�ѹ���classpath
	 * 
	 * @param dir
	 * @return
	 * @throws MalformedURLException
	 */
	private URL[] getUrls(File dir) throws MalformedURLException {
		File lib = new File(dir, base + libPath);
		File conf = new File(dir, base + confPath);
		File clazzes = new File(dir, base + clazzzPath);
		File[] fils = lib.listFiles();
		List<URL> l = new ArrayList<URL>();
		if (clazzes.isDirectory()) {
			l.add(clazzes.toURI().toURL());
		}
		if (conf.isDirectory()) {
			l.add(conf.toURI().toURL());
		}
		for (int i = 0; i < fils.length; i++) {
			File file = fils[i];
			if (file.getName().endsWith(".jar")) {
				l.add(file.toURI().toURL());
			}
		}
		return l.toArray(new URL[l.size()]);

	}

	public void afterPropertiesSet() throws Exception {
		File tmp = decompressToTempDir();
		URL[] urls = getUrls(tmp);
		for (int i = 0; i < urls.length; i++) {
			URL url = urls[i];
			System.out.println(url);
		}
		classLoader = new URLClassLoader(urls, parent);
	}

	/**
	 * ��Ӧ�ý�ѹ����ʱĿ¼
	 * 
	 * @return
	 * @throws IOException
	 */
	private File decompressToTempDir() throws IOException {
		File tmp = File.createTempFile("lpc", "tmp", new File(tmpPath));
		tmp.delete();
		tmp.mkdir();
		File tmpDir = new File(tmp, packageFile.getName());
		if (tmpDir.exists()) {
			FileUtils.deleteQuietly(tmpDir);
		}
		JarFile jarFile = new JarFile(packageFile);
		Enumeration<JarEntry> e = jarFile.entries();
		while (e.hasMoreElements()) {
			JarEntry je = e.nextElement();
			InputStream is = null;
			FileOutputStream fos = null;
			try {
				is = jarFile.getInputStream(je);
				File f = new File(tmpDir, je.getName());
				if (!f.exists()) {
					if (je.isDirectory()) {
						f.mkdirs();
						continue;
					} else {
						f.createNewFile();
					}
				}
				fos = new FileOutputStream(f.getAbsolutePath());
				IOUtils.copyLarge(is, fos);
			} finally {
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(fos);
			}
		}
		return tmpDir;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	public static void main(String[] args) throws Exception {
		File file = new File(
				"E:\\MyEclipse\\Workspaces\\free\\target\\free-0.0.1-SNAPSHOT-pa1ckage.jar");
		FileInputStream fis = new FileInputStream(file);

		TempDirClassLoaderFactory clf = new TempDirClassLoaderFactory(file,
				"e:\\lpcc", null);
		clf.afterPropertiesSet();
		URL u = clf.getClassLoader().getResource(
				"org/apache/commons/codec/BinaryDecoder.class");
		System.out.println(u);
	}
}
