package com.jgalante.classloader;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

public class MainClass {
	public static void main(String[] args)
			throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		MyClassLoader toolClassLoader = new MyClassLoader();
		toolClassLoader.getInstances().forEach(instance -> {
			System.out.println(instance.message());
		});
	}
}
