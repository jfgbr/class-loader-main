package com.jgalante.classloader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class MyClassLoader_OLD {
	private final Class<?>[] parameters = new Class[] { URL.class };

	private Set<Object> objects;

	private ClassLoader urlcl;

	public MyClassLoader_OLD() {
		this.urlcl = ClassLoader.getSystemClassLoader();
		this.objects = new TreeSet<>();
		dir(Paths.get("data/tools"));
	}

	public void dir(Path path) {
		try {
			Files.walk(path, 1).forEach(filePath -> {
				try {
					if (filePath.toString().toLowerCase().endsWith(".jar")) {
						File file = filePath.toFile();
						addFile(file);

						Object object = loadObject(file.getName());
						objects.add(object);
					}

				} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
						| IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Set<Object> getObjects() {
		return objects;
	}

	public Object loadObject(String objectName)
			throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> clazz = loadClass(toolQualifiedName(objectName));
		Constructor<?> constructor = clazz.getConstructor();
		return (Object) constructor.newInstance();
	}

	private String toolQualifiedName(String objectName) {
		StringBuffer result = new StringBuffer("com.jgalante");
		String[] parts = objectName.split("-");
		for (String part : parts) {
			if (!Pattern.matches("\\d", part.substring(0, 1))) {
				result.append(String.valueOf(part.charAt(0)).toUpperCase());
				result.append(part.substring(1));
			}
		}
		result.append("Obj");
		return result.toString();
	}

	public ClassLoader loader(String jarFileName) throws MalformedURLException {
		if (this.urlcl == null) {
			this.urlcl = ClassLoader.getSystemClassLoader();
		}
		try {
			addFile("data/tools/" + jarFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.urlcl;
	}

	/**
	 * Adds a file to the classpath.
	 * 
	 * @param s
	 *            a String pointing to the file
	 * @throws IOException
	 */
	public void addFile(String s) throws IOException {
		File f = new File(s);
		addFile(f);
	}

	/**
	 * Adds a file to the classpath
	 * 
	 * @param f
	 *            the file to be added
	 * @throws IOException
	 */
	private void addFile(File f) throws IOException {
		addURL(f.toURI().toURL());
	}

	/**
	 * Adds the content pointed by the URL to the classpath.
	 * 
	 * @param u
	 *            the URL pointing to the content to be added
	 * @throws IOException
	 */
	private void addURL(URL u) throws IOException {
		URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
		Class<?> sysclass = URLClassLoader.class;
		try {
			Method method = sysclass.getDeclaredMethod("addURL", parameters);
			method.setAccessible(true);
			method.invoke(sysloader, new Object[] { u });
		} catch (Throwable t) {
			t.printStackTrace();
			throw new IOException("Error, could not add URL to system classloader");
		}
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return this.urlcl.loadClass(name);
	}
}
