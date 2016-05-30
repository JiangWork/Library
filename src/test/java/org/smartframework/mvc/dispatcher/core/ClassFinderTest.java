package org.smartframework.mvc.dispatcher.core;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.smartframework.common.utils.ClassUtils;
import org.smartframework.mvc.dispatcher.annotation.Url;

public class ClassFinderTest {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		ConsoleAppender console = new ConsoleAppender();
		console.activateOptions();
		console.setThreshold(Level.DEBUG);
		console.setLayout(new PatternLayout("%m%n"));
		Logger.getRootLogger().addAppender(console);
		
		Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("");
		while(urls.hasMoreElements()) {
			URL url = urls.nextElement();
			System.out.println(url);
		}
		
		System.out.println(ClassUtils.findPackages(""));
		System.out.println(ClassUtils.findAllClasses("org.smart.framework.core"));
		System.out.println(ClassUtils.findAllClasses("org.smart.framework.core1"));
		System.out.println(ClassUtils.findClassesNameEnd("org.smart.framework.core", "Controller"));
		System.out.println(ClassUtils.findPackages("org.apache.log4j"));
		System.out.println(ClassUtils.findAllClasses("org.apache.log4j"));
		System.out.println(ClassUtils.findClassesWithAnnotation("org.smart.framework.core", Url.class));
		//Class.forName("ClassFinderTest");
		//Class.forName("ClassFinderTest");
	}

}
