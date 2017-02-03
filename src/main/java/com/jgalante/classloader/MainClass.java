package com.jgalante.classloader;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

public class MainClass {

	
	private static final String INTERFACE_CLASS = "com.jgalante.classloader.ClassA";
	
	public static void main(String[] args)
			throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		MyClassLoader<InterfaceClass> toolClassLoader = new MyClassLoader<>();
		toolClassLoader.load(INTERFACE_CLASS);
		toolClassLoader.getInstances().forEach(instance -> {
			System.out.println(instance.message());
		});
	}
}
