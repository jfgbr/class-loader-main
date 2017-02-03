package com.jgalante.classloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import com.jgalante.exception.ClassLoaderException;


public class MyClassLoader<T> {

	private static final String JAR = ".jar";
	
	private static final String LIB = "lib";
	
	private Set<T> instances;

	public MyClassLoader() {
		instances = new HashSet<>();
	}

	public void load(String commonClass) {
		loadPath(Paths.get(LIB), commonClass);
	}

	public void load(String path, String commonClass) {
		loadPath(Paths.get(path), commonClass);
	}

	protected void loadPath(Path path, String commonClass) {
		try {
			Files.walk(path, 1).forEach(filePath -> {
				if (filePath.toString().toLowerCase().endsWith(JAR)) {
					File file = filePath.toFile();
					T instance;
					try {
						instance = createInstance(commonClass, file);

						if (instance != null) {
							instances.add(instance);
						}
					} catch (MalformedURLException | ClassNotFoundException | InstantiationException
							| IllegalAccessException e) {
						throw new ClassLoaderException(e);
					}
				}

			});
		} catch (IOException e) {
			throw new ClassLoaderException(e);
		}
	}

	@SuppressWarnings("unchecked")
	protected T createInstance(String commonClass, File file)
			throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		URLClassLoader urlloader = URLClassLoader.newInstance(new URL[] { file.toURI().toURL() });
		Class<?> clazz = urlloader.loadClass(commonClass);
		return (T) clazz.newInstance();
	}

	public Set<T> getInstances() {
		return instances;
	}

}
