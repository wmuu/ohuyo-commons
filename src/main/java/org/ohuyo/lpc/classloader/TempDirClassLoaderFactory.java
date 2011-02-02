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
 * 临时目录ClassLoader工厂
 * 
 * @author rabbit
 * 
 */
public class TempDirClassLoaderFactory implements InitializingBean,
		ClassLoaderFactory {
	/**
	 * 应用包的基本目录
	 */
	private String base = "free-0.0.1-SNAPSHOT";
	/**
	 * lib目录
	 */
	private String libPath = "/lib";
	/**
	 * conf目录
	 */
	private String confPath = "/conf";
	/**
	 * classes目录
	 */
	private String clazzzPath = "/classes";
	/**
	 * 解压的临时目录
	 */
	private String tmpPath = null;

	/**
	 * 应用包
	 */
	private File packageFile;

	/**
	 * 上一级classLoader
	 */
	private ClassLoader parent;

	/**
	 * classLoader
	 */
	private URLClassLoader classLoader;

	/**
	 * 
	 * @param packageFile
	 *            java应用包
	 * @param tmpPath
	 *            解压用的临时目录
	 * @param parent
	 *            父ClassLoader
	 * @throws IOException
	 */
	public TempDirClassLoaderFactory(File packageFile, String tmpPath,
			ClassLoader parent) {
		this.tmpPath = tmpPath;
		this.packageFile = packageFile;
		this.parent = parent;
	}

	/**
	 * 取的解压完的classpath
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
	 * 将应用解压到临时目录
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
