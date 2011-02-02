package a;
import java.net.*;
import java.lang.reflect.*;

public class LoaderSample2 {
	public static void main(String[] args) {
		try {
			String path = System.getProperty("user.dir");
			URL[] us = { new URL("file://" + path + "/sub/") };
			ClassLoader loader = new URLClassLoader(us);
			Class c = loader.loadClass("LoaderSample3");
			Object o = c.newInstance();
			Field f = c.getField("age");
			int age = f.getInt(o);
			System.out.println("age is " + age);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}