package org.ohuyo.lpc.classloader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveInputStream;
import org.apache.commons.io.IOUtils;

/**
 * 
 * @author rabbit
 * 
 */
public class ClassLoader2 extends ClassLoader {
	private Map<String, String> classCache = new LinkedHashMap<String, String>();
	private File pkg;

	public ClassLoader2(File pkg, ClassLoader cl) throws IOException {
		this.pkg = pkg;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(pkg);
			show(fis);
		} finally {
			IOUtils.closeQuietly(fis);
		}
		Set<Entry<String, String>> eSet = classCache.entrySet();
		for (Entry<String, String> entry : eSet) {
			System.out.println("key=" + entry.getKey() + ", value="
					+ entry.getValue());
		}
	}

	public static void main(String[] args) throws IOException {
		File file = new File(
				"E:\\MyEclipse\\Workspaces\\free\\target\\free-0.0.1-SNAPSHOT-pa1ckage.jar");
		ClassLoader2 cl = new ClassLoader2(file, null);
		InputStream is = cl.getResourceAsStream("org/rabbit/free/MD5.class");
		System.out.println(is);
	}

	public void show(InputStream is) throws IOException {
		JarArchiveInputStream jais = new JarArchiveInputStream(is);
		JarArchiveEntry e = jais.getNextJarEntry();
		while (e != null) {
			if ((!e.isDirectory()) && e.getName().endsWith(".jar")) {
				cache(e.getName(), jais);
			}
			e = jais.getNextJarEntry();
		}
	}

	private void cache(String name, InputStream is) throws IOException {
		JarArchiveInputStream jais = null;
		try {
			jais = new JarArchiveInputStream(is);
			JarArchiveEntry e = jais.getNextJarEntry();
			while (e != null) {
				if (!e.isDirectory()) {
					classCache.put(e.getName(), name);
				}
				e = jais.getNextJarEntry();
			}
		} finally {
			// IOUtils.closeQuietly(jais);
		}
	}

	private InputStream getResourceAsStream(String name, InputStream is)
			throws IOException {
		JarArchiveInputStream jais = new JarArchiveInputStream(is);
		JarArchiveEntry e = jais.getNextJarEntry();
		while (e != null) {
			if ((!e.isDirectory()) && e.getName().equals(name)) {
				return jais;
			}
			e = jais.getNextJarEntry();
		}
		return null;
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		String s = classCache.get(name);
		if (s == null) {
			return super.getResourceAsStream(name);
		}
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(pkg);
			InputStream is = getResourceAsStream(s, fis);
			return getResourceAsStream(name, is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream ret = super.getResourceAsStream(name);
		if (ret == null) {
		}
		return null;
	}
}
