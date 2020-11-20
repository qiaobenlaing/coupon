package cn.suanzi.tools;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class JarLoader extends ClassLoader {
	private ZipFile zip;
	private List<String> listClass = new ArrayList<>();
	private List<String> listPackage = new ArrayList<>();
	private String sBasePath = "";

	public JarLoader(String sJarFile, String sClassPath) throws IOException {
		this.zip = new ZipFile(sJarFile);
		setClassPath(sClassPath);
		init();
	}

	public void init() {
		this.listClass.clear();
		Enumeration<? extends ZipEntry> entires = this.zip.entries();
		while (entires.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entires.nextElement();
			String name = entry.getName();
			if (name.endsWith(".class")) {
				this.listClass.add(fileToClass(name));
			} else if ((entry.isDirectory())
					&& (name.startsWith(this.sBasePath))
					&& (name.length() > this.sBasePath.length())) {
				this.listPackage.add(pathToPackage(name));
			}
		}
	}

	public void setClassPath(String sPath) {
		if ((sPath == null) || (sPath.equals("/")) || (sPath.equals(""))) {
			this.sBasePath = "";
		} else if (!sPath.endsWith("/")) {
			this.sBasePath = (sPath + "/");
		} else {
			this.sBasePath = sPath;
		}
	}

	public String[] getClassMethods(String className) {
		try {
			Class<?> clazz = loadClass(className);
			Method[] methods = clazz.getMethods();
			String[] sMethodName = new String[methods.length];
			int i = 0;
			for (int n = methods.length; i < n; i++) {
				sMethodName[i] = methods[i].getName();
			}
			return sMethodName;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object invokeClassMethod(String className, String methodName,
			Object[] args) {
		try {
			Class<?> clazz = loadClass(className);
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

	public String[] getClasses(String sClassPath) {
		String[] sRet = new String[0];
		List<String> lstRet = new ArrayList<>();
		int nLen = sClassPath.length();

		Iterator<String> iter = this.listClass.iterator();
		while (iter.hasNext()) {
			String sClass = (String) iter.next();
			if ((sClass.startsWith(sClassPath)) && (sClass.length() > nLen)) {
				lstRet.add(sClass);
			}
		}
		sRet = new String[lstRet.size()];
		Object[] obj = lstRet.toArray();
		int i = 0;
		for (int n = obj.length; i < n; i++) {
			sRet[i] = ((String) obj[i]);
		}
		return sRet;
	}

	public String[] getPackages(String sClassPath) {
		String[] sRet = new String[0];
		List<String> lstRet = new ArrayList<>();
		int nLen = sClassPath.length();

		Iterator<String> iter = this.listPackage.iterator();
		while (iter.hasNext()) {
			String sClass = (String) iter.next();
			if ((sClass.startsWith(sClassPath)) && (sClass.length() > nLen)) {
				lstRet.add(sClass);
			}
		}
		sRet = new String[lstRet.size()];
		Object[] obj = lstRet.toArray();
		int i = 0;
		for (int n = obj.length; i < n; i++) {
			sRet[i] = ((String) obj[i]);
		}
		return sRet;
	}

	public Class<?> loadClass(String clazz) throws ClassNotFoundException {
		Class<?> cls = findLoadedClass(clazz);
		if (cls != null) {
			return cls;
		}
		String name = classToFile(clazz);
		try {
			ZipEntry entry = this.zip.getEntry(name);
			if (entry == null) {
				cls = findSystemClass(clazz);
				if (cls != null) {
					return cls;
				}
			}
			InputStream in = this.zip.getInputStream(entry);
			int len = (int) entry.getSize();
			byte[] data = new byte[len];
			int success = 0;
			int offset = 0;
			while (success < len) {
				len -= success;
				offset += success;
				success = in.read(data, offset, len);
				if (success == -1) {
					throw new ClassNotFoundException(clazz);
				}
			}
			return defineClass(clazz, data, 0, data.length);
		} catch (IOException io) {
			throw new ClassNotFoundException(clazz);
		}
	}

	public static byte[] getJarResource(String sJarFileName, String sResource)
			throws IOException {
		ZipFile z = new ZipFile(sJarFileName);
		ZipEntry entry = z.getEntry(sResource);
		BufferedInputStream in = new BufferedInputStream(
				z.getInputStream(entry));
		int len = (int) entry.getSize();
		byte[] data = new byte[len];
		in.read(data);
		in.close();
		z.close();
		return data;
	}

	public byte[] getJarResource(String sResource) throws IOException {
		ZipEntry entry = this.zip.getEntry(sResource);
		BufferedInputStream in = new BufferedInputStream(
				this.zip.getInputStream(entry));
		int len = (int) entry.getSize();
		byte[] data = new byte[len];
		in.read(data);
		return data;
	}

	public InputStream getJarResourceInputSteam(String sResource)
			throws IOException {
		ZipEntry entry = this.zip.getEntry(sResource);

		BufferedInputStream in = new BufferedInputStream(
				this.zip.getInputStream(entry));
		return in;
	}

	public String classToFile(String clazz) {
		return this.sBasePath + clazz.replace('.', '/').concat(".class");
	}

	public String pathToPackage(String sPath) {
		String sRet = sPath.replace('/', '.');
		return sRet.substring(this.sBasePath.length(), sRet.length() - 1);
	}

	public String fileToClass(String sPath) {
		char[] clsName = sPath.toCharArray();
		for (int i = clsName.length - 6 - this.sBasePath.length(); i >= 0; i--) {
			if (clsName[i] == '/') {
				clsName[i] = '.';
			}
		}
		String sRet = new String(clsName, this.sBasePath.length(),
				clsName.length - this.sBasePath.length() - 6);
		return sRet;
	}

	public static void main(String[] args) {
		try {
			JarLoader loader = new JarLoader("./", "classes");
			@SuppressWarnings("unused")
			Class<?> clazz = loader
					.loadClass("cn.com.singlee.engine.analyzer.FixAnalyzer");
			String[] sPackage = loader
					.getPackages("cn.com.singlee.slice.stream");
			int i = 0;
			for (int n = sPackage.length; i < n; i++) {
				String[] sClass = loader.getClasses(sPackage[i]);
				int j = 0;
				for (int m = sClass.length; j < m; j++) {
					System.out.println("class[" + j + "] = " + sClass[j]);
					String sDesc = (String) loader.invokeClassMethod(sClass[j],
							"getPrferenceInfo", new Object[0]);
					System.out.println(sDesc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}