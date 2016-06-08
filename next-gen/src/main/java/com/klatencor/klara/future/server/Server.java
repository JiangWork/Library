package com.klatencor.klara.future.server;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Server {
	
	private static ServerContext ctx;
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BeanFactory beanFactory = new ClassPathXmlApplicationContext("spring-beans.xml");
		
		ctx = beanFactory.getBean("serverContext", ServerContext.class);
	}
	
	
	public static ServerContext getContext() {
		if (ctx == null)  {
			throw new RuntimeException("ServerContext hasn't been instantiated.");
		}
		return ctx;
			
	}

}
