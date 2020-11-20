package cn.suanzi.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 类文件加载器
 * 
 * @author 刘卫平
 * @version 1.0.0 2014-1-7
 */
public class ClassFileLoader extends ClassLoader {

	/**
	 * 读取文件
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	private byte[] getBytes(String filename) throws IOException {
		File file = new File(filename);
		long len = file.length();
		byte[] raw = new byte[(int) len];

		try (FileInputStream fin = new FileInputStream(file);
				BufferedInputStream bin = new BufferedInputStream(fin)) {

			int r = bin.read(raw, 0, (int) len);
			if (r != len) {
				throw new IOException("加载文件失败：长度" + r + " != " + len);
			}
			return raw;
		}

	}

	/**
	 * 从给定文件中加载类
	 * 
	 * @param clazz
	 *            类名
	 * @param sFile
	 *            文件路径
	 * @return
	 * @throws ClassNotFoundException
	 */
	public Class<?> loadClass(String clazz, String sFile)
			throws ClassNotFoundException {
		Class<?> cls = findLoadedClass(clazz);
		if (cls != null) {
			return cls;
		}
		cls = findSystemClass(clazz);
		if (cls == null) {
			try {
				byte[] data = getBytes(sFile);

				return defineClass(clazz, data, 0, data.length);
			} catch (IOException localIOException) {
			}
		}
		if (cls == null) {
			throw new ClassNotFoundException(clazz);
		}
		return cls;
	}

	/**
	 * 强制加载类
	 * 
	 * @param clazz
	 * @param sFile
	 * @return
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("rawtypes")
	public Class loadClassForced(String clazz, String sFile)
			throws ClassNotFoundException {
		Class cls = null;
		try {
			byte[] data = getBytes(sFile);
			if (data == null) {
				cls = findSystemClass(clazz);
			}
			return defineClass(clazz, data, 0, data.length);
		} catch (IOException io) {
			cls = findSystemClass(clazz);
			if (cls == null) {
				throw new ClassNotFoundException(clazz);
			}
		}
		return cls;
	}

	/**
	 * 激活方法
	 * 
	 * @param className
	 *            类名
	 * @param sFile
	 *            文件路径
	 * @param methodName
	 *            方法名
	 * @param args
	 *            方法参数
	 * @return
	 */
	public Object invokeClassMethod(String className, String sFile,
			String methodName, Object[] args) {
		try {
			@SuppressWarnings("rawtypes")
			Class clazz = loadClass(className, sFile);
			Method[] methods = clazz.getMethods();
			for (int k = 0; k < methods.length; k++) {
				Method m = methods[k];
				if (m.getName().equals(methodName)) {
					return m.invoke(clazz.newInstance(), args);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将类名转化为文件路径。
	 * 
	 * @param sBasePath
	 *            基础文件路径，例如 application/bin/
	 * @param clazz
	 *            类名，例如 cn.com.singlee.slice.Main
	 * @return
	 */
	public static String classToFile(String sBasePath, String clazz) {
		return sBasePath + clazz.replace('.', '/').concat(".class");
	}
}