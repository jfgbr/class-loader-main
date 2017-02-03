package com.jgalante.classloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class MyClassLoader {

	private Set<InterfaceClass> instances;

	public MyClassLoader() {
		instances = new HashSet<>();
		dir(Paths.get("lib"));
	}

	public void dir(Path path) {
		try {
			Files.walk(path, 1).forEach(filePath -> {
				try {
					if (filePath.toString().toLowerCase().endsWith(".jar")) {
						File file = filePath.toFile();
						URLClassLoader urlloader = URLClassLoader.newInstance(new URL[] { file.toURI().toURL() });
						Class<?> clazz = urlloader.loadClass("com.jgalante.classloader.ClassA");
						InterfaceClass interfaceClass = (InterfaceClass) clazz.newInstance();
						instances.add(interfaceClass);
					}

				} catch (ClassNotFoundException | SecurityException | InstantiationException | IllegalAccessException
						| IllegalArgumentException | IOException e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Set<InterfaceClass> getInstances() {
		return instances;
	}

}
